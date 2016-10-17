package au.com.biztune.retail.domain;

import au.com.biztune.retail.util.IdBConstant;

/**
 * Created by arash on 14/04/2016.
 */
public class TxnDetail {
    private long id;
    private long orguId;
    private long storeId;
    private long txhdId;
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
    private double txdeQtyInvoiced;
    private double txdeQtyTotalInvoiced;
    private double txdeQtyBalance;
    private double txdePriceSold;
    private boolean txdeLineRefund;
    private boolean txdeItemVoid;
    private String txdeSerialNumber;
    private boolean txdeWasSplitPck;
    private long txdeDetailLevel;
    private long txdeParentDetail;
    private ConfigCategory txdeDetailType;
    private double txdeDiscPrice;
    private long txdeReturnOrguId;
    private long txdeReturnStoreId;
    private boolean invoiced;
    private double originalQuantity;
    private boolean newAdded;
    private double profitMargin;
    private double txdeQtyRefund;
    private double txdeQtyTotalRefund;
    private ConfigCategory invoiceDetailType;
    private double quantity;
    private boolean selected;
    // for invoice detail, parentId store txdeId of sale transaction.
    private long parentId;




    /**
     * get item cost.
     * @return item cost
     */
    public double getItemCost() {
        if (this.getProduct() == null || this.getProduct().getSellPrice() == null) {
            return 0.00;
        }
        return this.getProduct().getSellPrice().getPrcePrice();
    }

    /**
     * get Item Gross Value after adding profit and before applying tax.
     * @return gross value
     */
    public double getItemGrossValue() {
        return this.getItemCost() + this.getProfitMargin() * this.getItemCost();
    }

    /**
     * get total tax paid for this item.
     * @return total tax paid
     */
    public double getItemTaxPaid () {
        return this.getTotalTaxRate() * this.getItemGrossValue();
    }
    /**
     * calculate total Tax Rate on product.
     * @return tax rate.
     */
    public double getTotalTaxRate() {
        double taxRate = 0.00;
        if (this.product == null || this.product.getProdOrguLink() == null || this.getProduct().getProdOrguLink().getTaxRules() == null) {
            return 0.10;
        }
        for (TaxRule taxRule : this.getProduct().getProdOrguLink().getTaxRules()) {
            if (taxRule == null) {
                continue;
            }
            taxRate = taxRate + taxRule.getTaxLegVariance().getTxlvRate();
        }
        return taxRate;
    }


    /**
     * check if item selected for invoice or refund.
     * @return true if item is selected either for invoice or for refund.
     */
    public boolean isSelected() {
       boolean result = false;
        if (txdeDetailType == null) {
            return false;
        }
       if (txdeDetailType.getCategoryCode().equals(IdBConstant.TXN_LINE_TYPE_SALE)) {
           result = isInvoiced();
       } else if (txdeDetailType.getCategoryCode().equals(IdBConstant.TXN_LINE_TYPE_REFUND)) {
           result = isTxdeLineRefund();
       }
        return result;
    }

    /**
     * get quantity entered by user.
     * @return quantity
     */
    public double getQuantity() {
        double quantity = 0.00;
        if (txdeDetailType == null) {
            return quantity;
        }
        if (txdeDetailType.getCategoryCode().equals(IdBConstant.TXN_LINE_TYPE_SALE)) {
            quantity = txdeQtyInvoiced;
        } else if (txdeDetailType.getCategoryCode().equals(IdBConstant.TXN_LINE_TYPE_REFUND)) {
            quantity = txdeQtyRefund * (-1);
        }
        return quantity;

    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrguId() {
        return orguId;
    }

    public void setOrguId(long orguId) {
        this.orguId = orguId;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public long getTxhdId() {
        return txhdId;
    }

    public void setTxhdId(long txhdId) {
        this.txhdId = txhdId;
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

    public String getTxdeSerialNumber() {
        return txdeSerialNumber;
    }

    public void setTxdeSerialNumber(String txdeSerialNumber) {
        this.txdeSerialNumber = txdeSerialNumber;
    }

    public boolean isTxdeWasSplitPck() {
        return txdeWasSplitPck;
    }

    public void setTxdeWasSplitPck(boolean txdeWasSplitPck) {
        this.txdeWasSplitPck = txdeWasSplitPck;
    }

    public long getTxdeDetailLevel() {
        return txdeDetailLevel;
    }

    public void setTxdeDetailLevel(long txdeDetailLevel) {
        this.txdeDetailLevel = txdeDetailLevel;
    }

    public long getTxdeParentDetail() {
        return txdeParentDetail;
    }

    public void setTxdeParentDetail(long txdeParentDetail) {
        this.txdeParentDetail = txdeParentDetail;
    }

    public ConfigCategory getTxdeDetailType() {
        return txdeDetailType;
    }

    public void setTxdeDetailType(ConfigCategory txdeDetailType) {
        this.txdeDetailType = txdeDetailType;
    }

    public double getTxdeDiscPrice() {
        return txdeDiscPrice;
    }

    public void setTxdeDiscPrice(double txdeDiscPrice) {
        this.txdeDiscPrice = txdeDiscPrice;
    }

    public long getTxdeReturnOrguId() {
        return txdeReturnOrguId;
    }

    public void setTxdeReturnOrguId(long txdeReturnOrguId) {
        this.txdeReturnOrguId = txdeReturnOrguId;
    }

    public long getTxdeReturnStoreId() {
        return txdeReturnStoreId;
    }

    public void setTxdeReturnStoreId(long txdeReturnStoreId) {
        this.txdeReturnStoreId = txdeReturnStoreId;
    }

    public double getTxdeQtyInvoiced() {
        return txdeQtyInvoiced;
    }

    public void setTxdeQtyInvoiced(double txdeQtyInvoiced) {
        this.txdeQtyInvoiced = txdeQtyInvoiced;
    }

    public double getTxdeQtyBalance() {
        return txdeQtyBalance;
    }

    public void setTxdeQtyBalance(double txdeQtyBalance) {
        this.txdeQtyBalance = txdeQtyBalance;
    }

    public double getTxdeQtyTotalInvoiced() {
        return txdeQtyTotalInvoiced;
    }

    public void setTxdeQtyTotalInvoiced(double txdeQtyTotalInvoiced) {
        this.txdeQtyTotalInvoiced = txdeQtyTotalInvoiced;
    }

    public boolean isInvoiced() {
        return invoiced;
    }

    public void setInvoiced(boolean invoiced) {
        this.invoiced = invoiced;
    }

    public double getOriginalQuantity() {
        return originalQuantity;
    }

    public void setOriginalQuantity(double originalQuantity) {
        this.originalQuantity = originalQuantity;
    }

    public boolean isNewAdded() {
        return newAdded;
    }

    public void setNewAdded(boolean newAdded) {
        this.newAdded = newAdded;
    }

    public double getProfitMargin() {
        return profitMargin;
    }

    public void setProfitMargin(double profitMargin) {
        this.profitMargin = profitMargin;
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

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }
}
