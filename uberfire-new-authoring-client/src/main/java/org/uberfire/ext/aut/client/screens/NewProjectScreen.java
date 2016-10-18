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

import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;
import org.uberfire.client.mvp.PlaceManager;
import org.uberfire.client.mvp.UberElement;
import org.uberfire.ext.widgets.common.client.breadcrumbs.UberfireBreadcrumbs;
import org.uberfire.lifecycle.OnStartup;
import org.uberfire.mvp.PlaceRequest;
import org.uberfire.mvp.impl.DefaultPlaceRequest;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@WorkbenchScreen( identifier = "NewProjectScreen" )
public class NewProjectScreen {


    @Inject
    private PlaceManager placeManager;

//    @Inject
//    private Caller<KieProjectService> projectServiceCaller;
//
//    @Inject
//    private Caller<RepositoryService> repositoryService;
//
//    @Inject
//    private Caller<OrganizationalUnitService> organizationalUnitService;
//
//    @Inject
//    private BusyIndicatorView busyIndicatorView;
//
//    @Inject
//    private Event<NotificationEvent> notificationEvent;
//
//    private Collection<OrganizationalUnit> organizationalUnits;
//    private Optional<OrganizationalUnit> defaultOU;
//    private Repository repository;
//
//    @Inject
//    private ConflictingRepositoriesPopup conflictingRepositoriesPopup;

    @Inject
    private UberfireBreadcrumbs breadcrumbs;

    private DefaultPlaceRequest backPlaceRequest;

    @OnStartup
    public void onStartup( final PlaceRequest place ) {
        String placeTarget = place.getParameter( "backPlace", "EmptyLibraryScreen" );
        this.backPlaceRequest = new DefaultPlaceRequest( placeTarget );

    }

    @AfterInitialization
    public void load() {
//        //TODO ederign handle if there is no OU
//        organizationalUnitService.call( new RemoteCallback<Collection<OrganizationalUnit>>() {
//            @Override
//            public void callback( Collection<OrganizationalUnit> organizationalUnits ) {
//                NewProjectScreen.this.organizationalUnits = organizationalUnits;
//                NewProjectScreen.this.defaultOU = organizationalUnits.stream().findFirst();
//                getRepo();
//                view.setGroupName( defaultOU.get().getName() );
//            }
//        } ).getOrganizationalUnits();

    }

    private void getRepo() {
//        repositoryService.call( new RemoteCallback<Repository>() {
//            @Override
//            public void callback( Repository repository ) {
//                if ( repository != null ) {
//                    NewProjectScreen.this.repository = repository;
//                } else {
//                    createDefaultRepo();
//                }
//
//            }
//        }, new ErrorCallback<Repository>() {
//            @Override
//            public boolean error( Repository repository, Throwable throwable ) {
//                Window.alert( "TODO GET REPOSITORY" );
//                return false;
//            }
//        } ).getRepository( "default-" + defaultOU.get().getName() );
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


    public void back() {
        placeManager.goTo( backPlaceRequest );
    }

//    public void createProject( String projectName ) {
//
//
//        Path repoRoot = repository.getRoot();
//        POM pom = new POMBuilder()
//                .setGroupId( "me.ederign" )
//                .setPackaging( "jar" )
//                .setVersion( "1.0.0" )
//                .setProjectName( projectName )
//                .build();
//        DeploymentMode mode = DeploymentMode.VALIDATED;
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
//    private RemoteCallback<KieProject> getSuccessCallback() {
//        return new RemoteCallback<KieProject>() {
//
//            @Override
//            public void callback( final KieProject project ) {
//
//                busyIndicatorView.hideBusyIndicator();
//                notificationEvent.fire( new NotificationEvent( CommonConstants.INSTANCE.ItemCreatedSuccessfully() ) );
//                openProject( project.getProjectName() );
//            }
//        };
//    }

    private void openProject( String projectName ) {
        //TODO ederign
        breadcrumbs.createRoot( "ProjectsPerspective", "All Projects", new DefaultPlaceRequest( "ProjectsPerspective" ),
                                true );
        Map<String, String> params = new HashMap<>();
        params.put( "projectName", projectName );
        //ou and others parameters here
        breadcrumbs.navigateTo( projectName, new DefaultPlaceRequest( "ProjectScreen", params ) );
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
