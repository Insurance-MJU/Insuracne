package infra.dao;

import domain.Employee;
import infra.persistence.Database;
import infra.persistence.ResultSetExtractor;

import java.util.List;

public class EmployeeDao {
    private static final EmployeeDao INSTANCE = new EmployeeDao();
    public static EmployeeDao getInstance() { return INSTANCE; }

    private static final Database DB = Database.getInstance();

    private static final ResultSetExtractor<Employee.FieldInvestigator> EXTRACTOR = rs ->
        new Employee.FieldInvestigator(
            rs.getString("employee_id"),
            rs.getString("name"),
            rs.getString("specialty"),
            rs.getInt("open_case_count")
        );

    public List<Employee.FieldInvestigator> findBySpecialty(String specialty) {
        if (specialty == null || specialty.isEmpty()) {
            return DB.queryForList("SELECT * FROM employees", EXTRACTOR);
        }
        return DB.queryForList(
            "SELECT * FROM employees WHERE specialty = ?",
            EXTRACTOR, specialty);
    }

    public Employee.FieldInvestigator findById(String employeeId) {
        return DB.queryForObject(
            "SELECT * FROM employees WHERE employee_id = ?",
            EXTRACTOR, employeeId);
    }
}
