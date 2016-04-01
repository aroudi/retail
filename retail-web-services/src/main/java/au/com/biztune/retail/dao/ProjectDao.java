package au.com.biztune.retail.dao;

import au.com.biztune.retail.domain.Project;

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
}
