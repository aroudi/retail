package au.com.biztune.retail.domain;

/**
 * Created by arash on 6/07/2017.
 */
public class PoSoLink {
    private long id;
    private long pohId;
    private String pohOrderNumber;
    private long polId;
    private long txhdId;
    private long projectId;
    private String projectCode;
    private long txdeId;
    private double soLineQtyTotal;
    private double poQtyReceived;
    private double soLineQtyBalance;
    private String comment;
    private ConfigCategory poSoStatus;

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

    public long getTxhdId() {
        return txhdId;
    }

    public void setTxhdId(long txhdId) {
        this.txhdId = txhdId;
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

    public long getTxdeId() {
        return txdeId;
    }

    public void setTxdeId(long txdeId) {
        this.txdeId = txdeId;
    }

    public double getSoLineQtyTotal() {
        return soLineQtyTotal;
    }

    public void setSoLineQtyTotal(double soLineQtyTotal) {
        this.soLineQtyTotal = soLineQtyTotal;
    }

    public double getPoQtyReceived() {
        return poQtyReceived;
    }

    public void setPoQtyReceived(double poQtyReceived) {
        this.poQtyReceived = poQtyReceived;
    }

    public double getSoLineQtyBalance() {
        return soLineQtyBalance;
    }

    public void setSoLineQtyBalance(double soLineQtyBalance) {
        this.soLineQtyBalance = soLineQtyBalance;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ConfigCategory getPoSoStatus() {
        return poSoStatus;
    }

    public void setPoSoStatus(ConfigCategory poSoStatus) {
        this.poSoStatus = poSoStatus;
    }
}
