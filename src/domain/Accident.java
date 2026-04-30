package domain;

import java.util.Date;

public class Accident {
    private Date accidentDate;
    private String accidentDetail;
    private String accidentId;
    private String accidentLocation;
    private AccidentType accidentType;
    private String reportedBy;
    private SeverityLevel severityLevel;

    public enum AccidentType {}
    public enum SeverityLevel {}

    public String getAccidentInfo() { return null; }
    public boolean updateAccidentDetail(String detail) { return false; }
    public boolean validateAccident() { return false; }

    public Date getAccidentDate() { return accidentDate; }
    public String getAccidentDetail() { return accidentDetail; }
    public String getAccidentId() { return accidentId; }
    public String getAccidentLocation() { return accidentLocation; }
    public AccidentType getAccidentType() { return accidentType; }
    public String getReportedBy() { return reportedBy; }
    public SeverityLevel getSeverityLevel() { return severityLevel; }
}
