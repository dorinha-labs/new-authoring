package org.uberfire.ext.aut.client.util;


import org.guvnor.common.services.project.model.Project;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.uberfire.client.workbench.docks.UberfireDock;
import org.uberfire.client.workbench.docks.UberfireDockPosition;
import org.uberfire.client.workbench.docks.UberfireDocks;
import org.uberfire.ext.aut.client.events.ProjectDetailEvent;
import org.uberfire.ext.aut.client.resources.i18n.NewAuthoringConstants;
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

    @Inject
    private TranslationService ts;


    private UberfireDock uberfireDock;

    private Project currentProject;

    @PostConstruct
    public void setup() {
        uberfireDock = new UberfireDock( UberfireDockPosition.EAST, "INFO_CIRCLE",
                                         new DefaultPlaceRequest( "ProjectsDetailScreen" ),
                                         "LibraryPerspective" )
                .withSize( 450 )
                .withLabel( ts.getTranslation(
                        NewAuthoringConstants.ProjectsDetailsScreen_Title ) );
    }

    public UberfireDock getUberfireDock() {
        return uberfireDock;
    }

    public void handle( Project selectedProject ) {

        if ( currentProject == null ) {
            uberfireDocks.enable( uberfireDock.getDockPosition(), uberfireDock.getAssociatedPerspective() );
            uberfireDocks.expand( uberfireDock );
        }
        this.currentProject = selectedProject;
        projectDetailEvent.fire( new ProjectDetailEvent( selectedProject ) );

    }

    public void start() {
        uberfireDocks.add( uberfireDock );
        uberfireDocks.disable( uberfireDock.getDockPosition(), uberfireDock.getAssociatedPerspective() );
    }

    public void hide(){
        uberfireDocks.disable( uberfireDock.getDockPosition(), uberfireDock.getAssociatedPerspective() );
    }

    public void refresh() {
        currentProject = null;
    }
}
