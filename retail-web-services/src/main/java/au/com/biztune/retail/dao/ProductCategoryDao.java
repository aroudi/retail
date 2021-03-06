package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.*;

import java.util.List;

/**
 * Created by arash on 26/03/2018.
 */
public interface ProductCategoryDao {
    /**
     * insert department.
     * @param department department
     */
    void insertDepartment (Department department);

    /**
     * update department by id.
     * @param department department
     */
    void updateDepartmentById (Department department);

    /**
     * insert category heading.
     * @param categoryHeading categoryHeading
     */
    void insertCategoryHeading (CategoryHeading categoryHeading);

    /**
     * update category heading by id.
     * @param categoryHeading categoryHeading
     */
    void updateCategoryHeadingById (CategoryHeading categoryHeading);

    /**
     * insert category value.
     * @param categoryValue categoryValue
     */
    void insertCategoryValue (CategoryValue categoryValue);

    /**
     * update category value.
     * @param categoryValue categoryValue
     */
    void updateCategoryValueById (CategoryValue categoryValue);

    /**
     * insert Department Category.
     * @param deptCategory deptCategory
     */
    void insertDeptCategory (DeptCategory deptCategory);

    /**
     * insert Product Department Category.
     * @param prodDeptCat prodDeptCat
     */
    void insertProdDeptCategory (ProdDeptCat prodDeptCat);

    /**
     * insert department category value.
     * @param deptCategoryVal deptCategoryVal
     */
    void insertDeptCategoryValue (DeptCategoryVal deptCategoryVal);
    /**
     * get category tree view list per department id.
     * @param deptId deptId
     * @return List of department category object in tree view
     */
    List<TreeViewNode> getCategoryTreeViewListPerDeptId(long deptId);

    /**
     * get category value tree view list per department_category  id.
     * @param deptCatId deptCatId
     * @return List of department category value object in tree view
     */
    List<TreeViewNode> getDeptCatValTreeViewListPerCatId(long deptCatId);

    /**
     * get all department list in tree view mode.
     * @return List of department in tree view mode.
     */
    List<TreeViewNode> getAllDepartmentListTreeView();

    /**
     * get department object in tree view mode.
     * @param deptId department id.
     * @return Department in tree view mode.
     */
    TreeViewNode getDepartmentTreeViewById(long deptId);
    /**
     * get Category List per department id.
     * @param deptId deptId
     * @return Category Heading List of specific department
     */
    List<CategoryHeading> getCategoryListPerDeptId(long deptId);

    /**
     * get values of specific category heading.
     * @param cathId cathId
     * @return values of heading
     */
    List<CategoryValue> getValueListPerCathId(long cathId);

    /**
     * get department category object by deptCatId.
     * @param deptCatId deptCatId
     * @return DeptCategory
     */
    DeptCategory getDeptCategoryPerId(long deptCatId);
    /**
     * get department category object by deptCatId.
     * @param deptCatValId deptCatValId
     * @return DeptCategoryVal
     */
    DeptCategoryVal getDeptCategoryValPerId(long deptCatValId);
    /**
     * delete Department Category by id.
     * @param id id
     */
    void deleteDeptCategoryById (long id);

    /**
     * delete department category by deptId.
     * @param deptId deptId
     */
    void deleteDeptCategoryByDeptId (long deptId);

    /**
     * delete department category by deptId.
     * @param deptCatValId deptCatValId
     */
    void deleteDeptCategoryValById (long deptCatValId);

    /**
     * delete Product Dept Category by id.
     * @param id id
     */
    void deleteProdDeptCatById (long id);

    /**
     * delete Prod Dept Cat by catId.
     * @param catId catId
     */
    void deleteProdDeptCatByCatId (long catId);

    /**
     * get category list not assigned to a department.
     * @param deptId department id.
     * @return list of category not defined for department.
     */
    List<TreeViewNode> getCategoryListNotDefinedForDepartment(long deptId);

    /**
     * get category heading type not assigned to department.
     * @param deptId  department id.
     * @return list of category not defined for department.
     */
    List<ConfigCategory> getCategoryTypeListNotDefinedForDepartment(long deptId);
    /**
     * get category value list not assigned to a department category.
     * @param deptCatId department category id.
     * @return list of category not defined for department.
     */
    List<TreeViewNode> getCatValListNotDefinedForDeptCat(long deptCatId);

