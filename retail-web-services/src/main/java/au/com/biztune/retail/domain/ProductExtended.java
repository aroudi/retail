package au.com.biztune.retail.domain;

public class ProductExtended extends Product {
    private long defaultSupplierId;
    private String defaultSupplierName;
    private double qtyAvailable;
    private double rrp;
    private double prodCost;
    private double gradeAPrice;
    private double gradeBPrice;
    private double gradeCPrice;
    private double gradeDPrice;
    private double defaultPrice;
    private String department;
    private String category1;
    private String category2;
    private String category3;

    public long getDefaultSupplierId() {
        return defaultSupplierId;
    }

    public void setDefaultSupplierId(long defaultSupplierId) {
        this.defaultSupplierId = defaultSupplierId;
    }

    public String getDefaultSupplierName() {
        return defaultSupplierName;
    }

    public void setDefaultSupplierName(String defaultSupplierName) {
        this.defaultSupplierName = defaultSupplierName;
    }

    public double getRrp() {
        return rrp;
    }

    public void setRrp(double rrp) {
        this.rrp = rrp;
    }

    public double getProdCost() {
        return prodCost;
    }

    public void setProdCost(double prodCost) {
        this.prodCost = prodCost;
    }

    public double getGradeAPrice() {
        return gradeAPrice;
    }

    public void setGradeAPrice(double gradeAPrice) {
        this.gradeAPrice = gradeAPrice;
    }

    public double getGradeBPrice() {
        return gradeBPrice;
    }

    public void setGradeBPrice(double gradeBPrice) {
        this.gradeBPrice = gradeBPrice;
    }

    public double getGradeCPrice() {
        return gradeCPrice;
    }

    public void setGradeCPrice(double gradeCPrice) {
        this.gradeCPrice = gradeCPrice;
    }

    public double getGradeDPrice() {
        return gradeDPrice;
    }

    public void setGradeDPrice(double gradeDPrice) {
        this.gradeDPrice = gradeDPrice;
    }

    public double getDefaultPrice() {
        return defaultPrice;
    }

    public void setDefaultPrice(double defaultPrice) {
        this.defaultPrice = defaultPrice;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCategory1() {
        return category1;
    }

    public void setCategory1(String category1) {
        this.category1 = category1;
    }

    public String getCategory2() {
        return category2;
    }

    public void setCategory2(String category2) {
        this.category2 = category2;
    }

    public String getCategory3() {
        return category3;
    }

    public void setCategory3(String category3) {
        this.category3 = category3;
    }

    public double getQtyAvailable() {
        return qtyAvailable;
    }

    public void setQtyAvailable(double qtyAvailable) {
        this.qtyAvailable = qtyAvailable;
    }
}
