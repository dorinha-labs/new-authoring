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

import org.guvnor.common.services.project.model.Project;
import org.guvnor.structure.organizationalunit.OrganizationalUnit;
import org.guvnor.structure.organizationalunit.OrganizationalUnitService;
import org.guvnor.structure.repositories.Repository;
import org.guvnor.structure.repositories.RepositoryService;
import org.jboss.errai.bus.server.annotations.Service;
import org.kie.workbench.common.services.shared.project.KieProjectService;
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
    public LibraryInfo getDefaultLibraryInfo() {

        Collection<OrganizationalUnit> organizationalUnits = service.getOrganizationalUnits();
        OrganizationalUnit defaultOU = getDefaultOU( organizationalUnits );

        LibraryInfo libraryInfo = new LibraryInfo(
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
                getProjects( selectedOU ),
                organizationalUnits );

        return libraryInfo;

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

}
