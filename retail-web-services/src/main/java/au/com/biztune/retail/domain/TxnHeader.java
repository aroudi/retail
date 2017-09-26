package au.com.biztune.retail.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
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
    //dummy field use in reports
    private double txhdAmountPaid;
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
    private String txhdEmailTo;
    private String txhdContactPerson;
    private String txhdContactPhone;
    private AppUser user;
    private ConfigCategory invoiceTxnType;
    private boolean txhdPrinted;
    private CustomerAccountDebt customerAccountDebt;
    private String txhdPoNo;
    //only for invoices to see it had been fully refunded or not
    private boolean txivFullyRefunded;
    //ordering status : OUTSTANDING, ON ORDER, PARTIAL RECEIVED, RECIEVED AND FINALISED
    private ConfigCategory status;
    private String txhdPrjCode;
    private boolean txivImported;
    private Timestamp txivImportTime;

    /**
     * add txn detail to txn header.
     * @param txnDetail txnDetail
     */
    public void addTxnDetail(TxnDetail txnDetail) {
        if (txnDetails == null) {
            txnDetails = new ArrayList<TxnDetail>();
        }
        txnDetails.add(txnDetail);
    }

    /**
     * add txn media to txn header.
     * @param txnMedia txnMedia
     */
    public void addTxnMedia(TxnMedia txnMedia) {
        if (txnMedias == null) {
            txnMedias = new ArrayList<TxnMedia>();
        }
        txnMedias.add(txnMedia);
    }

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

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public ConfigCategory getInvoiceTxnType() {
        return invoiceTxnType;
    }

    public void setInvoiceTxnType(ConfigCategory invoiceTxnType) {
        this.invoiceTxnType = invoiceTxnType;
    }

    public String getTxhdContactPerson() {
        return txhdContactPerson;
    }

    public void setTxhdContactPerson(String txhdContactPerson) {
        this.txhdContactPerson = txhdContactPerson;
    }

    public String getTxhdContactPhone() {
        return txhdContactPhone;
    }

    public void setTxhdContactPhone(String txhdContactPhone) {
        this.txhdContactPhone = txhdContactPhone;
    }

    public boolean isTxhdPrinted() {
        return txhdPrinted;
    }

    public void setTxhdPrinted(boolean txhdPrinted) {
        this.txhdPrinted = txhdPrinted;
    }

    public CustomerAccountDebt getCustomerAccountDebt() {
        return customerAccountDebt;
    }

    public void setCustomerAccountDebt(CustomerAccountDebt customerAccountDebt) {
        this.customerAccountDebt = customerAccountDebt;
    }

    public String getTxhdPoNo() {
        return txhdPoNo;
    }

    public void setTxhdPoNo(String txhdPoNo) {
        this.txhdPoNo = txhdPoNo;
    }

    public String getTxhdEmailTo() {
        return txhdEmailTo;
    }

    public void setTxhdEmailTo(String txhdEmailTo) {
        this.txhdEmailTo = txhdEmailTo;
    }

    public boolean isTxivFullyRefunded() {
        return txivFullyRefunded;
    }

    public void setTxivFullyRefunded(boolean txivFullyRefunded) {
        this.txivFullyRefunded = txivFullyRefunded;
    }

    public ConfigCategory getStatus() {
        return status;
    }

    public void setStatus(ConfigCategory status) {
        this.status = status;
    }

    public String getTxhdPrjCode() {
        return txhdPrjCode;
    }

    public void setTxhdPrjCode(String txhdPrjCode) {
        this.txhdPrjCode = txhdPrjCode;
    }

    public double getTxhdAmountPaid() {
        return txhdAmountPaid;
    }

    public void setTxhdAmountPaid(double txhdAmountPaid) {
        this.txhdAmountPaid = txhdAmountPaid;
    }

    public boolean isTxivImported() {
        return txivImported;
    }

    public void setTxivImported(boolean txivImported) {
        this.txivImported = txivImported;
    }

    public Timestamp getTxivImportTime() {
        return txivImportTime;
    }

    public void setTxivImportTime(Timestamp txivImportTime) {
        this.txivImportTime = txivImportTime;
    }
}
