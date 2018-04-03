package au.com.biztune.retail.service;

import au.com.biztune.retail.domain.TreeViewNode;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by arash on 28/03/2018.
 */
public interface ProductGroupService {
    /**
     * insert tree view node.
     * @param treeViewNode treeViewNode
     * @return saved treeViewNode.
     */
    @Transactional
    TreeViewNode insertTreeViewNode(TreeViewNode treeViewNode);

    /**
     * update tree view node.
     * @param treeViewNode treeViewNode
     * @return saved treeViewNode.
     */
    @Transactional
    TreeViewNode updateTreeViewNode(TreeViewNode treeViewNode);

    /**
     * get list of department in tree view mode.
     * @return List of Department as TreeViewNode object.
     */
    List<TreeViewNode> getAllDepartmentListAsTreeView();

    /**
     * get category list not defined for specific department.
     * @param treeViewNode node passed for department.
     * @return list of category heading in TreeViewNode
     */
    List<TreeViewNode> getCategoryListNotAssignedToDepartment(TreeViewNode treeViewNode);

    /**
     * get category list not defined for specific department.
     * @param treeViewNode DepartmentCategory Node.
     * @return list of category heading in TreeViewNode
     */
    List<TreeViewNode> getCatValListNotDefinedForDeptCat(TreeViewNode treeViewNode);
}
