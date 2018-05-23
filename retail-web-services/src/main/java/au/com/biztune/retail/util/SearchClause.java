package au.com.biztune.retail.util;

/**
 * Created by arash on 19/10/2016.
 */
public class SearchClause {
    private String column;
    private String operator;
    private Object value;

    public SearchClause(String column, String operator, Object value) {
        this.column = column;
        this.operator = operator;
        this.value = value;
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
}
