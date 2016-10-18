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

import org.jboss.errai.bus.server.annotations.Service;
import org.uberfire.ext.aut.api.LibraryInfo;
import org.uberfire.ext.aut.api.LibraryService;
import org.uberfire.ext.aut.api.OrganizationUnit;
import org.uberfire.ext.aut.api.Project;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.util.Arrays;
import java.util.List;

@Service
@ApplicationScoped
public class LibraryServiceImpl implements LibraryService {


    @PostConstruct
    public void setup() {
        //setupPreferences, default ou, etc.

    }

    @Override
    public LibraryInfo getProjectsInfo() {
        //delete this, just to show loading
        try {
            Thread.sleep( 1000 );
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }

        //TODO take real data
        LibraryInfo libraryInfo = new LibraryInfo(
                new OrganizationUnit( "defaultOU" ),
                getProjects( "defaultOU" ),
                getOrganizationUnits() );
        return libraryInfo;
    }

    @Override
    public List<Project> getProjects( String organizationUnitName ) {
        return Arrays.asList( new Project( "project 1 " + organizationUnitName ),
                              new Project( "project 2 " + organizationUnitName ),
                              new Project( "project 3 " + organizationUnitName ),
                              new Project( "project 4 " + organizationUnitName ),
                              new Project( "project 5 " + organizationUnitName ) );
    }

    public List<OrganizationUnit> getOrganizationUnits() {
        return Arrays.asList( new OrganizationUnit( "defaultOU" ),
                              new OrganizationUnit( "ou2" ) );
    }
}
