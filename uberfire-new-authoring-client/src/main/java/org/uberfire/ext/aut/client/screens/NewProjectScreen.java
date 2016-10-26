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
import org.guvnor.structure.organizationalunit.OrganizationalUnit;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.ErrorCallback;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.kie.uberfire.social.activities.model.SocialFileSelectedEvent;
import org.kie.workbench.common.services.shared.project.KieProject;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;
import org.uberfire.client.mvp.PlaceManager;
import org.uberfire.client.mvp.UberElement;
import org.uberfire.ext.aut.api.LibraryService;
import org.uberfire.ext.widgets.common.client.breadcrumbs.UberfireBreadcrumbs;
import org.uberfire.ext.widgets.common.client.common.BusyIndicatorView;
import org.uberfire.ext.widgets.table.client.resources.i18n.CommonConstants;
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
    private UberfireBreadcrumbs breadcrumbs;

    //TODO
    @Inject
    private Event<SocialFileSelectedEvent> socialEvent;

    private DefaultPlaceRequest backPlaceRequest;
    private String selectOu = "";

    @OnStartup
    public void onStartup( final PlaceRequest place ) {
        String placeTarget = place.getParameter( "backPlace", "EmptyLibraryScreen" );
        this.backPlaceRequest = new DefaultPlaceRequest( placeTarget );
        this.selectOu = place.getParameter( "selected_ou", "" );
        if ( this.selectOu.isEmpty() ) {
            libraryService.call( new RemoteCallback<OrganizationalUnit>() {
                @Override
                public void callback( OrganizationalUnit o ) {
                    NewProjectScreen.this.selectOu = o.getIdentifier();
                    view.setGroupName( selectOu );
                }
            } ).getDefaultOrganizationalUnit();
        }
        else{
            view.setGroupName( selectOu );
        }
    }

    public void back() {
        placeManager.goTo( backPlaceRequest );
    }

    public void createProject( String projectName ) {
        busyIndicatorView.showBusyIndicator( "Saving" );

        libraryService.call( getSuccessCallback(), getErrorCallBack() ).newProject( projectName, selectOu );
    }

    private ErrorCallback<?> getErrorCallBack() {
        return null;
    }

    private RemoteCallback<KieProject> getSuccessCallback() {
        return new RemoteCallback<KieProject>() {
            @Override
            public void callback( final KieProject project ) {
                GWT.log(project + "");
                GWT.log(project.getIdentifier() + "");
                GWT.log(project.getProjectName() + "");
                GWT.log(project + "");


                busyIndicatorView.hideBusyIndicator();
                notificationEvent.fire( new NotificationEvent( "Project Created" ) );
                openProject( project );
            }
        };
    }

    private void openProject( KieProject project ) {
        //TODO ederign
//        breadcrumbs.createRoot( "ProjectsPerspective", "All Projects", new DefaultPlaceRequest( "ProjectsPerspective" ),
//                                true );
//        Map<String, String> params = new HashMap<>();
//        params.put( "projectName", projectName );
//        ou and others parameters here
//        breadcrumbs.navigateTo( projectName, new DefaultPlaceRequest( "ProjectScreen", params ) );
        placeManager.goTo( "AuthoringPerspective" );
        socialEvent.fire( new SocialFileSelectedEvent( "NEW_PROJECT", project.getIdentifier() ) );
    }

    public interface View extends UberElement<NewProjectScreen> {
        void setGroupName( String name );
    }

    @Inject
    private View view;

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


//    private HashMap<Class<? extends Throwable>, CommandWithThrowableDrivenErrorCallback.CommandWithThrowable> createErrorsHandler(
//            POM pom ) {
//        return new HashMap<Class<? extends Throwable>, CommandWithThrowableDrivenErrorCallback.CommandWithThrowable>(
//        ) {{
//            put( GAVAlreadyExistsException.class,
//                 new CommandWithThrowableDrivenErrorCallback.CommandWithThrowable() {
//                     @Override
//                     public void execute( final Throwable parameter ) {
//                         GWT.log( "." );
//                         busyIndicatorView.hideBusyIndicator();
//                         conflictingRepositoriesPopup.setContent( pom.getGav(),
//                                                                  ( ( GAVAlreadyExistsException ) parameter )
//                                                                          .getRepositories(),
//                                                                  new Command() {
//                                                                      @Override
//                                                                      public void execute() {
//                                                                          conflictingRepositoriesPopup.hide();
//                                                                          createProject( pom.getName() );
//                                                                      }
//                                                                  } );
//                         conflictingRepositoriesPopup.show();
//                     }
//                 } );
//        }};
//    }
}
