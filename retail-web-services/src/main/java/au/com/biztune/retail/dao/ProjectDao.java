package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.Project;
import java.util.List;

/**
 * Created by arash on 1/04/2016.
 */
public interface ProjectDao {
    /**
     * get Projedct by code.
     * @param code code
     * @return Project
     */
    Project getProjectByCode(String code);

    /**
     * get Projedct by code and client Id.
     * @param code code
     * @param clientId clientId
     * @return Project
     */
    Project getProjectByCodeAndClientId(String code, long clientId);
    /**
     * get Projedct by name.
     * @param name name
     * @return Project
     */
    Project getProjectByName(String name);
    /**
     * get Projedct by id.
     * @param id id
     * @return Project
     */
    Project getProjectById(double id);

    /**
     * insert project.
     * @param project project
     */
    void insert(Project project);
    /**
     * update project by code.
     * @param project project
     */
    void updatePerId(Project project);
    /**
     * insert project by id.
     * @param project project
     */
    void updatePerCode(Project project);

    /**
     * get all projects.
     * @return project list
     */
    List<Project> getAllProject();

    /**
     * update project verification status. set verified to 0 or 1.
     * @param verified verification flag.
     * @param prodId project id.
     */
    void updateProjectVerificationStatus(boolean verified, long prodId);

    /**
     * get no of boq referenced this project.
     * @param projectId projectId
     * @return No Of Boq referenced this project.
     */
    Long getNoOfBoqReferencedProject(long projectId);

    /**
     * delete project by project id.
     * @param projectId projectId.
     */
    void deleteProjectById(long projectId);
}
