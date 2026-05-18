package infra.dao;

import domain.Rider;
import domain.RiderType;
import infra.persistence.Database;
import infra.persistence.ResultSetExtractor;

import java.util.List;

public class RiderDao {
    private static final RiderDao INSTANCE = new RiderDao();
    public static RiderDao getInstance() { return INSTANCE; }

    private static final Database DB = Database.getInstance();

    private static final ResultSetExtractor<Rider> EXTRACTOR = rs -> {
        Rider r = new Rider();
        r.setRiderId(rs.getString("rider_id"));
        r.setRiderCode(rs.getString("rider_code"));
        r.setRiderName(rs.getString("rider_name"));
        r.setDescription(rs.getString("description"));
        String typeStr = rs.getString("rider_type");
        if (typeStr != null) {
            try { r.setRiderType(RiderType.valueOf(typeStr)); } catch (Exception ignored) {}
        }
        r.setMandatory(rs.getInt("mandatory") == 1);
        r.setDiscountRate(rs.getDouble("discount_rate"));
        return r;
    };

    public Rider findByCode(String riderCode) {
        return DB.queryForObject(
            "SELECT * FROM riders WHERE rider_code = ?",
            EXTRACTOR, riderCode);
    }

    public List<Rider> findAll() {
        return DB.queryForList("SELECT * FROM riders", EXTRACTOR);
    }
}