    /**
     * get category heading per cathId.
     * @param cathId cathId
     * @return category heading
     */
    CategoryHeading getCategoryHeadingPerCathId(long cathId);

    /**
     * get category value per catvId.
     * @param catvId catvId
     * @return category value
     */
    CategoryValue getCategoryValPerCatvId(long catvId);

    /**
     * get department by name.
     * @param deptName deptName
     * @return Department
     */
    Department getDepartmentByName(String deptName);

    /**
     * get Category Heading by name.
     * @param catHeading catHeading
     * @return CategoryHeading
     */
    CategoryHeading getCategoryHeadingByName(String catHeading);

    /**
     * get Category Value by name.
     * @param catValue catValue
     * @return CategoryValue
     */
    CategoryValue getCategoryValueByName(String catValue);

    /**
     * get Category Value by name.
     * @param deptCatId deptCatId
     * @param catValue catValue
     * @return CategoryValue
     */
    CategoryValue getCategoryValueByDeptCatIdAndCatValue(long deptCatId, String catValue);

    /**
     * reassigne products to new categoryvalue where catval is changed.
     * @param deptId departmentId
     * @param catId category id
     * @param oldCatValId old category value id
     * @param newCatValId new category value id.
     */
    void changeCatValueOfProdDeptCat(long deptId, long catId, long oldCatValId, long newCatValId);

    /**
     * get department by name.
     * @param deptName deptName
     * @return TreeViewNode
     */
    TreeViewNode getDepartmentTreeViewByName (String deptName);

    /**
     * move products from department category value A to department category value B.
     * @param deptIdA deptIdA
     * @param catIdA catIdA
     * @param catValIdA catValIdA
     * @param deptIdB deptIdB
     * @param catIdB catIdB
     * @param catValIdB catValIdB
     */
    void moveProductsFromDeptCatValA2DeptCatValB(long deptIdA, long catIdA, long catValIdA, long deptIdB, long catIdB, long catValIdB);

    /**
     * move products from department category  A to department category  B.
     * @param deptIdA deptIdA
     * @param catIdA catIdA
     * @param deptIdB deptIdB
     * @param catIdB catIdB
     */
    void moveProductsFromDeptCatA2DeptCatB(long deptIdA, long catIdA, long deptIdB, long catIdB);

    /**
     * delete department category values by department id.
     * @param deptId deptId
     */
    void deleteDeptCategoryValByDeptId(long deptId);

    /**
     * delete department by id.
     * @param deptId deptId
     */
    void deleteDepartmentById(long deptId);

    /**
     * delete product department category by deptId.
     * @param deptIdA deptIdA
     * @param deptIdB deptIdB
     */
    void moveProductsFromDeptA2DeptB(long deptIdA, long deptIdB);

    /**
     * delete product from PROD_DEPT_CAT.
     * @param orguId orguId
     * @param deptId deptId
     * @param catId catId
     * @param catValId catValId
     */
    void deleteProdDeptCatByOrguIdAndDeptIdAndCatIdAndCatValId(long orguId, long deptId, long catId, long catValId);

    /**
     * get category heading in TreeViewMode by cathId.
     * @param cathId category heading id
     * @return CategoryHeading in treeviewmode.
     */
    TreeViewNode getCategoryTreeViewNodePerCathId(long cathId);

    /**
     * get category heading in TreeViewMode by cathId.
     * @param cathId category heading id
     * @return CategoryHeading in treeviewmode.
     */
    TreeViewNode getCategoryHeadingTreeViewNodePerCathId(long cathId);

    /**
     * delete product department category.
     * @param orguId orguId
     * @param deptId deptId
     * @param catId catId
     */
    void deleteProdDeptCatByOrguIdAndDeptIdAndCatId(long orguId, long deptId, long catId);

    /**
     * delete product department category by orguId and prodId.
     * @param orguId orguId
     * @param prodId prodId
     */
    void deleteProdDeptCatByOrguIdAndProdId(long orguId, long prodId);
}
