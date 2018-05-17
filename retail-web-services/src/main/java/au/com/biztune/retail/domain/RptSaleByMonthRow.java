package au.com.biztune.retail.domain;

import java.sql.Timestamp;

public class RptSaleByMonthRow {
    private String supplierId;
    private String supplierName;
    private String prodSku;
    private String prodBarcode;
    private String prodName;
    private String prodRef;
    private String client;
    private double prodCost;
    private double profitMargin;
    private double prodProfit;
    private double taxPaid;
    private double prodPriceGross;
    private double prodPriceNet;
    private Timestamp invoiceDate;
    private double qtyInvoiced;
    private double priceSold;

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getProdSku() {
        return prodSku;
    }

    public void setProdSku(String prodSku) {
        this.prodSku = prodSku;
    }

    public String getProdBarcode() {
        return prodBarcode;
    }

    public void setProdBarcode(String prodBarcode) {
        this.prodBarcode = prodBarcode;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getProdRef() {
        return prodRef;
    }

    public void setProdRef(String prodRef) {
        this.prodRef = prodRef;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public double getProdCost() {
        return prodCost;
    }

    public void setProdCost(double prodCost) {
        this.prodCost = prodCost;
    }

    public double getProfitMargin() {
        return profitMargin;
    }

    public void setProfitMargin(double profitMargin) {
        this.profitMargin = profitMargin;
    }

    public double getProdProfit() {
        return prodProfit;
    }

    public void setProdProfit(double prodProfit) {
        this.prodProfit = prodProfit;
    }

    public double getTaxPaid() {
        return taxPaid;
    }

    public void setTaxPaid(double taxPaid) {
        this.taxPaid = taxPaid;
    }

    public double getProdPriceGross() {
        return prodPriceGross;
    }

    public void setProdPriceGross(double prodPriceGross) {
        this.prodPriceGross = prodPriceGross;
    }

    public double getProdPriceNet() {
        return prodPriceNet;
    }

    public void setProdPriceNet(double prodPriceNet) {
        this.prodPriceNet = prodPriceNet;
    }

    public Timestamp getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Timestamp invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public double getQtyInvoiced() {
        return qtyInvoiced;
    }

    public void setQtyInvoiced(double qtyInvoiced) {
        this.qtyInvoiced = qtyInvoiced;
    }

    public double getPriceSold() {
        return priceSold;
    }

    public void setPriceSold(double priceSold) {
        this.priceSold = priceSold;
    }
}
