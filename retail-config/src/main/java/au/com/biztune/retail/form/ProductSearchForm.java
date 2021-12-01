package au.com.biztune.retail.form;

import au.com.biztune.retail.domain.GeneralSearchForm;

/**
 * Created by arash on 30/01/2017.
 */
public class ProductSearchForm extends GeneralSearchForm {
    private String prodSku;
    private String prodName;
    private String reference;
    private long prodTypeId;
    private long prodStatusId;
    private long inStockQtyFrom;
    private long inStockQtyTo;


    public String getProdSku() {
        return prodSku;
    }

    public void setProdSku(String prodSku) {
        this.prodSku = prodSku;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public long getProdTypeId() {
        return prodTypeId;
    }

    public void setProdTypeId(long prodTypeId) {
        this.prodTypeId = prodTypeId;
    }

    public long getInStockQtyFrom() {
        return inStockQtyFrom;
    }

    public void setInStockQtyFrom(long inStockQtyFrom) {
        this.inStockQtyFrom = inStockQtyFrom;
    }

    public long getInStockQtyTo() {
        return inStockQtyTo;
    }

    public void setInStockQtyTo(long inStockQtyTo) {
        this.inStockQtyTo = inStockQtyTo;
    }

    public long getProdStatusId() {
        return prodStatusId;
    }

    public void setProdStatusId(long prodStatusId) {
        this.prodStatusId = prodStatusId;
    }
}
