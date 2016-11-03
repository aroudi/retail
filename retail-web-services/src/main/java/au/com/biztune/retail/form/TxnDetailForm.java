package au.com.biztune.retail.form;

import au.com.biztune.retail.domain.ConfigCategory;
import au.com.biztune.retail.domain.ProductSaleItem;
import au.com.biztune.retail.domain.UnitOfMeasure;


/**
 * Created by arash on 15/04/2016.
 */
public class TxnDetailForm {
    private long id;
    private boolean txdePriceOveriden;
    private ProductSaleItem product;
    private UnitOfMeasure unitOfMeasure;
    private double txdeValueLine;
    private double txdeProfitMargin;
    private double txdeValueProfit;
    private double txdeValueGross;
    private double txdeTax;
    private double txdeValueNet;
    private double txdeQuantitySold;
    private double txdeQtyBalance;
    private double txdeQtyInvoiced;
    private double txdePriceSold;
    private boolean txdeLineRefund;
    private boolean txdeItemVoid;
    private ConfigCategory txdeDetailType;
    private long orguId;
    private boolean deleted;
    private double calculatedLineValue;
    private double calculatedLineTax;
    //indicate if this item is going to be invoiced or not
    private boolean invoiced;
    private boolean refund;
    private double txdeQtyTotalInvoiced;
    private double originalQuantity;
    private double txdeQtyRefund;
    private double txdeQtyTotalRefund;
    private ConfigCategory invoiceDetailType;
    private long txdeParentDetail;
    //for invoice detail, store the id of txn_detail
    private long parentId;
    private double txidSurcharge;



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isTxdePriceOveriden() {
        return txdePriceOveriden;
    }

    public void setTxdePriceOveriden(boolean txdePriceOveriden) {
        this.txdePriceOveriden = txdePriceOveriden;
    }

    public ProductSaleItem getProduct() {
        return product;
    }

    public void setProduct(ProductSaleItem product) {
        this.product = product;
    }

    public UnitOfMeasure getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public double getTxdeValueLine() {
        return txdeValueLine;
    }

    public void setTxdeValueLine(double txdeValueLine) {
        this.txdeValueLine = txdeValueLine;
    }

    public double getTxdeProfitMargin() {
        return txdeProfitMargin;
    }

    public void setTxdeProfitMargin(double txdeProfitMargin) {
        this.txdeProfitMargin = txdeProfitMargin;
    }

    public double getTxdeValueProfit() {
        return txdeValueProfit;
    }

    public void setTxdeValueProfit(double txdeValueProfit) {
        this.txdeValueProfit = txdeValueProfit;
    }

    public double getTxdeValueGross() {
        return txdeValueGross;
    }

    public void setTxdeValueGross(double txdeValueGross) {
        this.txdeValueGross = txdeValueGross;
    }

    public double getTxdeTax() {
        return txdeTax;
    }

    public void setTxdeTax(double txdeTax) {
        this.txdeTax = txdeTax;
    }

    public double getTxdeValueNet() {
        return txdeValueNet;
    }

    public void setTxdeValueNet(double txdeValueNet) {
        this.txdeValueNet = txdeValueNet;
    }

    public double getTxdeQuantitySold() {
        return txdeQuantitySold;
    }

    public void setTxdeQuantitySold(double txdeQuantitySold) {
        this.txdeQuantitySold = txdeQuantitySold;
    }

    public double getTxdePriceSold() {
        return txdePriceSold;
    }

    public void setTxdePriceSold(double txdePriceSold) {
        this.txdePriceSold = txdePriceSold;
    }

    public boolean isTxdeLineRefund() {
        return txdeLineRefund;
    }

    public void setTxdeLineRefund(boolean txdeLineRefund) {
        this.txdeLineRefund = txdeLineRefund;
    }

    public boolean isTxdeItemVoid() {
        return txdeItemVoid;
    }

    public void setTxdeItemVoid(boolean txdeItemVoid) {
        this.txdeItemVoid = txdeItemVoid;
    }

    public ConfigCategory getTxdeDetailType() {
        return txdeDetailType;
    }

    public void setTxdeDetailType(ConfigCategory txdeDetailType) {
        this.txdeDetailType = txdeDetailType;
    }

    public long getOrguId() {
        return orguId;
    }

    public void setOrguId(long orguId) {
        this.orguId = orguId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public double getCalculatedLineValue() {
        return calculatedLineValue;
    }

    public void setCalculatedLineValue(double calculatedLineValue) {
        this.calculatedLineValue = calculatedLineValue;
    }

    public double getCalculatedLineTax() {
        return calculatedLineTax;
    }

    public void setCalculatedLineTax(double calculatedLineTax) {
        this.calculatedLineTax = calculatedLineTax;
    }

    public boolean isInvoiced() {
        return invoiced;
    }

    public void setInvoiced(boolean invoiced) {
        this.invoiced = invoiced;
    }

    public double getTxdeQtyBalance() {
        return txdeQtyBalance;
    }

    public void setTxdeQtyBalance(double txdeQtyBalance) {
        this.txdeQtyBalance = txdeQtyBalance;
    }

    public double getTxdeQtyInvoiced() {
        return txdeQtyInvoiced;
    }

    public void setTxdeQtyInvoiced(double txdeQtyInvoiced) {
        this.txdeQtyInvoiced = txdeQtyInvoiced;
    }

    public double getTxdeQtyTotalInvoiced() {
        return txdeQtyTotalInvoiced;
    }

    public void setTxdeQtyTotalInvoiced(double txdeQtyTotalInvoiced) {
        this.txdeQtyTotalInvoiced = txdeQtyTotalInvoiced;
    }

    public double getOriginalQuantity() {
        return originalQuantity;
    }

    public void setOriginalQuantity(double originalQuantity) {
        this.originalQuantity = originalQuantity;
    }

    public double getTxdeQtyRefund() {
        return txdeQtyRefund;
    }

    public void setTxdeQtyRefund(double txdeQtyRefund) {
        this.txdeQtyRefund = txdeQtyRefund;
    }

    public double getTxdeQtyTotalRefund() {
        return txdeQtyTotalRefund;
    }

    public void setTxdeQtyTotalRefund(double txdeQtyTotalRefund) {
        this.txdeQtyTotalRefund = txdeQtyTotalRefund;
    }

    public ConfigCategory getInvoiceDetailType() {
        return invoiceDetailType;
    }

    public void setInvoiceDetailType(ConfigCategory invoiceDetailType) {
        this.invoiceDetailType = invoiceDetailType;
    }

    public boolean isRefund() {
        return refund;
    }

    public void setRefund(boolean refund) {
        this.refund = refund;
    }

    public long getTxdeParentDetail() {
        return txdeParentDetail;
    }

    public void setTxdeParentDetail(long txdeParentDetail) {
        this.txdeParentDetail = txdeParentDetail;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public double getTxidSurcharge() {
        return txidSurcharge;
    }

    public void setTxidSurcharge(double txidSurcharge) {
        this.txidSurcharge = txidSurcharge;
    }
}
