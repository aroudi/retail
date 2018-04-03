package au.com.biztune.retail.service;

import au.com.biztune.retail.dao.ProductCategoryDao;
import au.com.biztune.retail.domain.*;
import au.com.biztune.retail.util.IdBConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by arash on 28/03/2018.
 */

@Component
public class ProductGroupServiceImpl implements ProductGroupService {

    private final Logger logger = LoggerFactory.getLogger(PaymentMediaServiceImpl.class);
    @Autowired
    private ProductCategoryDao productCategoryDao;

    /**
     * get list of department in tree view mode.
     * @return List of Department as TreeViewNode object.
     */
    public List<TreeViewNode> getAllDepartmentListAsTreeView() {
        try {
            return productCategoryDao.getAllDepartmentListTreeView();
        } catch (Exception e) {
            logger.error("Error in retrieving department data as tree view object", e);
            return null;
        }
    }

    /**
     * insert tree view node.
     * @param treeViewNode treeViewNode
     * @return saved treeViewNode.
     */
    @Transactional
    public TreeViewNode insertTreeViewNode(TreeViewNode treeViewNode) {
        try {
            if (treeViewNode == null) {
                return null;
            }
            switch (treeViewNode.getNodeType()) {
                case IdBConstant.PRGP_LEVEL_1 :
                    return insertDepartment(treeViewNode);
                case IdBConstant.PRGP_LEVEL_2 :
                    return insertCategoryHeading(treeViewNode);
                case IdBConstant.PRGP_LEVEL_3 :
                    return insertCategoryValue(treeViewNode);
                default:
                    return null;
            }
        } catch (Exception e) {
            logger.error("Error in saving tree view object", e);
            return null;
        }
    }

    /**
     * update tree view node.
     * @param treeViewNode treeViewNode
     * @return saved treeViewNode.
     */
    @Transactional
    public TreeViewNode updateTreeViewNode(TreeViewNode treeViewNode) {
        try {
            if (treeViewNode == null) {
                return null;
            }
            switch (treeViewNode.getNodeType()) {
                case IdBConstant.PRGP_LEVEL_1 :
                    return updateDepartment(treeViewNode);
                case IdBConstant.PRGP_LEVEL_2 :
                    return updateCategoryHeading(treeViewNode);
                case IdBConstant.PRGP_LEVEL_3 :
                    return updateCategoryValue(treeViewNode);
                default:
                    return null;
            }
        } catch (Exception e) {
            logger.error("Error in updating tree view object", e);
            return null;
        }
    }
    /**
     * insert new department node.
     * @param treeViewNode department treeViewNode
     * @return saved tree view node.
     */
    private TreeViewNode insertDepartment(TreeViewNode treeViewNode) {
        try {
            final Department department = new Department();
            department.setDeptName(treeViewNode.getName());
            productCategoryDao.insertDepartment(department);
            treeViewNode.setId(department.getId());
            treeViewNode.setNodeType(IdBConstant.PRGP_LEVEL_1);
            return treeViewNode;
        } catch (Exception e) {
            logger.error("Error in saving treeViewNode department", e);
            return null;
        }
    }

    /**
     * insert new categoryHeading node.
     * @param treeViewNode category heading treeViewNode
     * @return saved tree view node.
     */
    private TreeViewNode insertCategoryHeading(TreeViewNode treeViewNode) {
        try {
            CategoryHeading categoryHeading = null;
            categoryHeading = productCategoryDao.getCategoryHeadingPerCathId(treeViewNode.getId());
            if (categoryHeading == null) {
                categoryHeading = new CategoryHeading();
                categoryHeading.setCathName(treeViewNode.getName());
                productCategoryDao.insertCategoryHeading(categoryHeading);
            }
            final DeptCategory deptCategory = new DeptCategory();
            deptCategory.setCathId(categoryHeading.getId());
            deptCategory.setDeptId(treeViewNode.getParentNodeId());
            productCategoryDao.insertDeptCategory(deptCategory);
            treeViewNode.setId(categoryHeading.getId());
            treeViewNode.setPrimaryKey(deptCategory.getId());
            treeViewNode.setNodeType(IdBConstant.PRGP_LEVEL_2);
            return treeViewNode;
        } catch (Exception e) {
            logger.error("Error in saving treeViewNode department", e);
            return null;
        }
    }

