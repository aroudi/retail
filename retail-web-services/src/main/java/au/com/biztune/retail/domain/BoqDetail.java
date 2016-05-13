package au.com.biztune.retail.domain;

/**
 * Created by arash on 1/04/2016.
 */
public class BoqDetail {
    private long id;
    private BillOfQuantity billOfQuantity;
    private UnitOfMeasure unitOfMeasure;
    private Product product;
    private boolean prodIsNew;
    private double quantity;
    private double cost;
    private double margin;
    private double sellPrice;
    private Supplier supplier;
    private double qtyOnStock;
    private double qtyPurchased;
    private double qtyBalance;
    private ConfigCategory bqdStatus;
    private String comment;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BillOfQuantity getBillOfQuantity() {
        return billOfQuantity;
    }

    public void setBillOfQuantity(BillOfQuantity billOfQuantity) {
        this.billOfQuantity = billOfQuantity;
    }

    public UnitOfMeasure getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public boolean isProdIsNew() {
        return prodIsNew;
    }

    public void setProdIsNew(boolean prodIsNew) {
        this.prodIsNew = prodIsNew;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getMargin() {
        return margin;
    }

    public void setMargin(double margin) {
        this.margin = margin;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public double getQtyOnStock() {
        return qtyOnStock;
    }

    public void setQtyOnStock(double qtyOnStock) {
        this.qtyOnStock = qtyOnStock;
    }

    public double getQtyPurchased() {
        return qtyPurchased;
    }

    public void setQtyPurchased(double qtyPurchased) {
        this.qtyPurchased = qtyPurchased;
    }

    public double getQtyBalance() {
        return qtyBalance;
    }

    public void setQtyBalance(double qtyBalance) {
        this.qtyBalance = qtyBalance;
    }

    public ConfigCategory getBqdStatus() {
        return bqdStatus;
    }

    public void setBqdStatus(ConfigCategory bqdStatus) {
        this.bqdStatus = bqdStatus;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
