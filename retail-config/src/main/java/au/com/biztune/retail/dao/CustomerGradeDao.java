package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.CustomerGrade;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by arash on 22/02/2016.
 */
@Mapper
public interface CustomerGradeDao {
    /**
     * get list of all grades.
     * @return list of grades.
     */
    List<CustomerGrade> getAllCustomerGrades();

    /**
     * update customer grade.
     * @param customerGrade customerGrade
     */
    void updateCustomerGrade(CustomerGrade customerGrade);

    /**
     * get customer grade by code.
     * @param gradeCode gradeCode
     * @return customer grade.
     */
    CustomerGrade getCustomerGradeByCode(String gradeCode);
}
