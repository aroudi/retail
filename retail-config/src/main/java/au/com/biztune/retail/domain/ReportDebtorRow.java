package au.com.biztune.retail.domain;

import java.sql.Timestamp;

/**
 * created by arash roudi.
 */
public class ReportDebtorRow {
    private String customer;
    private Timestamp tradingDate;
    private String invoiceNo;
    private int age;
    private double totalDebt;
    private double paid;
    private double owing;
    private Timestamp dueDate;
    private String phone;
    private String fax;
    private String email;
    private double creditLimit;
    private String term;

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Timestamp getTradingDate() {
        return tradingDate;
    }

    public void setTradingDate(Timestamp tradingDate) {
        this.tradingDate = tradingDate;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getTotalDebt() {
        return totalDebt;
    }

    public void setTotalDebt(double totalDebt) {
        this.totalDebt = totalDebt;
    }

    public double getPaid() {
        return paid;
    }

    public void setPaid(double paid) {
        this.paid = paid;
    }

    public double getOwing() {
        return owing;
    }

    public void setOwing(double owing) {
        this.owing = owing;
    }

    public Timestamp getDueDate() {
        return dueDate;
    }

    public void setDueDate(Timestamp dueDate) {
        this.dueDate = dueDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }
}
