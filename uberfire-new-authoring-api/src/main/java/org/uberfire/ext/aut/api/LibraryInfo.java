package org.uberfire.ext.aut.api;

import java.util.ArrayList;
import java.util.List;

public class LibraryInfo {

    private OrganizationUnit defaultOrganizationUnit;
    private List<Project> projects = new ArrayList<>();
    private List<OrganizationUnit> organizationUnits = new ArrayList<>();

    public LibraryInfo() {
    }

    public LibraryInfo( OrganizationUnit defaultOrganizationUnit,
                        List<Project> projects,
                        List<OrganizationUnit> organizationUnits ) {
        this.defaultOrganizationUnit = defaultOrganizationUnit;
        this.projects = projects;
        this.organizationUnits = organizationUnits;
    }

    public OrganizationUnit getDefaultOrganizationUnit() {
        return defaultOrganizationUnit;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public List<OrganizationUnit> getOrganizationUnits() {
        return organizationUnits;
    }

    public boolean fullLibrary() {
        return hasDefaultOu() && !getProjects().isEmpty();
    }

    public boolean hasDefaultOu() {
        return defaultOrganizationUnit != null;
    }
}
