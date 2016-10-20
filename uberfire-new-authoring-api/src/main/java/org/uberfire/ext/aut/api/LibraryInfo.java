package org.uberfire.ext.aut.api;

import org.guvnor.common.services.project.model.Project;
import org.guvnor.structure.organizationalunit.OrganizationalUnit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class LibraryInfo {

    private OrganizationalUnit defaultOrganizationUnit;
    private Set<Project> projects = new HashSet<>();
    private Collection<OrganizationalUnit> organizationUnits = new ArrayList<>();

    public LibraryInfo() {
    }

    public LibraryInfo( OrganizationalUnit defaultOrganizationUnit,
                        Set<Project> projects,
                        Collection<OrganizationalUnit> organizationUnits ) {
        this.defaultOrganizationUnit = defaultOrganizationUnit;
        this.projects = projects;
        this.organizationUnits = organizationUnits;
    }

    public OrganizationalUnit getDefaultOrganizationUnit() {
        return defaultOrganizationUnit;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public Collection<OrganizationalUnit> getOrganizationUnits() {
        return organizationUnits;
    }

    public boolean fullLibrary() {
        return hasDefaultOu() && !getProjects().isEmpty();
    }

    public boolean hasProjects() {
        return getProjects() != null && !getProjects().isEmpty();
    }

    public boolean hasDefaultOu() {
        return defaultOrganizationUnit != null;
    }
}
