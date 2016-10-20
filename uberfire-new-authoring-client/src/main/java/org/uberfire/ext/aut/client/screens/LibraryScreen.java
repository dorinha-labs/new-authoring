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
import org.guvnor.common.services.project.model.Project;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.kie.uberfire.social.activities.model.SocialFileSelectedEvent;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;
import org.uberfire.client.mvp.PlaceManager;
import org.uberfire.client.mvp.UberElement;
import org.uberfire.client.workbench.docks.UberfireDocks;
import org.uberfire.ext.aut.api.LibraryInfo;
import org.uberfire.ext.aut.api.LibraryService;
import org.uberfire.ext.aut.client.util.ProjectsDocks;
import org.uberfire.lifecycle.OnOpen;
import org.uberfire.mvp.Command;
import org.uberfire.mvp.impl.DefaultPlaceRequest;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@WorkbenchScreen( identifier = "LibraryScreen" )
public class LibraryScreen {


    private LibraryInfo libraryInfo;

    public interface View extends UberElement<LibraryScreen> {

        void clearProjects();

        void addOrganizationUnit( String ou );

        void addProject( String project, String projectCreated, Command details, Command select );

        void clearOrganizationUnits();

        void clearFilterText();
    }

    @Inject
    private View view;

    @Inject
    private Caller<LibraryService> projectsService;

    @Inject
    private UberfireDocks uberfireDocks;

    @Inject
    private ProjectsDocks projectsDocks;

    @Inject
    private PlaceManager placeManager;

    @OnOpen
    public void onOpen() {

        loadDefaultLibrary();
    }

    private void loadDefaultLibrary() {
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
        } ).getDefaultLibraryInfo();
    }

    private void loadLibrary( LibraryInfo libraryInfo ) {
        LibraryScreen.this.libraryInfo = libraryInfo;
        setupProjects( libraryInfo.getProjects() );
        setupOus( libraryInfo );
        projectsDocks.refresh();
    }

    private void setupOus( LibraryInfo libraryInfo ) {
        view.clearOrganizationUnits();
        libraryInfo.getOrganizationUnits().forEach( ou -> view.addOrganizationUnit( ou.getIdentifier() ) );
    }

    private void updateLibrary( String ou ) {
        projectsService.call( new RemoteCallback<LibraryInfo>() {
            @Override
            public void callback( LibraryInfo libraryInfo ) {
                LibraryScreen.this.libraryInfo = libraryInfo;
                setupProjects( libraryInfo.getProjects() );
            }
        } ).getLibraryInfo( ou );
    }

    private void setupProjects( Set<Project> projects ) {
        view.clearProjects();

        //TODO project id or project name?
        projects.stream().forEach( p -> view
                .addProject( p.getProjectName(), "01/01/2001", detailsCommand( p.getIdentifier() ),
                             selectCommand( p.getIdentifier() ) ) );
    }

    public void newProject() {
        Map<String, String> param = new HashMap<>();
        param.put( "backPlace", "LibraryScreen" );
        placeManager.goTo( new DefaultPlaceRequest( "NewProjectScreen", param ) );
    }

    //TODO
    @Inject
    private Event<SocialFileSelectedEvent> socialEvent;

    private Command selectCommand( String id ) {
        return () -> {
            //check permissions like DefaultSocialLinkCommandGenerator.java
            placeManager.goTo( "AuthoringPerspective" );
            socialEvent.fire( new SocialFileSelectedEvent( "NEW_PROJECT", id ) );
        };
    }

    private Command detailsCommand( String selectedProject ) {
        return () -> {
            //TODO Open Details Screen
            GWT.log( "details" );
            projectsDocks.handle( selectedProject );
        };
    }

    public void selectOrganizationUnit( String ou ) {
        updateLibrary( ou );
    }

    public void filterProjects( String filter ) {
        if ( libraryInfo != null && libraryInfo.fullLibrary() ) {
            Set<Project> filteredProjects = libraryInfo.getProjects().stream()
                    .filter( p -> p.getProjectName().toUpperCase()
                            .startsWith( filter.toUpperCase() ) )
                    .collect( Collectors.toSet() );
            setupProjects( filteredProjects );
        }
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
