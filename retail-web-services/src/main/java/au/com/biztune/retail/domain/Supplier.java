package au.com.biztune.retail.domain;

import java.sql.Timestamp;

/**
 * Created by akhoshraft on 29/02/2016.
 */
public class Supplier {
    private long id;
    private String supplierCode;
    private String supplierName;
    private ConfigCategory supplierType;
    private ConfigCategory supplierStatus;
    private Contact contact;
    private int leadTime;
    private int stockCover;
    private double minOrderValue;
    private double creditLimit;
    private int maxAdvOrder;
    private Timestamp createDate;
    private Timestamp lastModifiedDate;
    private long lastModifiedBy;
    private String contactFirstName;
    private String contactSurName;
    private String contactTitle;
    private String contactKnownAs;
    private SuppOrguLink suppOrguLink;
    private boolean deliveryFreightFree;
    private boolean deleted;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public ConfigCategory getSupplierType() {
        return supplierType;
    }

    public void setSupplierType(ConfigCategory supplierType) {
        this.supplierType = supplierType;
    }

    public ConfigCategory getSupplierStatus() {
        return supplierStatus;
    }

    public void setSupplierStatus(ConfigCategory supplierStatus) {
        this.supplierStatus = supplierStatus;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public int getLeadTime() {
        return leadTime;
    }

    public void setLeadTime(int leadTime) {
        this.leadTime = leadTime;
    }

    public int getStockCover() {
        return stockCover;
    }

    public void setStockCover(int stockCover) {
        this.stockCover = stockCover;
    }

    public double getMinOrderValue() {
        return minOrderValue;
    }

    public void setMinOrderValue(double minOrderValue) {
        this.minOrderValue = minOrderValue;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public int getMaxAdvOrder() {
        return maxAdvOrder;
    }

    public void setMaxAdvOrder(int maxAdvOrder) {
        this.maxAdvOrder = maxAdvOrder;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Timestamp lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public long getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getContactFirstName() {
        return contactFirstName;
    }

    public void setContactFirstName(String contactFirstName) {
        this.contactFirstName = contactFirstName;
    }

    public String getContactSurName() {
        return contactSurName;
    }

    public void setContactSurName(String contactSurName) {
        this.contactSurName = contactSurName;
    }

    public String getContactTitle() {
        return contactTitle;
    }

    public void setContactTitle(String contactTitle) {
        this.contactTitle = contactTitle;
    }

    public String getContactKnownAs() {
        return contactKnownAs;
    }

    public void setContactKnownAs(String contactKnownAs) {
        this.contactKnownAs = contactKnownAs;
    }

    public SuppOrguLink getSuppOrguLink() {
        return suppOrguLink;
    }

    public void setSuppOrguLink(SuppOrguLink suppOrguLink) {
        this.suppOrguLink = suppOrguLink;
    }

    public boolean isDeliveryFreightFree() {
        return deliveryFreightFree;
    }

    public void setDeliveryFreightFree(boolean deliveryFreightFree) {
        this.deliveryFreightFree = deliveryFreightFree;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
