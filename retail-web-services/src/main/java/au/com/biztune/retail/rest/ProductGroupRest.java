package au.com.biztune.retail.rest;

import au.com.biztune.retail.domain.TreeViewNode;
import au.com.biztune.retail.security.Secured;
import au.com.biztune.retail.service.ProductGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
import java.util.List;

/**
 * Created by arash on 28/03/2018.
 */

@Component
@Path("productGroup")
public class ProductGroupRest {
    private final Logger logger = LoggerFactory.getLogger(ProductGroupRest.class);
    @Context
    private UriInfo uriInfo;
    @Context
    private Request request;

    @Autowired
    private ProductGroupService productGroupService;

    /**
     * Returns list of department in tree view format.
     * @return list of department
     */
    @Secured
    @Path("/department/all/treeViewModel")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TreeViewNode> getAllDepartmentTreeViewFormat() {
        try {
            return productGroupService.getAllDepartmentListAsTreeView();
        } catch (Exception e) {
            logger.error ("Error in retrieving department List(tree view format) :", e);
            return null;
        }
    }

    /**
     * crate a product group node (department/category/value).
     * @param treeViewNode treeViewNode
     * @return CommonResponse
     */
    @Secured
    @Path("/add/treeViewNode")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TreeViewNode addProductGroupTreeViewMode (TreeViewNode treeViewNode) {
        return productGroupService.insertTreeViewNode(treeViewNode);
    }

    /**
     * update a product group node (department/category/value).
     * @param treeViewNode treeViewNode
     * @return CommonResponse
     */
    @Secured
    @Path("/update/treeViewNode")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TreeViewNode updateProductGroupTreeViewMode (TreeViewNode treeViewNode) {
        return productGroupService.updateTreeViewNode(treeViewNode);
    }

    /**
     * Returns list of category not assigned to a department in tree view format.
     * @param treeViewNode treeViewNode for department
     * @return list of category
     */
    @Secured
    @Path("/department/getCategoryNotAssignedToDept")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<TreeViewNode> getCategoryNotAssignedToDept(TreeViewNode treeViewNode) {
        try {
            return productGroupService.getCategoryListNotAssignedToDepartment(treeViewNode);
        } catch (Exception e) {
            logger.error ("Error in retrieving category List(tree view format) :", e);
            return null;
        }
    }
    /**
     * Returns list of category value not assigned to a department category in tree view format.
     * @param treeViewNode treeViewNode for department category
     * @return list of category value
     */
    @Secured
    @Path("/department/getCategoryValNotAssignedToDeptCat")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<TreeViewNode> getCategoryValNotAssignedToDeptCat(TreeViewNode treeViewNode) {
        try {
            return productGroupService.getCatValListNotDefinedForDeptCat(treeViewNode);
        } catch (Exception e) {
            logger.error ("Error in retrieving category value List(tree view format) :", e);
            return null;
        }
    }
}
