package au.com.biztune.retail.service;

import au.com.biztune.retail.dao.ProductCategoryDao;
import au.com.biztune.retail.domain.*;
import au.com.biztune.retail.response.CommonResponse;
import au.com.biztune.retail.session.SessionState;
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
    @Autowired
    private SessionState sessionState;

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
            //check if deparment exist
            Department department = productCategoryDao.getDepartmentByName(treeViewNode.getName());
            if (department != null) {
                final TreeViewNode response = new TreeViewNode();
                treeViewNode.setId(-1);
                treeViewNode.setName("Department is already defined");
                return treeViewNode;
            }
            department = new Department();
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
                categoryHeading = productCategoryDao.getCategoryHeadingByName(treeViewNode.getName());
                if (categoryHeading != null) {
                    final TreeViewNode response = new TreeViewNode();
                    treeViewNode.setId(-1);
                    treeViewNode.setName("Category Heading is already defined");
                    return treeViewNode;
                }
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
            TreeViewNode response = null;
            final DeptCategory deptCategory = productCategoryDao.getDeptCategoryPerId(treeViewNode.getParentNodeId());
            if (deptCategory == null) {
                logger.warn("not able to find the deptartment category with id [{}]", treeViewNode.getParentNodeId());
                return null;
            }

            categoryValue = productCategoryDao.getCategoryValPerCatvId(treeViewNode.getId());
            if (categoryValue == null) {
                categoryValue = productCategoryDao.getCategoryValueByName(treeViewNode.getName());
                if (categoryValue != null) {
                    response = new TreeViewNode();
                    response.setId(-1);
                    response.setName("Category Value is already defined");
                    return response;
                }
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
            response.setId(categoryValue.getId());
            response.setPrimaryKey(deptCategoryVal.getId());
            response.setParentNodeId(deptCategory.getId());
            response.setNodeType(IdBConstant.PRGP_LEVEL_3);
            return response;
        } catch (Exception e) {
            logger.error("Error in saving treeViewNode department", e);
            return null;
        }
    }

    private TreeViewNode updateDepartment(TreeViewNode treeViewNode) {
        try {
            TreeViewNode response = null;
            //check if deparment exist
            Department department = productCategoryDao.getDepartmentByName(treeViewNode.getName());
            if (department != null) {
                response = new TreeViewNode();
                response.setId(-1);
                response.setName("Department is already defined");
                return response;
            }
            department = new Department();
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
            TreeViewNode response = null;
            CategoryHeading categoryHeading = productCategoryDao.getCategoryHeadingByName(treeViewNode.getName());
            if (categoryHeading != null) {
                response = new TreeViewNode();
                response.setId(-1);
                response.setName("Category Heading is already defined");
                return response;
            }
            categoryHeading = new CategoryHeading();
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
            CategoryValue categoryValue = productCategoryDao.getCategoryValueByDeptCatIdAndCatValue(treeViewNode.getParentNodeId(), treeViewNode.getName());
            final TreeViewNode response;
            if (categoryValue != null) {
                treeViewNode.setId(-1);
                treeViewNode.setName("Category Value is already defined for this Department Category");
                return treeViewNode;
            }
            //check if update is only for current category heading
            if (treeViewNode.getOrder() < 0) {
                //check if category value is exists for other department categories
                categoryValue = productCategoryDao.getCategoryValueByName(treeViewNode.getName());
                if (categoryValue != null) {
                    treeViewNode.setId(categoryValue.getId());
                } else {
                    treeViewNode.setId(-1);
                }
                //re-assign products to new category value
                final DeptCategoryVal oldDeptCatVal = productCategoryDao.getDeptCategoryValPerId(treeViewNode.getPrimaryKey());
                response = insertCategoryValue(treeViewNode);
                if (oldDeptCatVal != null && response != null && response.getId() != oldDeptCatVal.getCatvId()) {
                    productCategoryDao.changeCatValueOfProdDeptCat(oldDeptCatVal.getDeptId(), oldDeptCatVal.getCathId(), oldDeptCatVal.getCatvId(), response.getId());
                }
                //delete current department category value
                productCategoryDao.deleteDeptCategoryValById(treeViewNode.getPrimaryKey());
                return response;
            }
            categoryValue = new CategoryValue();
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

    /**
     * delete product group.
     * @param treeViewNode treeViewNode
     * @return Common Response
     */
    public CommonResponse deleteProductGroup(TreeViewNode treeViewNode) {
        CommonResponse commonResponse = new CommonResponse();
        try {
            if (treeViewNode == null) {
                commonResponse.setStatus(IdBConstant.RESULT_FAILURE);
                commonResponse.setMessage("Product Group node is empty");
                return commonResponse;
            }
            switch (treeViewNode.getNodeType()) {
                case IdBConstant.PRGP_LEVEL_1 :
                    commonResponse = deleteDepartment(treeViewNode);
                    break;
                case IdBConstant.PRGP_LEVEL_2 :
                    commonResponse = deleteCategoryHeading(treeViewNode);
                    break;
                case IdBConstant.PRGP_LEVEL_3 :
                    commonResponse = deleteCategoryValue(treeViewNode);
                    break;
                default:
                    commonResponse.setStatus(IdBConstant.RESULT_FAILURE);
                    commonResponse.setMessage("unrecognised product gorup");
                    break;
            }
            return commonResponse;

        } catch (Exception e) {
            logger.error("Exception in deleting product group", e);
            commonResponse.setStatus(IdBConstant.RESULT_FAILURE);
            commonResponse.setMessage(e.getMessage());
            return commonResponse;
        }
    }

    private CommonResponse deleteDepartment(TreeViewNode treeViewNode) {
        final CommonResponse commonResponse = new CommonResponse();
        try {

            //check if it is 'DEFAULT' department
            if (treeViewNode.getName().toLowerCase().equals(IdBConstant.DEFAULT_DEPARTMENT.toLowerCase())) {
                commonResponse.setStatus(IdBConstant.RESULT_FAILURE);
                commonResponse.setMessage("Can not delete DEFAULT department");
                return commonResponse;
            }
            //for other department, first we need to transfer the categories to 'Default' department and then delete the selected one
            final TreeViewNode toBeDeletedDepartment = productCategoryDao.getDepartmentTreeViewById(treeViewNode.getId());
            final TreeViewNode defaultDepartment = productCategoryDao.getDepartmentTreeViewByName(IdBConstant.DEFAULT_DEPARTMENT);
            if (toBeDeletedDepartment == null) {
                commonResponse.setStatus(IdBConstant.RESULT_FAILURE);
                commonResponse.setMessage("not able to retrieve selected department");
                return commonResponse;
            }
            if (defaultDepartment == null) {
                commonResponse.setStatus(IdBConstant.RESULT_FAILURE);
                commonResponse.setMessage("not able to retrieve Default department");
                return commonResponse;
            }
            //iterate through the toBeDeleted department categories and try to move them under Default department.
            if (toBeDeletedDepartment.getChildren() != null && toBeDeletedDepartment.getChildren().size() > 0) {
                for (TreeViewNode currenCatHeadingNode : toBeDeletedDepartment.getChildren()) {
                    if (currenCatHeadingNode == null) {
                        continue;
                    }
                    //search category heading in Default department
                    TreeViewNode defaultCatHeadingNode = searchNodeInTreeViewNodeList(currenCatHeadingNode, defaultDepartment.getChildren());
                    //category exusts under default department
                    if (defaultCatHeadingNode != null) {
                        //check if category values of this node exist also
                        if (currenCatHeadingNode.getChildren() != null && currenCatHeadingNode.getChildren().size() > 0) {
                            //category heading already exists under default
                            for (TreeViewNode currentCatValueNode : currenCatHeadingNode.getChildren()) {
                                //search category value under category heading
                                TreeViewNode defaultCatValueNode = searchNodeInTreeViewNodeList(currentCatValueNode, defaultCatHeadingNode.getChildren());
                                if (defaultCatValueNode == null) {
                                    //it does not exist under default. move it under default
                                    defaultCatValueNode  = new TreeViewNode();
                                    defaultCatValueNode.setId(currentCatValueNode.getId());
                                    defaultCatValueNode.setName(currentCatValueNode.getName());
                                    defaultCatValueNode.setParentNodeId(defaultCatHeadingNode.getPrimaryKey());
                                    defaultCatValueNode.setPrimaryKey(-1);
                                    defaultCatValueNode = insertCategoryValue(defaultCatValueNode);
                                }
                                //category value already exists under default department and default category heading
                                //only move the products from toBeDeleted department to departments
                                final DeptCategoryVal currentCategoryValue = productCategoryDao.getDeptCategoryValPerId(currentCatValueNode.getId());
                                final DeptCategoryVal defaultCategoryValue = productCategoryDao.getDeptCategoryValPerId(defaultCatValueNode.getId());
                                if (currentCategoryValue != null && defaultCategoryValue != null) {
                                    productCategoryDao.moveProductsFromDeptCatValA2DeptCatValB(currentCategoryValue.getDeptId(), currentCategoryValue.getCathId(), currentCategoryValue.getCatvId()
                                            , defaultCategoryValue.getDeptId(), defaultCategoryValue.getCathId(), defaultCategoryValue.getCatvId());
                                }
                            }
                        }
                    } else {
                        //category heading does not exists under default department
                        //move it under default department
                        defaultCatHeadingNode = new TreeViewNode();
                        defaultCatHeadingNode.setId(currenCatHeadingNode.getId());
                        defaultCatHeadingNode.setParentNodeId(defaultDepartment.getId());
                        defaultCatHeadingNode.setName(currenCatHeadingNode.getName());
                        defaultCatHeadingNode.setPrimaryKey(-1);
                        defaultCatHeadingNode = insertCategoryHeading(defaultCatHeadingNode);
                        if (defaultCatHeadingNode != null) {
                            //move category values
                            if (currenCatHeadingNode.getChildren() != null && currenCatHeadingNode.getChildren().size() > 0) {
                                for (TreeViewNode currentCatValueNode : currenCatHeadingNode.getChildren()) {
                                    //it does not exist under default. move it under default
                                    TreeViewNode defaultCatValueNode  = new TreeViewNode();
                                    defaultCatValueNode.setId(currentCatValueNode.getId());
                                    defaultCatValueNode.setName(currentCatValueNode.getName());
                                    defaultCatValueNode.setParentNodeId(defaultCatHeadingNode.getPrimaryKey());
                                    defaultCatValueNode.setPrimaryKey(-1);
                                    defaultCatValueNode = insertCategoryValue(defaultCatValueNode);
                                }
                            }
                            //move products
                            productCategoryDao.moveProductsFromDeptCatA2DeptCatB(toBeDeletedDepartment.getId(), currenCatHeadingNode.getId()
                                    , defaultDepartment.getId(), defaultCatHeadingNode.getId());
                        }
                    }

                }
            }
            //delete department category values
            productCategoryDao.deleteDeptCategoryValByDeptId(toBeDeletedDepartment.getId());
            //delete department categories
            productCategoryDao.deleteDeptCategoryByDeptId(toBeDeletedDepartment.getId());
            //move products from deleted department to default department
            productCategoryDao.moveProductsFromDeptA2DeptB(toBeDeletedDepartment.getId(), defaultDepartment.getId());
            //delete department
            productCategoryDao.deleteDepartmentById(toBeDeletedDepartment.getId());
            commonResponse.setStatus(IdBConstant.RESULT_SUCCESS);
            return commonResponse;

        } catch (Exception e) {
            logger.error("Exception in deleting department", e);
            commonResponse.setStatus(IdBConstant.RESULT_FAILURE);
            commonResponse.setMessage(e.getMessage());
            return commonResponse;
        }
    }
    private CommonResponse deleteCategoryHeading(TreeViewNode treeViewNode) {
        final CommonResponse commonResponse = new CommonResponse();
        try {
            final TreeViewNode catHeadingTreeViewNode = productCategoryDao.getCategoryHeadingTreeViewNodePerCathId(treeViewNode.getId());
            if (catHeadingTreeViewNode == null) {
                commonResponse.setStatus(IdBConstant.RESULT_FAILURE);
                commonResponse.setMessage("not able to find category heading in db");
                return commonResponse;
            }
            //delete category values for this heading
            if (catHeadingTreeViewNode.getChildren() != null && catHeadingTreeViewNode.getChildren().size() > 0) {
                for (TreeViewNode catValTreeViewNode : catHeadingTreeViewNode.getChildren()) {
                    if (catValTreeViewNode == null) {
                        continue;
                    }
                    deleteCategoryValue(catValTreeViewNode);
                }
            }
            final DeptCategory deptCategory = productCategoryDao.getDeptCategoryPerId(treeViewNode.getPrimaryKey());
            if (deptCategory != null) {
                productCategoryDao.deleteProdDeptCatByOrguIdAndDeptIdAndCatId(sessionState.getOrgUnit().getId(), deptCategory.getDeptId()
                        , deptCategory.getCathId());
                productCategoryDao.deleteDeptCategoryById(deptCategory.getId());
            }
            commonResponse.setStatus(IdBConstant.RESULT_SUCCESS);
            return commonResponse;
        } catch (Exception e) {
            logger.error("Exception in deleting category heading", e);
            commonResponse.setStatus(IdBConstant.RESULT_FAILURE);
            commonResponse.setMessage(e.getMessage());
            return commonResponse;
        }
    }
    private CommonResponse deleteCategoryValue(TreeViewNode treeViewNode) {
        final CommonResponse commonResponse = new CommonResponse();
        try {
            final DeptCategoryVal deptCategoryVal = productCategoryDao.getDeptCategoryValPerId(treeViewNode.getPrimaryKey());
            if (deptCategoryVal == null) {
                commonResponse.setStatus(IdBConstant.RESULT_FAILURE);
                commonResponse.setMessage("not able to find category value in db");
                return commonResponse;
            }
            //remove products from this category value
            productCategoryDao.deleteProdDeptCatByOrguIdAndDeptIdAndCatIdAndCatValId(sessionState.getOrgUnit().getId()
                    , deptCategoryVal.getDeptId(), deptCategoryVal.getCathId(), deptCategoryVal.getCatvId());
            //remove this category value
            productCategoryDao.deleteDeptCategoryValById(deptCategoryVal.getId());
            commonResponse.setStatus(IdBConstant.RESULT_SUCCESS);
            return commonResponse;
        } catch (Exception e) {
            logger.error("Exception in deleting category value", e);
            commonResponse.setStatus(IdBConstant.RESULT_FAILURE);
            commonResponse.setMessage(e.getMessage());
            return commonResponse;
        }
    }
    private TreeViewNode searchNodeInTreeViewNodeList(TreeViewNode treeViewNode, List<TreeViewNode> treeViewNodeList) {
        try {
            if (treeViewNode == null || treeViewNodeList == null || treeViewNodeList.size() < 1) {
                return null;
            }
            for (TreeViewNode node : treeViewNodeList) {
                if (node.getId() == treeViewNode.getId()) {
                    return node;
                }
            }
            return null;
        } catch (Exception e) {
            logger.error("Exception in searching node in treeViewNode list", e);
            return null;
        }
    }
}
