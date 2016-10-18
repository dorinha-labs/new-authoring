/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
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

package org.uberfire.ext.aut.client.screens;

import com.google.gwt.core.client.GWT;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;
import org.uberfire.client.mvp.PlaceManager;
import org.uberfire.client.mvp.UberElement;
import org.uberfire.client.workbench.docks.UberfireDocks;
import org.uberfire.ext.aut.api.LibraryInfo;
import org.uberfire.ext.aut.api.LibraryService;
import org.uberfire.ext.aut.api.Project;
import org.uberfire.ext.aut.client.events.OrganizationUnitChangeEvent;
import org.uberfire.ext.aut.client.util.ProjectsDocks;
import org.uberfire.lifecycle.OnOpen;
import org.uberfire.mvp.Command;
import org.uberfire.mvp.impl.DefaultPlaceRequest;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WorkbenchScreen( identifier = "LibraryScreen" )
public class LibraryScreen {

    public interface View extends UberElement<LibraryScreen> {
        void clearProjects();

        void addOrganizationUnit( String ou );

        void addProject( String project, Command details, Command select );

        void clearOrganizationUnits();
    }

    @Inject
    private View view;

    @Inject
    private Caller<LibraryService> projectsService;

    @Inject
    private Event<OrganizationUnitChangeEvent> organizationUnitChangeEvent;

    @Inject
    private UberfireDocks uberfireDocks;

    @Inject
    private ProjectsDocks projectsDocks;

    @Inject
    private PlaceManager placeManager;

    @OnOpen
    public void onOpen() {

        //TODO put loading here
        projectsService.call( new RemoteCallback<LibraryInfo>() {
            @Override
            public void callback( LibraryInfo libraryInfo ) {
                if ( libraryInfo.fullLibrary() ) {
                    loadLibrary( libraryInfo );
                } else if ( !libraryInfo.hasDefaultOu() ) {
                    GWT.log( "GOTO ADM SCREEN WITH A POPUP TELLING THAT NEEDS A OU" );
                } else {
                    GWT.log( "GOTO NEW PROJECT SCREEN" );
                }
            }
        } ).getProjectsInfo();
    }

    public void newProject() {
        Map<String, String> param = new HashMap<>();
        param.put( "backPlace", "LibraryScreen" );
        placeManager.goTo( new DefaultPlaceRequest( "NewProjectScreen", param ) );
    }

    private void loadLibrary( LibraryInfo libraryInfo ) {
        setupProjects( libraryInfo.getProjects() );
        setupOus( libraryInfo );
        projectsDocks.refresh();
    }

    private void setupOus( LibraryInfo libraryInfo ) {
        view.clearOrganizationUnits();
        //TODO setup default ou
        libraryInfo.getOrganizationUnits().forEach( ou -> view.addOrganizationUnit( ou.getNome() ) );
    }

    public void organizationUnitChange( @Observes OrganizationUnitChangeEvent event ) {
        reloadProjects( event.getOrganizationUnit() );
    }

    private void reloadProjects( String organizationUnit ) {
        projectsService.call( new RemoteCallback<List<Project>>() {
            @Override
            public void callback( List<Project> projects ) {
                setupProjects( projects );
            }
        } ).getProjects( organizationUnit );
    }

    private void setupProjects( List<Project> projects ) {
        view.clearProjects();
        projects.stream().forEach( p -> view
                .addProject( p.getNome(), detailsCommand( p.getNome() ), selectCommand( p.getNome() ) ) );
    }

    private Command selectCommand( String nome ) {
        return () -> {
            GWT.log( "select" );
        };
    }

    private Command detailsCommand( String selectedProject ) {
        String currentProject = null;
        return () -> {
            //TODO Open Details Screen
            GWT.log( "details" );
//            projectsDocks.handle(selectedProject);
        };
    }

    public void selectOrganizationUnit( String ou ) {
        organizationUnitChangeEvent.fire( new OrganizationUnitChangeEvent( ou ) );
    }

    @WorkbenchPartTitle
    public String getTitle() {
        return "LibraryScreen";
    }

    @WorkbenchPartView
    public UberElement<LibraryScreen> getView() {
        return view;
    }
}