    /**
     * insert new categoryValue node.
     * @param treeViewNode category value treeViewNode
     * @return saved tree view node.
     */
    private TreeViewNode insertCategoryValue(TreeViewNode treeViewNode) {
        try {
            CategoryValue categoryValue = null;

            final DeptCategory deptCategory = productCategoryDao.getDeptCategoryPerId(treeViewNode.getParentNodeId());
            if (deptCategory == null) {
                logger.warn("not able to find the deptartment category with id [{}]", treeViewNode.getParentNodeId());
                return null;
            }

            categoryValue = productCategoryDao.getCategoryValPerCatvId(treeViewNode.getId());
            if (categoryValue == null) {
                categoryValue = new CategoryValue();
                categoryValue.setCathId(deptCategory.getCathId());
                categoryValue.setCatValue(treeViewNode.getName());
                productCategoryDao.insertCategoryValue(categoryValue);
            }
            final DeptCategoryVal deptCategoryVal = new DeptCategoryVal();
            deptCategoryVal.setCathId(categoryValue.getCathId());
            deptCategoryVal.setCatvId(categoryValue.getId());
            deptCategoryVal.setDeptCatId(deptCategory.getId());
            deptCategoryVal.setDeptId(deptCategory.getDeptId());
            productCategoryDao.insertDeptCategoryValue(deptCategoryVal);
            treeViewNode.setId(categoryValue.getId());
            treeViewNode.setPrimaryKey(deptCategoryVal.getId());
            treeViewNode.setNodeType(IdBConstant.PRGP_LEVEL_3);
            return treeViewNode;
        } catch (Exception e) {
            logger.error("Error in saving treeViewNode department", e);
            return null;
        }
    }

    private TreeViewNode updateDepartment(TreeViewNode treeViewNode) {
        try {
            final Department department = new Department();
            department.setId(treeViewNode.getId());
            department.setDeptName(treeViewNode.getName());
            productCategoryDao.updateDepartmentById(department);
            return treeViewNode;
        } catch (Exception e) {
            logger.error("Error in updating treeViewNode Department", e);
            return null;
        }
    }

    private TreeViewNode updateCategoryHeading(TreeViewNode treeViewNode) {
        try {
            final CategoryHeading categoryHeading = new CategoryHeading();
            categoryHeading.setId(treeViewNode.getId());
            categoryHeading.setCathName(treeViewNode.getName());
            productCategoryDao.updateCategoryHeadingById(categoryHeading);
            return treeViewNode;
        } catch (Exception e) {
            logger.error("Error in updating treeViewNode Category Heading", e);
            return null;
        }
    }

    private TreeViewNode updateCategoryValue(TreeViewNode treeViewNode) {
        try {
            final CategoryValue categoryValue = new CategoryValue();
            categoryValue.setId(treeViewNode.getId());
            categoryValue.setCatValue(treeViewNode.getName());
            productCategoryDao.updateCategoryValueById(categoryValue);
            return treeViewNode;
        } catch (Exception e) {
            logger.error("Error in updating treeViewNode Category Value", e);
            return null;
        }
    }

    /**
     * get category list not defined for specific department.
     * @param treeViewNode node passed for department.
     * @return list of category heading in TreeViewNode
     */
    public List<TreeViewNode> getCategoryListNotAssignedToDepartment(TreeViewNode treeViewNode) {
        try {
            if (treeViewNode == null) {
                return null;
            }
            return productCategoryDao.getCategoryListNotDefinedForDepartment(treeViewNode.getId());
        } catch (Exception e) {
            logger.error("Error in getting Category list", e);
            return null;
        }
    }
    /**
     * get category list not defined for specific department.
     * @param treeViewNode DepartmentCategory Node.
     * @return list of category heading in TreeViewNode
     */
    public List<TreeViewNode> getCatValListNotDefinedForDeptCat(TreeViewNode treeViewNode) {
        try {
            if (treeViewNode == null) {
                return null;
            }
            return productCategoryDao.getCatValListNotDefinedForDeptCat(treeViewNode.getPrimaryKey());
        } catch (Exception e) {
            logger.error("Error in getting Category list", e);
            return null;
        }
    }
}
