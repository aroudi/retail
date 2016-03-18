package au.com.biztune.retail.domain;

import java.sql.Timestamp;

/**
 * Created by arash on 18/03/2016.
 */
public class TaxRule {
    private long id;
    private Timestamp txrlDateFrom;
    private Timestamp txrlDateTo;
    private int txrlApplySeq;
    private boolean txrlCumTax;
    private double txrlUpperVal;
    private TaxRuleName taxRuleName;
    private TaxLegVariance taxLegVariance;
    private String txrlCode;
    private String txrlDesc;
    private long txrlCalcMthd;
    private String txrlLegalDesc;
    private long txrlRoundingMthd;
    private boolean txrlStepped;
    private String txrlAccCode;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Timestamp getTxrlDateFrom() {
        return txrlDateFrom;
    }

    public void setTxrlDateFrom(Timestamp txrlDateFrom) {
        this.txrlDateFrom = txrlDateFrom;
    }

    public Timestamp getTxrlDateTo() {
        return txrlDateTo;
    }

    public void setTxrlDateTo(Timestamp txrlDateTo) {
        this.txrlDateTo = txrlDateTo;
    }

    public int getTxrlApplySeq() {
        return txrlApplySeq;
    }

    public void setTxrlApplySeq(int txrlApplySeq) {
        this.txrlApplySeq = txrlApplySeq;
    }

    public boolean isTxrlCumTax() {
        return txrlCumTax;
    }

    public void setTxrlCumTax(boolean txrlCumTax) {
        this.txrlCumTax = txrlCumTax;
    }

    public double getTxrlUpperVal() {
        return txrlUpperVal;
    }

    public void setTxrlUpperVal(double txrlUpperVal) {
        this.txrlUpperVal = txrlUpperVal;
    }

    public TaxRuleName getTaxRuleName() {
        return taxRuleName;
    }

    public void setTaxRuleName(TaxRuleName taxRuleName) {
        this.taxRuleName = taxRuleName;
    }

    public String getTxrlCode() {
        return txrlCode;
    }

    public void setTxrlCode(String txrlCode) {
        this.txrlCode = txrlCode;
    }

    public String getTxrlDesc() {
        return txrlDesc;
    }

    public void setTxrlDesc(String txrlDesc) {
        this.txrlDesc = txrlDesc;
    }

    public long getTxrlCalcMthd() {
        return txrlCalcMthd;
    }

    public void setTxrlCalcMthd(long txrlCalcMthd) {
        this.txrlCalcMthd = txrlCalcMthd;
    }

    public String getTxrlLegalDesc() {
        return txrlLegalDesc;
    }

    public void setTxrlLegalDesc(String txrlLegalDesc) {
        this.txrlLegalDesc = txrlLegalDesc;
    }

    public long getTxrlRoundingMthd() {
        return txrlRoundingMthd;
    }

    public void setTxrlRoundingMthd(long txrlRoundingMthd) {
        this.txrlRoundingMthd = txrlRoundingMthd;
    }

    public boolean isTxrlStepped() {
        return txrlStepped;
    }

    public void setTxrlStepped(boolean txrlStepped) {
        this.txrlStepped = txrlStepped;
    }

    public String getTxrlAccCode() {
        return txrlAccCode;
    }

    public void setTxrlAccCode(String txrlAccCode) {
        this.txrlAccCode = txrlAccCode;
    }

    public TaxLegVariance getTaxLegVariance() {
        return taxLegVariance;
    }

    public void setTaxLegVariance(TaxLegVariance taxLegVariance) {
        this.taxLegVariance = taxLegVariance;
    }
}
