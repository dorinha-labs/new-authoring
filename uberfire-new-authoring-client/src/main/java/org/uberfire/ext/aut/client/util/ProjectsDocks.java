package org.uberfire.ext.aut.client.util;


import org.uberfire.client.workbench.docks.UberfireDock;
import org.uberfire.client.workbench.docks.UberfireDockPosition;
import org.uberfire.client.workbench.docks.UberfireDocks;
import org.uberfire.ext.aut.client.events.ProjectDetailEvent;
import org.uberfire.mvp.impl.DefaultPlaceRequest;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

@ApplicationScoped
public class ProjectsDocks {

    @Inject
    private UberfireDocks uberfireDocks;

    @Inject
    private Event<ProjectDetailEvent> projectDetailEvent;


    private UberfireDock uberfireDock;

    private String currentProject;

    @PostConstruct
    public void setup() {
        uberfireDock = new UberfireDock( UberfireDockPosition.EAST, "INFO_CIRCLE",
                                         new DefaultPlaceRequest( "ProjectsDetailScreen" ),
                                         "ProjectsPerspective" ).withSize( 450 ).withLabel( "View Project" );
    }

    public UberfireDock getUberfireDock() {
        return uberfireDock;
    }

    public void handle( String selectedProject ) {

        if ( currentProject == null ) {
            currentProject = selectedProject;
            uberfireDocks.enable( uberfireDock.getDockPosition(), uberfireDock.getAssociatedPerspective() );
            uberfireDocks.expand( uberfireDock );
            projectDetailEvent.fire( new ProjectDetailEvent( selectedProject ) );

        } else if ( currentProject == selectedProject ) {
            uberfireDocks.disable( uberfireDock.getDockPosition(), uberfireDock.getAssociatedPerspective() );
            currentProject = null;
        } else {
            projectDetailEvent.fire( new ProjectDetailEvent( selectedProject ) );
        }

    }

    public void start() {
        uberfireDocks.add( uberfireDock );
        uberfireDocks.disable( uberfireDock.getDockPosition(), uberfireDock.getAssociatedPerspective() );
    }

    public void refresh() {
        currentProject = null;
    }
}
