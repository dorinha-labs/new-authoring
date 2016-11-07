package org.uberfire.ext.aut.client.util;


import org.guvnor.common.services.project.model.Project;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.uberfire.client.workbench.docks.UberfireDock;
import org.uberfire.client.workbench.docks.UberfireDockPosition;
import org.uberfire.client.workbench.docks.UberfireDocks;
import org.uberfire.client.workbench.docks.UberfireDocksInteractionEvent;
import org.uberfire.ext.aut.client.events.ProjectDetailEvent;
import org.uberfire.ext.aut.client.resources.i18n.NewAuthoringConstants;
import org.uberfire.mvp.impl.DefaultPlaceRequest;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class ProjectsDocks {

    @Inject
    private UberfireDocks uberfireDocks;

    @Inject
    private Event<ProjectDetailEvent> projectDetailEvent;

    @Inject
    private TranslationService ts;


    private UberfireDock projectDock;

    private Project currentProject;

    @PostConstruct
    public void setup() {
        projectDock = new UberfireDock( UberfireDockPosition.EAST, "INFO_CIRCLE",
                                        new DefaultPlaceRequest( "ProjectsDetailScreen" ),
                                        "LibraryPerspective" )
                .withSize( 450 )
                .withLabel( ts.getTranslation(
                        NewAuthoringConstants.ProjectsDetailsScreen_Title ) );
    }

    public UberfireDock getProjectDock() {
        return projectDock;
    }

    public void handle( Project selectedProject ) {

        if ( currentProject == null ) {
            uberfireDocks.enable( projectDock.getDockPosition(), projectDock.getAssociatedPerspective() );
            uberfireDocks.expand( projectDock );
        }
        this.currentProject = selectedProject;
        projectDetailEvent.fire( new ProjectDetailEvent( selectedProject ) );
    }

    public void start() {
        uberfireDocks.add( projectDock );
        uberfireDocks.disable( projectDock.getDockPosition(), projectDock.getAssociatedPerspective() );
    }

    public void hide() {
        uberfireDocks.disable( projectDock.getDockPosition(), projectDock.getAssociatedPerspective() );
    }

    public void refresh() {
        currentProject = null;
    }

    public void reloadProjectDetail( @Observes UberfireDocksInteractionEvent event ) {

        if ( shouldUpdate( event ) ) {
            projectDetailEvent.fire( new ProjectDetailEvent( currentProject ) );
        }
    }

    private boolean shouldUpdate( @Observes UberfireDocksInteractionEvent event ) {
        return currentProject != null && event.getTargetDock() == projectDock && event
                .getType() == UberfireDocksInteractionEvent.InteractionType.SELECTED;
    }
}
