package au.com.biztune.retail.domain;

/**
 * Created by arash on 13/05/2016.
 */
public class PoBoqLink {
    private long id;
    private long pohId;
    private String pohOrderNumber;
    private long polId;
    private long boqId;
    private String boqName;
    private long projectId;
    private String projectCode;
    private long boqDetailId;
    private double boqQtyTotal;
    private double poQtyReceived;
    private double boqQtyBalance;
    private ConfigCategory status;
    private String projectName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPohId() {
        return pohId;
    }

    public void setPohId(long pohId) {
        this.pohId = pohId;
    }

    public String getPohOrderNumber() {
        return pohOrderNumber;
    }

    public void setPohOrderNumber(String pohOrderNumber) {
        this.pohOrderNumber = pohOrderNumber;
    }

    public long getPolId() {
        return polId;
    }

    public void setPolId(long polId) {
        this.polId = polId;
    }

    public long getBoqId() {
        return boqId;
    }

    public void setBoqId(long boqId) {
        this.boqId = boqId;
    }

    public String getBoqName() {
        return boqName;
    }

    public void setBoqName(String boqName) {
        this.boqName = boqName;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public double getBoqQtyTotal() {
        return boqQtyTotal;
    }

    public void setBoqQtyTotal(double boqQtyTotal) {
        this.boqQtyTotal = boqQtyTotal;
    }

    public double getPoQtyReceived() {
        return poQtyReceived;
    }

    public void setPoQtyReceived(double poQtyReceived) {
        this.poQtyReceived = poQtyReceived;
    }

    public double getBoqQtyBalance() {
        return boqQtyBalance;
    }

    public void setBoqQtyBalance(double boqQtyBalance) {
        this.boqQtyBalance = boqQtyBalance;
    }

    public ConfigCategory getStatus() {
        return status;
    }

    public void setStatus(ConfigCategory status) {
        this.status = status;
    }

    public long getBoqDetailId() {
        return boqDetailId;
    }

    public void setBoqDetailId(long boqDetailId) {
        this.boqDetailId = boqDetailId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
