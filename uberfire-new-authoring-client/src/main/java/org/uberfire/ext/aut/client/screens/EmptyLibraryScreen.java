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

import org.jboss.errai.security.shared.api.identity.User;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;
import org.uberfire.client.mvp.PlaceManager;
import org.uberfire.client.mvp.UberElement;
import org.uberfire.ext.aut.api.LibraryContextSwitchEvent;
import org.uberfire.mvp.impl.DefaultPlaceRequest;
import org.uberfire.rpc.SessionInfo;
import org.uberfire.security.ResourceRef;
import org.uberfire.security.authz.AuthorizationManager;
import org.uberfire.workbench.model.ActivityResourceType;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.inject.Inject;

@WorkbenchScreen( identifier = "EmptyLibraryScreen" )
public class EmptyLibraryScreen {


    public interface View extends UberElement<EmptyLibraryScreen> {

        void setup( String username );

    }

    @Inject
    private View view;

    @Inject
    private User user;

    @Inject
    private PlaceManager placeManager;

    @Inject
    private Event<LibraryContextSwitchEvent> libraryContextSwitchEvent;

    @Inject
    private AuthorizationManager authorizationManager;

    @Inject
    private SessionInfo sessionInfo;


    @PostConstruct
    public void setup() {
        view.init( this );
        view.setup( user.getIdentifier() );
    }

    public void newProject() {
        placeManager.goTo( new DefaultPlaceRequest( "NewProjectScreen" ) );
    }


    public void importExample() {
        //TODO PerspectiveIds.Authoring
        if ( hasAccessToPerspective( "AuthoringPerspective" ) ) {

            placeManager.goTo( new DefaultPlaceRequest( "AuthoringPerspective" ) );
            libraryContextSwitchEvent
                    .fire( new LibraryContextSwitchEvent( LibraryContextSwitchEvent.EventType.PROJECT_FROM_EXAMPLE ) );
        }
    }

    boolean hasAccessToPerspective( String perspectiveId ) {
        ResourceRef resourceRef = new ResourceRef( perspectiveId, ActivityResourceType.PERSPECTIVE );
        return authorizationManager.authorize( resourceRef, sessionInfo.getIdentity() );
    }

    @WorkbenchPartTitle
    public String getTitle() {
        return "Empty Project Screen";
    }

    @WorkbenchPartView
    public UberElement<EmptyLibraryScreen> getView() {
        return view;
    }
}
