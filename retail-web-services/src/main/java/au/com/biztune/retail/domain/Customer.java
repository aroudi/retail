package au.com.biztune.retail.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by arash on 22/02/2016.
 */
public class Customer {
    private long id;
    private ConfigCategory customerType;
    private ConfigCategory customerStatus;
    private String firstName;
    private String surName;
    private String middleName;
    private String companyName;
    @JsonIgnore
    private Timestamp dateOfBirth;
    private String address;
    private String address2;
    private String email;
    private String mobile;
    private String phone;
    private String webSite;
    private String code;
    private String title;
    private String fax;
    private String comment;
    private String dateOfBirthStr;
    private CustomerGrade grade;
    private ArrayList<Contact> contacts;
    private double creditLimit;
    private double owing;
    private double remainCredit;
    private int creditDuration;
    private Timestamp creditStartDate;
    private boolean creditStartEom;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Timestamp getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Timestamp dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public CustomerGrade getGrade() {
        return grade;
    }

    public void setGrade(CustomerGrade grade) {
        this.grade = grade;
    }

    public String getDateOfBirthStr() {
        return dateOfBirthStr;
    }

    public void setDateOfBirthStr(String dateOfBirthStr) {
        this.dateOfBirthStr = dateOfBirthStr;
    }

    public ConfigCategory getCustomerType() {
        return customerType;
    }

    public void setCustomerType(ConfigCategory customerType) {
        this.customerType = customerType;
    }

    public ConfigCategory getCustomerStatus() {
        return customerStatus;
    }

    public void setCustomerStatus(ConfigCategory customerStatus) {
        this.customerStatus = customerStatus;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public double getOwing() {
        return owing;
    }

    public void setOwing(double owing) {
        this.owing = owing;
    }

    public double getRemainCredit() {
        return remainCredit;
    }

    public void setRemainCredit(double remainCredit) {
        this.remainCredit = remainCredit;
    }

    public int getCreditDuration() {
        return creditDuration;
    }

    public void setCreditDuration(int creditDuration) {
        this.creditDuration = creditDuration;
    }

    public Timestamp getCreditStartDate() {
        return creditStartDate;
    }

    public void setCreditStartDate(Timestamp creditStartDate) {
        this.creditStartDate = creditStartDate;
    }

    public boolean isCreditStartEom() {
        return creditStartEom;
    }

    public void setCreditStartEom(boolean creditStartEom) {
        this.creditStartEom = creditStartEom;
    }
}
