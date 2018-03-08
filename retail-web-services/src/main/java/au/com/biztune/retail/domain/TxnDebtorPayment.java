package au.com.biztune.retail.domain;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by arash on 6/03/2018.
 */
public class TxnDebtorPayment {
    private long id;
    private String txhdTxnNr;
    private String txhdOrigTxnNr;
    private Timestamp txhdTradingDate;
    private double txhdValueGross;
    private double txhdValueNett;
    private double txhdValueDue;
    private double txhdValueChange;
    private double txhdValRounding;
    private double txhdValueTaxable;
    private double txhdValueTax;
    private AppUser user;
    private Customer customer;
    private List<TxnAccPayment> lines;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Timestamp getTxhdTradingDate() {
        return txhdTradingDate;
    }

    public void setTxhdTradingDate(Timestamp txhdTradingDate) {
        this.txhdTradingDate = txhdTradingDate;
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

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<TxnAccPayment> getLines() {
        return lines;
    }

    public void setLines(List<TxnAccPayment> lines) {
        this.lines = lines;
    }
}
