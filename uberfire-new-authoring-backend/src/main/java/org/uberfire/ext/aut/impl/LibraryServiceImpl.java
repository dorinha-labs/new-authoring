/*
 * Copyright 2015 JBoss, by Red Hat, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.uberfire.ext.aut.impl;

import org.guvnor.common.services.project.model.GAV;
import org.guvnor.common.services.project.model.POM;
import org.guvnor.common.services.project.model.Project;
import org.guvnor.common.services.project.service.DeploymentMode;
import org.guvnor.structure.organizationalunit.OrganizationalUnit;
import org.guvnor.structure.organizationalunit.OrganizationalUnitService;
import org.guvnor.structure.repositories.Repository;
import org.guvnor.structure.repositories.RepositoryEnvironmentConfigurations;
import org.guvnor.structure.repositories.RepositoryService;
import org.jboss.errai.bus.server.annotations.Service;
import org.kie.workbench.common.services.shared.project.KieProject;
import org.kie.workbench.common.services.shared.project.KieProjectService;
import org.uberfire.backend.vfs.Path;
import org.uberfire.ext.aut.api.LibraryInfo;
import org.uberfire.ext.aut.api.LibraryPreferences;
import org.uberfire.ext.aut.api.LibraryService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Service
@ApplicationScoped
public class LibraryServiceImpl implements LibraryService {

    @Inject
    private OrganizationalUnitService ouService;

    @Inject
    private RepositoryService repositoryService;

    @Inject
    private KieProjectService kieProjectService;

    @Inject
    private LibraryPreferences preferences;

    @Override
    public OrganizationalUnit getDefaultOrganizationalUnit() {
        Collection<OrganizationalUnit> organizationalUnits = getOrganizationalUnits();

        LibraryPreferences preferences = getPreferences();

        OrganizationalUnit defaultOU = getOU( preferences.getOuName(), organizationalUnits );
        if ( defaultOU == null ) {
            return createDefaultOU();
        }

        return defaultOU;
    }

    @Override
    public LibraryInfo getDefaultLibraryInfo() {

        OrganizationalUnit defaultOU = getDefaultOrganizationalUnit();

        LibraryInfo libraryInfo = new LibraryInfo(
                defaultOU,
                defaultOU,
                getProjects( defaultOU ),
                getOrganizationalUnits(),
                getPreferences().getOuAlias());

        return libraryInfo;
    }

    @Override
    public LibraryInfo getLibraryInfo( String selectedOuIdentifier ) {
        Collection<OrganizationalUnit> organizationalUnits = getOrganizationalUnits();
        OrganizationalUnit defaultOU = getDefaultOrganizationalUnit();
        OrganizationalUnit selectedOU = getOU( selectedOuIdentifier, organizationalUnits );


        LibraryInfo libraryInfo = new LibraryInfo(
                defaultOU,
                selectedOU,
                getProjects( selectedOU ),
                organizationalUnits,
                getPreferences().getOuAlias());

        return libraryInfo;

    }


    private OrganizationalUnit createDefaultOU() {
        LibraryPreferences preferences = getPreferences();

        return ouService.createOrganizationalUnit( preferences.getOuName(), preferences.getoUOwner(),
                                                   preferences.getoUGroupId() );
    }

    private OrganizationalUnit getOU( String ouIdentifier, Collection<OrganizationalUnit> organizationalUnits ) {
        Optional<OrganizationalUnit> targetOU = organizationalUnits.stream()
                .filter( p -> p.getIdentifier().equalsIgnoreCase( ouIdentifier ) ).findFirst();
        if ( targetOU.isPresent() ) {
            return targetOU.get();
        }
        return null;
    }

    private Collection<OrganizationalUnit> getOrganizationalUnits() {
        return ouService.getOrganizationalUnits();
    }


    @Override
    public KieProject newProject( String projectName, String selectOu ) {
        Collection<OrganizationalUnit> organizationalUnits = getOrganizationalUnits();
        OrganizationalUnit selectedOU = getOU( selectOu, organizationalUnits );
        Repository repository = getDefaultRepository( selectedOU );
        Path repoRoot = repository.getRoot();
        LibraryPreferences preferences = getPreferences();

        GAV gav = new GAV( preferences.getProjectGroupId(), projectName, preferences.getProjectVersion() );

        POM pom = new POM( projectName, preferences.getProjectDescription(), gav );
        DeploymentMode mode = DeploymentMode.VALIDATED;

        KieProject kieProject = kieProjectService.newProject( repoRoot, pom, mode.name() );

        return kieProject;
    }


    private Set<Project> getProjects( OrganizationalUnit ou ) {

        Repository defaultRepository = getDefaultRepository( ou );
        if ( defaultRepository == null ) {
            defaultRepository = createDefaultRepo( ou );
        }
        return kieProjectService.getProjects( defaultRepository, getPreferences().getProjectDefaultBranch() );
    }

    private Repository getDefaultRepository( OrganizationalUnit ou ) {
        String defaultRepositoryName = getDefaultRepositoryName( ou );
        Optional<Repository> repo = ou.getRepositories().stream()
                .filter( r -> r.getAlias().equalsIgnoreCase( defaultRepositoryName ) )
                .findAny();
        if ( !repo.isPresent() ) {
            return createDefaultRepo( ou );
        }
        return repo.get();
    }

    private Repository createDefaultRepo( OrganizationalUnit ou ) {
        LibraryPreferences preferences = getPreferences();
        final String scheme = preferences.getRepositoryDefaultScheme();
        final String alias = getDefaultRepositoryName( ou );
        final RepositoryEnvironmentConfigurations configuration = new RepositoryEnvironmentConfigurations();
        configuration.setManaged( true );

        return repositoryService.createRepository( ou, scheme, alias, configuration );
    }

    private String getDefaultRepositoryName( OrganizationalUnit ou ) {
        return ou.getIdentifier() + "-" + getPreferences().getRepositoryId();
    }

    private LibraryPreferences getPreferences() {
        preferences.load();
        return preferences;
    }

}

