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
import org.guvnor.structure.repositories.RepositoryService;
import org.jboss.errai.bus.server.annotations.Service;
import org.kie.workbench.common.services.shared.project.KieProject;
import org.kie.workbench.common.services.shared.project.KieProjectService;
import org.uberfire.backend.vfs.Path;
import org.uberfire.ext.aut.api.LibraryInfo;
import org.uberfire.ext.aut.api.LibraryService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
@ApplicationScoped
public class LibraryServiceImpl implements LibraryService {

    @Inject
    private OrganizationalUnitService service;

    @Inject
    private RepositoryService repositoryService;

    @Inject
    private KieProjectService kieProjectService;

    @Override
    public OrganizationalUnit getDefaultOrganizationalUnit() {
        Collection<OrganizationalUnit> organizationalUnits = service.getOrganizationalUnits();
        return getDefaultOU( organizationalUnits );
    }

    @Override
    public LibraryInfo getDefaultLibraryInfo() {

        Collection<OrganizationalUnit> organizationalUnits = service.getOrganizationalUnits();
        OrganizationalUnit defaultOU = getDefaultOU( organizationalUnits );

        LibraryInfo libraryInfo = new LibraryInfo(
                defaultOU,
                defaultOU,
                getProjects( defaultOU ),
                organizationalUnits );

        return libraryInfo;
    }

    @Override
    public LibraryInfo getLibraryInfo( String selectedOuIdentifier ) {
        Collection<OrganizationalUnit> organizationalUnits = service.getOrganizationalUnits();
        OrganizationalUnit defaultOU = getDefaultOU( organizationalUnits );
        OrganizationalUnit selectedOU = getOU( selectedOuIdentifier, organizationalUnits );


        LibraryInfo libraryInfo = new LibraryInfo(
                defaultOU,
                selectedOU,
                getProjects( selectedOU ),
                organizationalUnits );

        return libraryInfo;

    }

    @Override
    public KieProject newProject( String projectName, String selectOu ) {
        Collection<OrganizationalUnit> organizationalUnits = service.getOrganizationalUnits();
        OrganizationalUnit selectedOU = getOU( selectOu, organizationalUnits );
        Repository repository = selectedOU.getRepositories().stream().findFirst().get();
        Path repoRoot = repository.getRoot();
        GAV gav = new GAV( "me.ederign", projectName, "1.0.0" );

        POM pom = new POM(projectName, "description", gav);
        DeploymentMode mode = DeploymentMode.VALIDATED;
        ///?
        String baseURL = "http://localhost";
        KieProject kieProject = kieProjectService.newProject( repoRoot, pom, mode.name() );
        System.out.println();

        return kieProject;
//        final String url = GWT.getModuleBaseURL();
//        final String baseUrl = url.replace( GWT.getModuleName() + "/", "" );
//
//        busyIndicatorView.showBusyIndicator( CommonConstants.INSTANCE.Saving() );
//        projectServiceCaller.call( getSuccessCallback(),
//                                   new CommandWithThrowableDrivenErrorCallback( busyIndicatorView,
//                                                                                createErrorsHandler( pom ) ) )
//                .newProject( repoRoot, pom, baseUrl, mode );
//    }
//
//
    }

    private OrganizationalUnit getOU( String ouIdentifier, Collection<OrganizationalUnit> organizationalUnits ) {
        //READ FROM PREFERENCES
        Optional<OrganizationalUnit> defaultOU = organizationalUnits.stream()
                .filter( p -> p.getIdentifier().equalsIgnoreCase( ouIdentifier ) ).findFirst();
        if ( defaultOU.isPresent() ) {
            return defaultOU.get();
        }
        return null;
    }

    private OrganizationalUnit getDefaultOU( Collection<OrganizationalUnit> organizationalUnits ) {
        //READ FROM PREFERENCES
        Optional<OrganizationalUnit> defaultOU = organizationalUnits.stream().findFirst();
        if ( defaultOU.isPresent() ) {
            return defaultOU.get();
        }
        return null;
    }

    private Set<Project> getProjects( OrganizationalUnit ou ) {
        if ( ou != null && ou.getRepositories() != null ) {
            Repository repository = ou.getRepositories().stream().findFirst().get();
            return kieProjectService.getProjects( repository, "master" );
        } else {
            return Collections.emptySet();
        }
    }

    private void createDefaultRepo() {
//        final String scheme = "git";
//        final String alias = "default-" + defaultOU.get().getName();
//        final RepositoryEnvironmentConfigurations configuration = new RepositoryEnvironmentConfigurations();
//        configuration.setManaged( true );
//
//
//        repositoryService.call( new RemoteCallback<Repository>() {
//            @Override
//            public void callback( Repository repository ) {
//                NewProjectScreen.this.repository = repository;
//            }
//        }, new ErrorCallback<Repository>() {
//            @Override
//            public boolean error( Repository repository, Throwable throwable ) {
//                Window.alert( "createRepository" );
//                return false;
//            }
//        } ).createRepository( defaultOU.get(), scheme, alias, configuration );
    }

}

