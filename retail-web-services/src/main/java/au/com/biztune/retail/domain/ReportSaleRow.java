package au.com.biztune.retail.domain;

import java.sql.Timestamp;

/**
 * created by Arash Roudi 10/05/2018.
 */
public class ReportSaleRow {
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
    private double totalExTax;
    private double totalIncTax;
    private String taxDesc;
    private String taxCode;
    private String txivTxnNr;
    private double taxRate;
    private String operator;
    private String deptName;
    private String prodCat1;
    private String prodCat2;
    private String prodCat3;
    private double qtyAvailable;
    private double stockValue;
    public String getSupplierId() {
        return supplierId;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
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

    public double getTotalExTax() {
        return totalExTax;
    }

    public void setTotalExTax(double totalExTax) {
        this.totalExTax = totalExTax;
    }

    public double getTotalIncTax() {
        return totalIncTax;
    }

    public void setTotalIncTax(double totalIncTax) {
        this.totalIncTax = totalIncTax;
    }

    public String getTaxDesc() {
        return taxDesc;
    }

    public void setTaxDesc(String taxDesc) {
        this.taxDesc = taxDesc;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getTxivTxnNr() {
        return txivTxnNr;
    }

    public void setTxivTxnNr(String txivTxnNr) {
        this.txivTxnNr = txivTxnNr;
    }

    public double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getProdCat1() {
        return prodCat1;
    }

    public void setProdCat1(String prodCat1) {
        this.prodCat1 = prodCat1;
    }

    public String getProdCat2() {
        return prodCat2;
    }

    public void setProdCat2(String prodCat2) {
        this.prodCat2 = prodCat2;
    }

    public String getProdCat3() {
        return prodCat3;
    }

    public void setProdCat3(String prodCat3) {
        this.prodCat3 = prodCat3;
    }

    public double getQtyAvailable() {
        return qtyAvailable;
    }

    public void setQtyAvailable(double qtyAvailable) {
        this.qtyAvailable = qtyAvailable;
    }

    public double getStockValue() {
        return stockValue;
    }

    public void setStockValue(double stockValue) {
        this.stockValue = stockValue;
    }
}
