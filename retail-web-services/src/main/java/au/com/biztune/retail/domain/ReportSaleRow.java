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
    private String catalogueNo;
    private String partNo;
    private java.sql.Timestamp expectedDate;
    private java.sql.Timestamp orderDate;
    private String client;
    private String orderNo;
    private String orderedBy;
    private double qtyOnOrder;
    private double qtyBackOrder;
    private double qtyTotal;
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
    private double rrp;
    private double expectedTotalExTax;
    private double expectedProfit;
    private double actualProfit;
    private double rrpGp;
    private double defaultGrade;
    private double aGrade;
    private double bGrade;
    private double cGrade;
    private double dGrade;
    private double defaultGradeGp;
    private double aGradeGp;
    private double bGradeGp;
    private double cGradeGp;
    private double dGradeGp;

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

    public double getRrp() {
        return rrp;
    }

    public void setRrp(double rrp) {
        this.rrp = rrp;
    }

    public double getExpectedTotalExTax() {
        return expectedTotalExTax;
    }

    public void setExpectedTotalExTax(double expectedTotalExTax) {
        this.expectedTotalExTax = expectedTotalExTax;
    }

    public double getExpectedProfit() {
        return expectedProfit;
    }

    public void setExpectedProfit(double expectedProfit) {
        this.expectedProfit = expectedProfit;
    }

    public double getActualProfit() {
        return actualProfit;
    }

    public void setActualProfit(double actualProfit) {
        this.actualProfit = actualProfit;
    }

    public double getRrpGp() {
        return rrpGp;
    }

    public void setRrpGp(double rrpGp) {
        this.rrpGp = rrpGp;
    }

    public double getDefaultGrade() {
        return defaultGrade;
    }

    public void setDefaultGrade(double defaultGrade) {
        this.defaultGrade = defaultGrade;
    }
    public double getaGrade() {
        return aGrade;
    }

    public void setaGrade(double aGrade) {
        this.aGrade = aGrade;
    }

    public double getbGrade() {
        return bGrade;
    }

    public void setbGrade(double bGrade) {
        this.bGrade = bGrade;
    }

    public double getcGrade() {
        return cGrade;
    }

    public void setcGrade(double cGrade) {
        this.cGrade = cGrade;
    }

    public double getdGrade() {
        return dGrade;
    }

    public void setdGrade(double dGrade) {
        this.dGrade = dGrade;
    }

    public double getDefaultGradeGp() {
        return defaultGradeGp;
    }

    public void setDefaultGradeGp(double defaultGradeGp) {
        this.defaultGradeGp = defaultGradeGp;
    }

    public double getaGradeGp() {
        return aGradeGp;
    }

    public void setaGradeGp(double aGradeGp) {
        this.aGradeGp = aGradeGp;
    }

    public double getbGradeGp() {
        return bGradeGp;
    }

    public void setbGradeGp(double bGradeGp) {
        this.bGradeGp = bGradeGp;
    }

    public double getcGradeGp() {
        return cGradeGp;
    }

    public void setcGradeGp(double cGradeGp) {
        this.cGradeGp = cGradeGp;
    }

    public double getdGradeGp() {
        return dGradeGp;
    }

    public void setdGradeGp(double dGradeGp) {
        this.dGradeGp = dGradeGp;
    }

    public String getCatalogueNo() {
        return catalogueNo;
    }

    public void setCatalogueNo(String catalogueNo) {
        this.catalogueNo = catalogueNo;
    }

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    public Timestamp getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(Timestamp expectedDate) {
        this.expectedDate = expectedDate;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderedBy() {
        return orderedBy;
    }

    public void setOrderedBy(String orderedBy) {
        this.orderedBy = orderedBy;
    }

    public double getQtyOnOrder() {
        return qtyOnOrder;
    }

    public void setQtyOnOrder(double qtyOnOrder) {
        this.qtyOnOrder = qtyOnOrder;
    }

    public double getQtyBackOrder() {
        return qtyBackOrder;
    }

    public void setQtyBackOrder(double qtyBackOrder) {
        this.qtyBackOrder = qtyBackOrder;
    }

    public double getQtyTotal() {
        return qtyTotal;
    }

    public void setQtyTotal(double qtyTotal) {
        this.qtyTotal = qtyTotal;
    }
}
