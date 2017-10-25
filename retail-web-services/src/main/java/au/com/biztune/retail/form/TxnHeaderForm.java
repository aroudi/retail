package au.com.biztune.retail.form;

import au.com.biztune.retail.domain.*;

import java.sql.Timestamp;
import java.util.ArrayList;
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
    private boolean convertedToInvoice;
    private double txhdValueCredit;
    private String txhdDlvAddress;
    private ConfigCategory invoiceTxnType;
    private long parentId;
    private String txhdContactPerson;
    private String txhdContactPhone;
    private boolean temporarySaved;
    private String txhdPoNo;
    private String txhdEmailTo;
    private ConfigCategory status;
    private String txhdPrjCode;
    private Timestamp txhdTradingDate;
    private boolean imported;


    /**
     * add txndetail to detail list.
     * @param txnDetailForm txnDetailForm
     */
    public void addTxnDetailForm(TxnDetailForm txnDetailForm) {
        if (txnDetailFormList == null) {
            txnDetailFormList = new ArrayList<TxnDetailForm>();
        }
        txnDetailFormList.add(txnDetailForm);
    }

    /**
     * add TxnMediaForm to payment list.
     * @param txnMediaForm txnMediaForm
     */
    public void addTxnMediaForm(TxnMediaForm txnMediaForm) {
        if (txnMediaFormList == null) {
            txnMediaFormList = new ArrayList<TxnMediaForm>();
        }
        txnMediaFormList.add(txnMediaForm);
    }
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

    public double getTxhdValueCredit() {
        return txhdValueCredit;
    }

    public void setTxhdValueCredit(double txhdValueCredit) {
        this.txhdValueCredit = txhdValueCredit;
    }

    public String getTxhdDlvAddress() {
        return txhdDlvAddress;
    }

    public void setTxhdDlvAddress(String txhdDlvAddress) {
        this.txhdDlvAddress = txhdDlvAddress;
    }

    public ConfigCategory getInvoiceTxnType() {
        return invoiceTxnType;
    }

    public void setInvoiceTxnType(ConfigCategory invoiceTxnType) {
        this.invoiceTxnType = invoiceTxnType;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public boolean isConvertedToInvoice() {
        return convertedToInvoice;
    }

    public void setConvertedToInvoice(boolean convertedToInvoice) {
        this.convertedToInvoice = convertedToInvoice;
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

    public boolean isTemporarySaved() {
        return temporarySaved;
    }

    public void setTemporarySaved(boolean temporarySaved) {
        this.temporarySaved = temporarySaved;
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

    public Timestamp getTxhdTradingDate() {
        return txhdTradingDate;
    }

    public void setTxhdTradingDate(Timestamp txhdTradingDate) {
        this.txhdTradingDate = txhdTradingDate;
    }

    public boolean isImported() {
        return imported;
    }

    public void setImported(boolean imported) {
        this.imported = imported;
    }
}
