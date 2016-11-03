package org.uberfire.ext.aut.client.screens;

import com.google.gwt.user.client.Event;
import org.jboss.errai.common.client.dom.Anchor;
import org.jboss.errai.common.client.dom.Button;
import org.jboss.errai.common.client.dom.Heading;
import org.jboss.errai.ui.client.local.api.IsElement;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.SinkNative;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.uberfire.ext.aut.client.resources.i18n.NewAuthoringConstants;

import javax.inject.Inject;
import javax.inject.Named;

@Templated
public class EmptyLibraryView implements EmptyLibraryScreen.View, IsElement {

    private EmptyLibraryScreen presenter;

    @Inject
    @DataField
    private Button newProject;

//    @Inject
//    @DataField
//    private Button mortageDemo;
//
//    @Inject
//    @DataField
//    private Button hrOnboardingDemo;
//
//    @Inject
//    @DataField
//    private Button medicalAppDemo;

    @Named( "h1" )
    @Inject
    @DataField
    private Heading welcome;

    @Inject
    @DataField
    private Anchor newProjectLink;

    @Inject
    TranslationService ts;

    @Override
    public void init( EmptyLibraryScreen presenter ) {
        this.presenter = presenter;
    }

    @SinkNative( Event.ONCLICK )
    @EventHandler( "newProject" )
    public void newProject( Event e ) {
        presenter.newProject();
    }

    @SinkNative( Event.ONCLICK )
    @EventHandler( "newProjectLink" )
    public void newProjectLink( Event e ) {
        presenter.newProject();
    }

//    @SinkNative( Event.ONCLICK )
//    @EventHandler( "mortageDemo" )
//    public void mortageDemo( Event e ) {
//        presenter.runDemo( "mortageDemo" );
//    }
//
//    @SinkNative( Event.ONCLICK )
//    @EventHandler( "hrOnboardingDemo" )
//    public void hrOnboardingDemo( Event e ) {
//        presenter.runDemo( "hrOnboardingDemo" );
//    }
//
//    @SinkNative( Event.ONCLICK )
//    @EventHandler( "medicalAppDemo" )
//    public void medicalAppDemo( Event e ) {
//        presenter.runDemo( "medicalAppDemo" );
//    }

    @Override
    public void setup( String username ) {
        welcome.setInnerHTML( ts.getTranslation( NewAuthoringConstants.EmptyLibraryView_Welcome ) + " " +  username + "." );
    }
}