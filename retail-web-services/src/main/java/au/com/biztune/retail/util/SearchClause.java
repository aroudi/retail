package au.com.biztune.retail.util;

import java.sql.Timestamp;

/**
 * Created by arash on 19/10/2016.
 */
public class SearchClause {
    private String column;
    private String operator;
    private Object value;
    private Object paramVal;
    private String paramName;

    public SearchClause(String column, String operator, Object value) {
        this.column = column;
        this.operator = operator;
        this.value = value;
    }

    /**
     * build search clause.
     * @param column column name used in search.
     * @param operator operator used in search.
     * @param value parameter value used in search.
     * @param paramName param name used in report.
     */
    public SearchClause(String column, String operator, Object value, String paramName) {
        this.column = column;
        this.operator = operator;
        if (value instanceof Timestamp) {
            this.value = "\'" + value.toString() + "\'";
            this.paramVal = value;
        } else {
            this.value = value;
            this.paramVal = value;
        }
        this.paramName = paramName;
    }
    /**
     * build search clause.
     * @param column column name used in search.
     * @param operator operator used in search.
     * @param value parameter value used in search.
     * @param paramName param name used in report.
     * @param paramVal param value used in report.
     */
    public SearchClause(String column, String operator, Object value, String paramName, String paramVal) {
        this.column = column;
        this.operator = operator;
        if (value instanceof Timestamp) {
            this.value = "\'" + value.toString() + "\'";
        } else {
            this.value = value;
        }
        this.paramName = paramName;
        this.paramVal = paramVal;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getColumn() {
        return column;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public Object getParamVal() {
        return paramVal;
    }

    public void setParamVal(Object paramVal) {
        this.paramVal = paramVal;
    }
}
