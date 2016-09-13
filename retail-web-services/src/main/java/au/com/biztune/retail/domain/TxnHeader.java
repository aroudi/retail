package au.com.biztune.retail.domain;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by arash on 14/04/2016.
 */
public class TxnHeader {
    private long id;
    private OrgUnit orgUnit;
    private Store store;
    private String txhdTxnNr;
    private ConfigCategory txhdState;
    private OrgUnit orgUnitOriginal;
    private String txhdOrigTxnNr;
    private Timestamp txhdTradingDate;
    private ConfigCategory txhdTxnType;
    private long txhdOperator;
    private boolean txhdVoided;
    private double txhdValueGross;
    private double txhdValueNett;
    private double txhdValueDue;
    private double txhdValueChange;
    private double txhdValRounding;
    private double txhdValueTaxable;
    private double txhdValueTax;
    private String txhdReceiptId;
    private Customer customer;
    private int txhdVoidTxnNr;
    private boolean txhdTaxExempt;
    private Timestamp txhdRefundExpiry;
    private Timestamp txhdCollectDate;
    private List<TxnDetail> txnDetails;
    private List<TxnMedia> txnMedias;
    //this is used for invoice only. it contains parent sale order id
    private long parentId;
    private String txhdDlvAddress;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public OrgUnit getOrgUnit() {
        return orgUnit;
    }

    public void setOrgUnit(OrgUnit orgUnit) {
        this.orgUnit = orgUnit;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public ConfigCategory getTxhdState() {
        return txhdState;
    }

    public void setTxhdState(ConfigCategory txhdState) {
        this.txhdState = txhdState;
    }

    public OrgUnit getOrgUnitOriginal() {
        return orgUnitOriginal;
    }

    public void setOrgUnitOriginal(OrgUnit orgUnitOriginal) {
        this.orgUnitOriginal = orgUnitOriginal;
    }

    public Timestamp getTxhdTradingDate() {
        return txhdTradingDate;
    }

    public void setTxhdTradingDate(Timestamp txhdTradingDate) {
        this.txhdTradingDate = txhdTradingDate;
    }

    public ConfigCategory getTxhdTxnType() {
        return txhdTxnType;
    }

    public void setTxhdTxnType(ConfigCategory txhdTxnType) {
        this.txhdTxnType = txhdTxnType;
    }

    public long getTxhdOperator() {
        return txhdOperator;
    }

    public void setTxhdOperator(long txhdOperator) {
        this.txhdOperator = txhdOperator;
    }

    public boolean isTxhdVoided() {
        return txhdVoided;
    }

    public void setTxhdVoided(boolean txhdVoided) {
        this.txhdVoided = txhdVoided;
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

    public double getTxhdValueTaxable() {
        return txhdValueTaxable;
    }

    public void setTxhdValueTaxable(double txhdValueTaxable) {
        this.txhdValueTaxable = txhdValueTaxable;
    }

    public double getTxhdValueTax() {
        return txhdValueTax;
    }

    public void setTxhdValueTax(double txhdValueTax) {
        this.txhdValueTax = txhdValueTax;
    }

    public String getTxhdReceiptId() {
        return txhdReceiptId;
    }

    public void setTxhdReceiptId(String txhdReceiptId) {
        this.txhdReceiptId = txhdReceiptId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getTxhdVoidTxnNr() {
        return txhdVoidTxnNr;
    }

    public void setTxhdVoidTxnNr(int txhdVoidTxnNr) {
        this.txhdVoidTxnNr = txhdVoidTxnNr;
    }

    public boolean isTxhdTaxExempt() {
        return txhdTaxExempt;
    }

    public void setTxhdTaxExempt(boolean txhdTaxExempt) {
        this.txhdTaxExempt = txhdTaxExempt;
    }

    public Timestamp getTxhdRefundExpiry() {
        return txhdRefundExpiry;
    }

    public void setTxhdRefundExpiry(Timestamp txhdRefundExpiry) {
        this.txhdRefundExpiry = txhdRefundExpiry;
    }

    public Timestamp getTxhdCollectDate() {
        return txhdCollectDate;
    }

    public void setTxhdCollectDate(Timestamp txhdCollectDate) {
        this.txhdCollectDate = txhdCollectDate;
    }

    public List<TxnDetail> getTxnDetails() {
        return txnDetails;
    }

    public void setTxnDetails(List<TxnDetail> txnDetails) {
        this.txnDetails = txnDetails;
    }

    public List<TxnMedia> getTxnMedias() {
        return txnMedias;
    }

    public void setTxnMedias(List<TxnMedia> txnMedias) {
        this.txnMedias = txnMedias;
    }

    public String getTxhdTxnNr() {
        return txhdTxnNr;
    }

    public void setTxhdTxnNr(String txhdTxnNr) {
        this.txhdTxnNr = txhdTxnNr;
    }

    public void setTxhdOrigTxnNr(String txhdOrigTxnNr) {
        this.txhdOrigTxnNr = txhdOrigTxnNr;
    }

    public String getTxhdOrigTxnNr() {
        return txhdOrigTxnNr;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public String getTxhdDlvAddress() {
        return txhdDlvAddress;
    }

    public void setTxhdDlvAddress(String txhdDlvAddress) {
        this.txhdDlvAddress = txhdDlvAddress;
    }
}
