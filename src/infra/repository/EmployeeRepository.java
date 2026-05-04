package infra.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeRepository {

    public static class FieldInvestigator {
        private final String employeeId;
        private final String name;
        private final String region;
        private final String specialty;
        private final int openCaseCount;

        public FieldInvestigator(String employeeId, String name,
                                 String region, String specialty, int openCaseCount) {
            this.employeeId    = employeeId;
            this.name          = name;
            this.region        = region;
            this.specialty     = specialty;
            this.openCaseCount = openCaseCount;
        }

        public String getEmployeeId()    { return employeeId; }
        public String getName()          { return name; }
        public String getRegion()        { return region; }
        public String getSpecialty()     { return specialty; }
        public int    getOpenCaseCount() { return openCaseCount; }
    }

    private static final List<FieldInvestigator> STORE = new ArrayList<>();

    static {
        STORE.add(new FieldInvestigator("EMP-1023", "이현수", "SEOUL-01",    "자동차 대물",  2));
        STORE.add(new FieldInvestigator("EMP-1045", "박지영", "SEOUL-01",    "자동차 대물",  4));
        STORE.add(new FieldInvestigator("EMP-1067", "최준호", "GYEONGGI-01", "자기차량손해", 1));
        STORE.add(new FieldInvestigator("EMP-1082", "정다은", "INCHEON-01",  "자동차 대물",  3));
    }

    public static List<FieldInvestigator> findByRegionAndSpecialty(String region, String specialty) {
        return STORE.stream()
            .filter(e -> region.isEmpty()   || e.getRegion().equalsIgnoreCase(region))
            .filter(e -> specialty.isEmpty() || e.getSpecialty().equals(specialty))
            .collect(Collectors.toList());
    }

    public static FieldInvestigator findById(String employeeId) {
        return STORE.stream()
            .filter(e -> e.getEmployeeId().equals(employeeId))
            .findFirst().orElse(null);
    }
}
