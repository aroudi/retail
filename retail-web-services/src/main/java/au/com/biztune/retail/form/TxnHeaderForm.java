package au.com.biztune.retail.form;

import au.com.biztune.retail.domain.*;

import java.util.List;

/**
 * Created by arash on 15/04/2016.
 */
public class TxnHeaderForm {
    private long id;
    private String txhdTxnNr;
    private String txhdOrigTxnNr;
    private double txhdValueGross;
    private double txhdValueNett;
    private double txhdValueDue;
    private double txhdValueChange;
    private double txhdValRounding;
    private double txhdValueTax;
    private Customer customer;
    private List<TxnDetailForm> txnDetailFormList;
    private List<TxnMediaForm> txnMediaFormList;
    private boolean txhdVoided;
    private Store store;
    private ConfigCategory txhdTxnType;
    private ConfigCategory txhdState;
    private boolean convertedToTxnSale;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getTxhdValueGross() {
        return txhdValueGross;
    }

    public void setTxhdValueGross(double txhdValueGross) {
        this.txhdValueGross = txhdValueGross;
    }

    public double getTxhdValueNett() {
        return txhdValueNett;
    }

    public void setTxhdValueNett(double txhdValueNett) {
        this.txhdValueNett = txhdValueNett;
    }

    public double getTxhdValueDue() {
        return txhdValueDue;
    }

    public void setTxhdValueDue(double txhdValueDue) {
        this.txhdValueDue = txhdValueDue;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<TxnDetailForm> getTxnDetailFormList() {
        return txnDetailFormList;
    }

    public void setTxnDetailFormList(List<TxnDetailForm> txnDetailFormList) {
        this.txnDetailFormList = txnDetailFormList;
    }

    public List<TxnMediaForm> getTxnMediaFormList() {
        return txnMediaFormList;
    }

    public void setTxnMediaFormList(List<TxnMediaForm> txnMediaFormList) {
        this.txnMediaFormList = txnMediaFormList;
    }

    public double getTxhdValueChange() {
        return txhdValueChange;
    }

    public void setTxhdValueChange(double txhdValueChange) {
        this.txhdValueChange = txhdValueChange;
    }

    public double getTxhdValRounding() {
        return txhdValRounding;
    }

    public void setTxhdValRounding(double txhdValRounding) {
        this.txhdValRounding = txhdValRounding;
    }

    public double getTxhdValueTax() {
        return txhdValueTax;
    }

    public void setTxhdValueTax(double txhdValueTax) {
        this.txhdValueTax = txhdValueTax;
    }

    public boolean isTxhdVoided() {
        return txhdVoided;
    }

    public void setTxhdVoided(boolean txhdVoided) {
        this.txhdVoided = txhdVoided;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public String getTxhdTxnNr() {
        return txhdTxnNr;
    }

    public void setTxhdTxnNr(String txhdTxnNr) {
        this.txhdTxnNr = txhdTxnNr;
    }

    public String getTxhdOrigTxnNr() {
        return txhdOrigTxnNr;
    }

    public void setTxhdOrigTxnNr(String txhdOrigTxnNr) {
        this.txhdOrigTxnNr = txhdOrigTxnNr;
    }

    public ConfigCategory getTxhdTxnType() {
        return txhdTxnType;
    }

    public void setTxhdTxnType(ConfigCategory txhdTxnType) {
        this.txhdTxnType = txhdTxnType;
    }

    public ConfigCategory getTxhdState() {
        return txhdState;
    }

    public void setTxhdState(ConfigCategory txhdState) {
        this.txhdState = txhdState;
    }

    public boolean isConvertedToTxnSale() {
        return convertedToTxnSale;
    }

    public void setConvertedToTxnSale(boolean convertedToTxnSale) {
        this.convertedToTxnSale = convertedToTxnSale;
    }
}
