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
import org.uberfire.client.mvp.UberElement;
import org.uberfire.ext.aut.api.Project;
import org.uberfire.ext.aut.api.ProjectsService;
import org.uberfire.ext.aut.client.events.OrganizationUnitChangeEvent;
import org.uberfire.lifecycle.OnOpen;
import org.uberfire.mvp.Command;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.List;

@WorkbenchScreen( identifier = "ProjectsScreen" )
public class ProjectsScreen {

    public interface View extends UberElement<ProjectsScreen> {
        void clearProjects();

        void addOrganizationUnit( String ou );

        void addProject( String project, Command details, Command select );

        void clearOrganizationUnits();
    }

    @Inject
    private View view;

    @Inject
    private Caller<ProjectsService> projectsService;

    @Inject
    private Event<OrganizationUnitChangeEvent> organizationUnitChangeEvent;

    @OnOpen
    public void onOpen() {
        loadOrganizationUnits();
        //TODO
        loadProjects( "default-ou or saved in preferences" );

    }

    private void loadOrganizationUnits() {
        //TODO ederign Error callback
        projectsService.call( new RemoteCallback<List<String>>() {
            @Override
            public void callback( List<String> ous ) {
                view.clearOrganizationUnits();
                ous.forEach( ou -> view.addOrganizationUnit( ou ) );
            }
        } ).getOrganizationUnits();
    }


    public void organizationUnitChange( @Observes OrganizationUnitChangeEvent event ) {
        loadProjects( event.getOrganizationUnit() );
    }

    private void loadProjects( String organizationUnit ) {
        projectsService.call( new RemoteCallback<List<Project>>() {
            @Override
            public void callback( List<Project> projects ) {
                view.clearProjects();
                projects.stream().forEach( p -> view
                        .addProject( p.getNome(), detailsCommand( p.getNome() ), selectCommand( p.getNome() ) ) );
            }
        } ).getProjects( organizationUnit );
    }

    private Command selectCommand( String nome ) {
        return () -> GWT.log( "select command " + nome );
    }

    private Command detailsCommand( String nome ) {
        return () -> GWT.log( "details command " + nome );
    }

    public void selectOrganizationUnit( String ou ) {
        organizationUnitChangeEvent.fire( new OrganizationUnitChangeEvent( ou ) );
    }

    @WorkbenchPartTitle
    public String getTitle() {
        return "ProjectsScreen";
    }

    @WorkbenchPartView
    public UberElement<ProjectsScreen> getView() {
        return view;
    }
}
