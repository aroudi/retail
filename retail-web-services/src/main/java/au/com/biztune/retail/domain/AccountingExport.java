package au.com.biztune.retail.domain;

import java.sql.Timestamp;

/**
 * Created by arash on 30/11/2016.
 */
public class AccountingExport {
    private long id;
    private long orguId;
    private long storeId;
    private long appUserId;
    private boolean exportSuppliers;
    private boolean exportDeliveryNotes;
    private boolean exportJournalEntries;
    private String exportedContents;
    private Timestamp exportTime;

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

    public long getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(long appUserId) {
        this.appUserId = appUserId;
    }

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

    public boolean isExportJournalEntries() {
        return exportJournalEntries;
    }

    public void setExportJournalEntries(boolean exportJournalEntries) {
        this.exportJournalEntries = exportJournalEntries;
    }

    public String getExportedContents() {
        return exportedContents;
    }

    public void setExportedContents(String exportedContents) {
        this.exportedContents = exportedContents;
    }

    public Timestamp getExportTime() {
        return exportTime;
    }

    public void setExportTime(Timestamp exportTime) {
        this.exportTime = exportTime;
    }
}
