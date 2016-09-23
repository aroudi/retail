package au.com.biztune.retail.domain;

import java.sql.Timestamp;

/**
 * Created by arash on 21/09/2016.
 */
public class CashSession {
    private long id;
    private long orguId;
    private long storeId;
    private String tillShortDesc;
    private AppUser cssnOperator;
    private Timestamp cssnTradingDate;
    private ConfigCategory cssnStatus;
    private Timestamp cssnStartDate;
    private String cssnImbalanceRsn;
    private String cssnTxnNr;
    private boolean cssnForced;
    private boolean cssnIsOpAcc;
    private String cssnSapCtrlNr;
    private Timestamp cssnExpEndDate;
    private Timestamp cssnActEndDate;
    private ConfigCategory cssnMethodOpen;
    private ConfigCategory cssnMethodClose;
    private ConfigCategory cssnCyclePeriod;
    private double cssnCurrentCash;
    private double cssnTotalFloat;
    private double cssnTotalPickup;

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

    public String getTillShortDesc() {
        return tillShortDesc;
    }

    public void setTillShortDesc(String tillShortDesc) {
        this.tillShortDesc = tillShortDesc;
    }

    public AppUser getCssnOperator() {
        return cssnOperator;
    }

    public void setCssnOperator(AppUser cssnOperator) {
        this.cssnOperator = cssnOperator;
    }

    public Timestamp getCssnTradingDate() {
        return cssnTradingDate;
    }

    public void setCssnTradingDate(Timestamp cssnTradingDate) {
        this.cssnTradingDate = cssnTradingDate;
    }

    public ConfigCategory getCssnStatus() {
        return cssnStatus;
    }

    public void setCssnStatus(ConfigCategory cssnStatus) {
        this.cssnStatus = cssnStatus;
    }

    public Timestamp getCssnStartDate() {
        return cssnStartDate;
    }

    public void setCssnStartDate(Timestamp cssnStartDate) {
        this.cssnStartDate = cssnStartDate;
    }

    public String getCssnImbalanceRsn() {
        return cssnImbalanceRsn;
    }

    public void setCssnImbalanceRsn(String cssnImbalanceRsn) {
        this.cssnImbalanceRsn = cssnImbalanceRsn;
    }

    public String getCssnTxnNr() {
        return cssnTxnNr;
    }

    public void setCssnTxnNr(String cssnTxnNr) {
        this.cssnTxnNr = cssnTxnNr;
    }

    public boolean isCssnForced() {
        return cssnForced;
    }

    public void setCssnForced(boolean cssnForced) {
        this.cssnForced = cssnForced;
    }

    public boolean isCssnIsOpAcc() {
        return cssnIsOpAcc;
    }

    public void setCssnIsOpAcc(boolean cssnIsOpAcc) {
        this.cssnIsOpAcc = cssnIsOpAcc;
    }

    public String getCssnSapCtrlNr() {
        return cssnSapCtrlNr;
    }

    public void setCssnSapCtrlNr(String cssnSapCtrlNr) {
        this.cssnSapCtrlNr = cssnSapCtrlNr;
    }

    public Timestamp getCssnExpEndDate() {
        return cssnExpEndDate;
    }

    public void setCssnExpEndDate(Timestamp cssnExpEndDate) {
        this.cssnExpEndDate = cssnExpEndDate;
    }

    public Timestamp getCssnActEndDate() {
        return cssnActEndDate;
    }

    public void setCssnActEndDate(Timestamp cssnActEndDate) {
        this.cssnActEndDate = cssnActEndDate;
    }

    public ConfigCategory getCssnMethodOpen() {
        return cssnMethodOpen;
    }

    public void setCssnMethodOpen(ConfigCategory cssnMethodOpen) {
        this.cssnMethodOpen = cssnMethodOpen;
    }

    public ConfigCategory getCssnMethodClose() {
        return cssnMethodClose;
    }

    public void setCssnMethodClose(ConfigCategory cssnMethodClose) {
        this.cssnMethodClose = cssnMethodClose;
    }

    public ConfigCategory getCssnCyclePeriod() {
        return cssnCyclePeriod;
    }

    public void setCssnCyclePeriod(ConfigCategory cssnCyclePeriod) {
        this.cssnCyclePeriod = cssnCyclePeriod;
    }

    public double getCssnCurrentCash() {
        return cssnCurrentCash;
    }

    public void setCssnCurrentCash(double cssnCurrentCash) {
        this.cssnCurrentCash = cssnCurrentCash;
    }

    public double getCssnTotalFloat() {
        return cssnTotalFloat;
    }

    public void setCssnTotalFloat(double cssnTotalFloat) {
        this.cssnTotalFloat = cssnTotalFloat;
    }

    public double getCssnTotalPickup() {
        return cssnTotalPickup;
    }

    public void setCssnTotalPickup(double cssnTotalPickup) {
        this.cssnTotalPickup = cssnTotalPickup;
    }
}
