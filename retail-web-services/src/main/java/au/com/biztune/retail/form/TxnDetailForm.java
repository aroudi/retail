package au.com.biztune.retail.form;

import au.com.biztune.retail.domain.ConfigCategory;
import au.com.biztune.retail.domain.Product;
import au.com.biztune.retail.domain.UnitOfMeasure;

import java.util.List;

/**
 * Created by arash on 15/04/2016.
 */
public class TxnDetailForm {
    private long id;
    private boolean txdePriceOveriden;
    private Product product;
    private UnitOfMeasure unitOfMeasure;
    private double txdeValueLine;
    private double txdeProfitMargin;
    private double txdeValueProfit;
    private double txdeValueGross;
    private double txdeTax;
    private double txdeValueNet;
    private double txdeQuantitySold;
    private double txdePriceSold;
    private boolean txdeLineRefund;
    private boolean txdeItemVoid;
    private ConfigCategory txdeDetailType;
    private List<TxnDetailForm> txnDetailFormList;

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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
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

    public List<TxnDetailForm> getTxnDetailFormList() {
        return txnDetailFormList;
    }

    public void setTxnDetailFormList(List<TxnDetailForm> txnDetailFormList) {
        this.txnDetailFormList = txnDetailFormList;
    }
}
