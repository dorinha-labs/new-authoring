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

import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.ErrorCallback;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.kie.workbench.common.services.shared.project.KieProject;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;
import org.uberfire.client.mvp.PlaceManager;
import org.uberfire.client.mvp.UberElement;
import org.uberfire.ext.aut.api.LibraryInfo;
import org.uberfire.ext.aut.api.LibrarySelectedEvent;
import org.uberfire.ext.aut.api.LibraryService;
import org.uberfire.ext.aut.client.events.NewProjectErrorEvent;
import org.uberfire.ext.aut.client.resources.i18n.NewAuthoringConstants;
import org.uberfire.ext.widgets.common.client.breadcrumbs.UberfireBreadcrumbs;
import org.uberfire.ext.widgets.common.client.common.BusyIndicatorView;
import org.uberfire.lifecycle.OnStartup;
import org.uberfire.mvp.PlaceRequest;
import org.uberfire.mvp.impl.DefaultPlaceRequest;
import org.uberfire.rpc.SessionInfo;
import org.uberfire.security.ResourceRef;
import org.uberfire.security.authz.AuthorizationManager;
import org.uberfire.workbench.events.NotificationEvent;
import org.uberfire.workbench.model.ActivityResourceType;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.inject.Inject;

@WorkbenchScreen( identifier = "NewProjectScreen" )
public class NewProjectScreen {

    public interface View extends UberElement<NewProjectScreen> {
        void setOUName( String name );

        void setOUAlias( String ouAlias );
    }

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

    @Inject
    private TranslationService ts;

    @Inject
    private Event<LibrarySelectedEvent> librarySelectedEvent;

    @Inject
    private AuthorizationManager authorizationManager;

    @Inject
    private SessionInfo sessionInfo;

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
            loadOU( selectOu );
        }
    }

    private void loadOU( String selectOu ) {
        libraryService.call( new RemoteCallback<LibraryInfo>() {
            @Override
            public void callback( LibraryInfo lib ) {
                NewProjectScreen.this.selectOu = lib.getSelectedOrganizationUnit().getIdentifier();
                view.setOUName( NewProjectScreen.this.selectOu );
                view.setOUAlias( lib.getOuAlias() );
            }
        } ).getLibraryInfo( selectOu );
    }

    private void loadDefaultOU() {
        libraryService.call( new RemoteCallback<LibraryInfo>() {
            @Override
            public void callback( LibraryInfo lib ) {
                NewProjectScreen.this.selectOu = lib.getSelectedOrganizationUnit().getIdentifier();
                view.setOUName( selectOu );
                view.setOUAlias( lib.getOuAlias() );
            }
        } ).getDefaultLibraryInfo();
    }

    private void setupBackPlaceRequest( PlaceRequest place ) {
        String placeTarget = place.getParameter( "backPlace", "EmptyLibraryScreen" );
        this.backPlaceRequest = new DefaultPlaceRequest( placeTarget );
    }

    public void back() {
        placeManager.goTo( backPlaceRequest );
    }

    public void createProject( String projectName ) {
        busyIndicatorView.showBusyIndicator( ts.getTranslation( NewAuthoringConstants.NewProjectScreen_Saving ) );

        libraryService.call( getSuccessCallback(), getErrorCallBack() ).newProject( projectName, selectOu );
    }

    private ErrorCallback<?> getErrorCallBack() {
        return ( o, throwable ) -> {
            hideLoadingBox();
            notificationEvent
                    .fire( new NotificationEvent( ts.getTranslation( NewAuthoringConstants.NewProjectScreen_Error ),
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
            //TODO PerspectiveIds.Authoring
            if ( hasAccessToPerspective( "AuthoringPerspective" ) ) {
                setupBreadCrumbs( project );
                openProject( project );
            }
            else{
                //TODO no rights popup? DefaultSocialLinkCommandGenerator
            }
        };
    }

    boolean hasAccessToPerspective( String perspectiveId ) {
        ResourceRef resourceRef = new ResourceRef( perspectiveId, ActivityResourceType.PERSPECTIVE );
        return authorizationManager.authorize( resourceRef, sessionInfo.getIdentity() );
    }

    private void setupBreadCrumbs( KieProject project ) {
        breadcrumbs.clearBreadCrumbs( "AuthoringPerspective" );
        breadcrumbs.addBreadCrumb( "AuthoringPerspective",
                                   ts.getTranslation( NewAuthoringConstants.NewProjectScreen_AllProjects ),
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
        librarySelectedEvent.fire( new LibrarySelectedEvent( LibrarySelectedEvent.EventType.PROJECT_SELECTED,
                                                             project.getIdentifier() ) );
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
