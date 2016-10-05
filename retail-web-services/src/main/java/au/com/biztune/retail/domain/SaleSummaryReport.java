package au.com.biztune.retail.domain;

import java.util.List;

/**
 * Created by arash on 5/10/2016.
 */
public class SaleSummaryReport {
    private TotalSaleOperator totalSaleFigures;
    private List<TotalSaleOperator> totalOperatorSaleFigures;

    public TotalSaleOperator getTotalSaleFigures() {
        return totalSaleFigures;
    }

    public void setTotalSaleFigures(TotalSaleOperator totalSaleFigures) {
        this.totalSaleFigures = totalSaleFigures;
    }

    public List<TotalSaleOperator> getTotalOperatorSaleFigures() {
        return totalOperatorSaleFigures;
    }

    public void setTotalOperatorSaleFigures(List<TotalSaleOperator> totalOperatorSaleFigures) {
        this.totalOperatorSaleFigures = totalOperatorSaleFigures;
    }
}
