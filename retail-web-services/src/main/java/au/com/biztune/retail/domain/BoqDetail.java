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
}
