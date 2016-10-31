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

import org.guvnor.structure.organizationalunit.OrganizationalUnit;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.ErrorCallback;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.kie.uberfire.social.activities.model.SocialFileSelectedEvent;
import org.kie.workbench.common.services.shared.project.KieProject;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;
import org.uberfire.client.mvp.PlaceManager;
import org.uberfire.client.mvp.UberElement;
import org.uberfire.ext.aut.api.LibraryService;
import org.uberfire.ext.aut.client.events.NewProjectErrorEvent;
import org.uberfire.ext.widgets.common.client.breadcrumbs.UberfireBreadcrumbs;
import org.uberfire.ext.widgets.common.client.common.BusyIndicatorView;
import org.uberfire.lifecycle.OnStartup;
import org.uberfire.mvp.PlaceRequest;
import org.uberfire.mvp.impl.DefaultPlaceRequest;
import org.uberfire.workbench.events.NotificationEvent;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.inject.Inject;

@WorkbenchScreen( identifier = "NewProjectScreen" )
public class NewProjectScreen {

    @Inject
    private PlaceManager placeManager;

    @Inject
    private Caller<LibraryService> libraryService;

    @Inject
    private BusyIndicatorView busyIndicatorView;

    @Inject
    private Event<NotificationEvent> notificationEvent;

    @Inject
    private Event<NewProjectErrorEvent> errorEvent;

    @Inject
    private UberfireBreadcrumbs breadcrumbs;

    @Inject
    private View view;

    //TODO
    @Inject
    private Event<SocialFileSelectedEvent> socialEvent;

    private DefaultPlaceRequest backPlaceRequest;

    private String selectOu = "";

    @OnStartup
    public void onStartup( final PlaceRequest place ) {
        setupBackPlaceRequest( place );
        loadSelectedOU( place );
    }

    private void loadSelectedOU( PlaceRequest place ) {
        this.selectOu = place.getParameter( "selected_ou", "" );
        if ( this.selectOu.isEmpty() ) {
            loadDefaultOU();
        } else {
            view.setGroupName( selectOu );
        }
    }

    private void loadDefaultOU() {
        libraryService.call( new RemoteCallback<OrganizationalUnit>() {
            @Override
            public void callback( OrganizationalUnit o ) {
                NewProjectScreen.this.selectOu = o.getIdentifier();
                view.setGroupName( selectOu );
            }
        } ).getDefaultOrganizationalUnit();
    }

    private void setupBackPlaceRequest( PlaceRequest place ) {
        String placeTarget = place.getParameter( "backPlace", "EmptyLibraryScreen" );
        this.backPlaceRequest = new DefaultPlaceRequest( placeTarget );
    }

    public void back() {
        placeManager.goTo( backPlaceRequest );
    }

    public void createProject( String projectName ) {
        busyIndicatorView.showBusyIndicator( "Saving" );

        libraryService.call( getSuccessCallback(), getErrorCallBack() ).newProject( projectName, selectOu );
    }

    private ErrorCallback<?> getErrorCallBack() {
        return ( o, throwable ) -> {
            hideLoadingBox();
            notificationEvent.fire( new NotificationEvent( "Error creating a project.",
                                                           NotificationEvent.NotificationType.ERROR ) );
            return false;
        };
    }

    private void hideLoadingBox() {
        busyIndicatorView.hideBusyIndicator();
    }

    private RemoteCallback<KieProject> getSuccessCallback() {
        return project -> {
            hideLoadingBox();
            notifySuccess();
            setupBreadCrumbs( project );
            openProject( project );
        };
    }

    private void setupBreadCrumbs( KieProject project ) {
        breadcrumbs.clearBreadCrumbs( "AuthoringPerspective" );
        breadcrumbs.addBreadCrumb( "AuthoringPerspective", "All Projects",
                                   new DefaultPlaceRequest( "LibraryPerspective" ) );
        breadcrumbs
                .addBreadCrumb( "AuthoringPerspective", project.getProjectName(),
                                new DefaultPlaceRequest( "AuthoringPerspective" ) );
    }

    private void notifySuccess() {
        notificationEvent.fire( new NotificationEvent( "Project Created" ) );
    }

    private void openProject( KieProject project ) {
        placeManager.goTo( "AuthoringPerspective" );
        socialEvent.fire( new SocialFileSelectedEvent( "NEW_PROJECT", project.getIdentifier() ) );
    }


    public interface View extends UberElement<NewProjectScreen> {
        void setGroupName( String name );
    }

    @PostConstruct
    public void setup() {
        view.init( this );
    }

    @WorkbenchPartTitle
    public String getTitle() {
        return "NewProjectScreen";
    }

    @WorkbenchPartView
    public UberElement<NewProjectScreen> getView() {
        return view;
    }

}
