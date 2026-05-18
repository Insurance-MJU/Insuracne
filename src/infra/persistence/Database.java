package infra.persistence;

import common.exception.dao.DatabaseException;
import infra.config.DBConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Database implements SqlExecutor {

    private static Database instance;

    public static Database getInstance() {
        if (instance == null) instance = new Database(DBConfig.defaults());
        return instance;
    }

    private final DBConfig config;
    private final BlockingQueue<Connection> pool;

    public Database(DBConfig config) {
        this.config = config;
        this.pool = new ArrayBlockingQueue<>(config.getPoolSize());
        initialize();
    }

    private void initialize() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            for (int i = 0; i < config.getPoolSize(); i++) {
                pool.offer(newConnection());
            }
            System.out.println("[DB] Connection pool initialized (size=" + config.getPoolSize() + ")");
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize connection pool", e);
        }
    }

    private Connection newConnection() throws SQLException {
        return DriverManager.getConnection(config.getUrl(), config.getUsername(), config.getPassword());
    }

    private Connection acquire() {
        try {
            Connection conn = pool.poll(3, TimeUnit.SECONDS);
            if (Objects.requireNonNull(conn).isClosed() || !conn.isValid(1)) {
                conn = newConnection();
            }
            return conn;
        } catch (Exception e) {
            throw new DatabaseException("Failed to acquire connection", e);
        }
    }

    private void release(Connection conn) {
        if (conn != null) pool.offer(conn);
    }

    @Override
    public int executeInsert(String sql, Object... params) {
        Connection conn = acquire();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bind(ps, params);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
                return -1;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Insert failed: " + e.getMessage(), e);
        } finally {
            release(conn);
        }
    }

    @Override
    public void execute(String sql, Object... params) {
        Connection conn = acquire();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            bind(ps, params);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Execute failed: " + e.getMessage(), e);
        } finally {
            release(conn);
        }
    }

    @Override
    public <T> T queryForObject(String sql, ResultSetExtractor<T> extractor, Object... params) {
        Connection conn = acquire();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            bind(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? extractor.extract(rs) : null;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Query failed: " + e.getMessage(), e);
        } finally {
            release(conn);
        }
    }

    @Override
    public <T> List<T> queryForList(String sql, ResultSetExtractor<T> extractor, Object... params) {
        Connection conn = acquire();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            bind(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                List<T> result = new ArrayList<>();
                while (rs.next()) result.add(extractor.extract(rs));
                return result;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Query failed: " + e.getMessage(), e);
        } finally {
            release(conn);
        }
    }

    private void bind(PreparedStatement ps, Object[] params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
    }

    public void shutdown() {
        pool.forEach(conn -> {
            try { conn.close(); } catch (SQLException ignored) {}
        });
        pool.clear();
        System.out.println("[DB] Connection pool shut down");
    }
}
