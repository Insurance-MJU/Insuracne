package infra.dao;

import domain.common.User;
import domain.common.UserRole;
import infra.persistence.Database;
import infra.persistence.ResultSetExtractor;

public class UserDao {
    private static final UserDao INSTANCE = new UserDao();
    public static UserDao getInstance() { return INSTANCE; }

    private static final Database DB = Database.getInstance();

    private static final ResultSetExtractor<User> EXTRACTOR = rs -> new User(
        rs.getString("user_id"),
        rs.getString("password"),
        rs.getString("name"),
        UserRole.valueOf(rs.getString("role"))
    );

    public User findByCredentials(String userId, String password) {
        User user = DB.queryForObject(
            "SELECT * FROM users WHERE user_id = ?", EXTRACTOR, userId);
        if (user != null && user.getPassword().equals(password)) return user;
        return null;
    }
}
