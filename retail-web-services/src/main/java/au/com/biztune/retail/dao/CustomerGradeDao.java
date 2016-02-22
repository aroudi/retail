package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.CustomerGrade;

import java.util.List;

/**
 * Created by arash on 22/02/2016.
 */
public interface CustomerGradeDao {
    /**
     * get list of all grades.
     * @return list of grades.
     */
    List<CustomerGrade> getAllCustomerGrades();

}
