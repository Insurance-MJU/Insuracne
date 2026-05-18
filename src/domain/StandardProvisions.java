package domain;

import java.util.List;

public class StandardProvisions {
    private String description;
    private List<Exclusion> exclusions;
    private String standardProvisionId;
    private String title;

    public String getDescription()             { return description; }
    public List<Exclusion> getExclusions()     { return exclusions; }
    public String getStandardProvisionId()     { return standardProvisionId; }
    public String getTitle()                   { return title; }

    public void setDescription(String v)             { this.description = v; }
    public void setExclusions(List<Exclusion> v)     { this.exclusions = v; }
    public void setStandardProvisionId(String v)     { this.standardProvisionId = v; }
    public void setTitle(String v)                   { this.title = v; }
}
