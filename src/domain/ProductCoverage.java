package domain;

import java.io.Serializable;
import java.util.List;

public class ProductCoverage implements Serializable {
    private static final long serialVersionUID = 1L;
    private String coverageMasterId;
    private String coverageName;
    private CoverageType coverageType;
    private List<CoverageLimitOption> limitOptions;
    private boolean mandatory;
    private String productCoverageId;
    private String productId;

    public String getCoverageMasterId() { return coverageMasterId; }
    public String getCoverageName() { return coverageName; }
    public CoverageType getCoverageType() { return coverageType; }
    public List<CoverageLimitOption> getLimitOptions() { return limitOptions; }
    public boolean isMandatory() { return mandatory; }
    public String getProductCoverageId() { return productCoverageId; }
    public String getProductId() { return productId; }

    public void setCoverageMasterId(String v)               { this.coverageMasterId = v; }
    public void setCoverageName(String v)                   { this.coverageName = v; }
    public void setCoverageType(CoverageType v)             { this.coverageType = v; }
    public void setLimitOptions(List<CoverageLimitOption> v){ this.limitOptions = v; }
    public void setMandatory(boolean v)                     { this.mandatory = v; }
    public void setProductCoverageId(String v)              { this.productCoverageId = v; }
    public void setProductId(String v)                      { this.productId = v; }
}
