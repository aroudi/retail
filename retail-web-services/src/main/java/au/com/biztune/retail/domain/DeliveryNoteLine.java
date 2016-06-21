package au.com.biztune.retail.domain;

/**
 * Created by arash on 27/05/2016.
 */
public class DeliveryNoteLine {
    private long id;
    private long delnId;
    private long polId;
    private String delnGrn;
    private int dlnlLineNumber;
    private ProductPurchaseItem purchaseItem;
    private UnitOfMeasure dlnlCaseSize;
    private UnitOfMeasure dlnlProductSize;
    private double dlnlUnitCost;
    private double dlnlQty;
    private UnitOfMeasure rcvdCaseSize;
    private UnitOfMeasure rcvdProductSize;
    private double rcvdQty;
    private double totalCost;
    private int dlnlQtyRegected;
    private ConfigCategory dlnlStatus;
    private boolean dlnlDiscrepancy;
    private double polQty;
    private String dlnlComment;
    private boolean deleted;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDelnId() {
        return delnId;
    }

    public void setDelnId(long delnId) {
        this.delnId = delnId;
    }

    public long getPolId() {
        return polId;
    }

    public void setPolId(long polId) {
        this.polId = polId;
    }

    public String getDelnGrn() {
        return delnGrn;
    }

    public void setDelnGrn(String delnGrn) {
        this.delnGrn = delnGrn;
    }

    public int getDlnlLineNumber() {
        return dlnlLineNumber;
    }

    public void setDlnlLineNumber(int dlnlLineNumber) {
        this.dlnlLineNumber = dlnlLineNumber;
    }

    public ProductPurchaseItem getPurchaseItem() {
        return purchaseItem;
    }

    public void setPurchaseItem(ProductPurchaseItem purchaseItem) {
        this.purchaseItem = purchaseItem;
    }

    public UnitOfMeasure getDlnlCaseSize() {
        return dlnlCaseSize;
    }

    public void setDlnlCaseSize(UnitOfMeasure dlnlCaseSize) {
        this.dlnlCaseSize = dlnlCaseSize;
    }

    public UnitOfMeasure getDlnlProductSize() {
        return dlnlProductSize;
    }

    public void setDlnlProductSize(UnitOfMeasure dlnlProductSize) {
        this.dlnlProductSize = dlnlProductSize;
    }

    public double getDlnlUnitCost() {
        return dlnlUnitCost;
    }

    public void setDlnlUnitCost(double dlnlUnitCost) {
        this.dlnlUnitCost = dlnlUnitCost;
    }

    public double getDlnlQty() {
        return dlnlQty;
    }

    public void setDlnlQty(double dlnlQty) {
        this.dlnlQty = dlnlQty;
    }

    public UnitOfMeasure getRcvdCaseSize() {
        return rcvdCaseSize;
    }

    public void setRcvdCaseSize(UnitOfMeasure rcvdCaseSize) {
        this.rcvdCaseSize = rcvdCaseSize;
    }

    public UnitOfMeasure getRcvdProductSize() {
        return rcvdProductSize;
    }

    public void setRcvdProductSize(UnitOfMeasure rcvdProductSize) {
        this.rcvdProductSize = rcvdProductSize;
    }

    public double getRcvdQty() {
        return rcvdQty;
    }

    public void setRcvdQty(double rcvdQty) {
        this.rcvdQty = rcvdQty;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public int getDlnlQtyRegected() {
        return dlnlQtyRegected;
    }

    public void setDlnlQtyRegected(int dlnlQtyRegected) {
        this.dlnlQtyRegected = dlnlQtyRegected;
    }

    public ConfigCategory getDlnlStatus() {
        return dlnlStatus;
    }

    public void setDlnlStatus(ConfigCategory dlnlStatus) {
        this.dlnlStatus = dlnlStatus;
    }

    public boolean isDlnlDiscrepancy() {
        return dlnlDiscrepancy;
    }

    public void setDlnlDiscrepancy(boolean dlnlDiscrepancy) {
        this.dlnlDiscrepancy = dlnlDiscrepancy;
    }

    public double getPolQty() {
        return polQty;
    }

    public void setPolQty(double polQty) {
        this.polQty = polQty;
    }

    public String getDlnlComment() {
        return dlnlComment;
    }

    public void setDlnlComment(String dlnlComment) {
        this.dlnlComment = dlnlComment;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
