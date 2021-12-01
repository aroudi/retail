package au.com.biztune.retail.form;

import au.com.biztune.retail.domain.CashSession;

import java.util.List;

/**
 * Created by arash on 5/12/2016.
 */
public class AccountingExportForm {
    private boolean exportSuppliers;
    private boolean exportDeliveryNotes;
    private boolean exportAccJournal;
    private int noOfSuppliers;
    private int noOfDeliveryNotes;
    private List<CashSession> cashSessionList;

    public boolean isExportSuppliers() {
        return exportSuppliers;
    }

    public void setExportSuppliers(boolean exportSuppliers) {
        this.exportSuppliers = exportSuppliers;
    }

    public boolean isExportDeliveryNotes() {
        return exportDeliveryNotes;
    }

    public void setExportDeliveryNotes(boolean exportDeliveryNotes) {
        this.exportDeliveryNotes = exportDeliveryNotes;
    }

    public boolean isExportAccJournal() {
        return exportAccJournal;
    }

    public void setExportAccJournal(boolean exportAccJournal) {
        this.exportAccJournal = exportAccJournal;
    }

    public int getNoOfSuppliers() {
        return noOfSuppliers;
    }

    public void setNoOfSuppliers(int noOfSuppliers) {
        this.noOfSuppliers = noOfSuppliers;
    }

    public int getNoOfDeliveryNotes() {
        return noOfDeliveryNotes;
    }

    public void setNoOfDeliveryNotes(int noOfDeliveryNotes) {
        this.noOfDeliveryNotes = noOfDeliveryNotes;
    }

    public List<CashSession> getCashSessionList() {
        return cashSessionList;
    }

    public void setCashSessionList(List<CashSession> cashSessionList) {
        this.cashSessionList = cashSessionList;
    }
}
