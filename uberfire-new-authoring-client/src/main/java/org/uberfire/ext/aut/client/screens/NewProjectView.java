package org.uberfire.ext.aut.client.screens;

import com.google.gwt.user.client.Event;
import org.jboss.errai.common.client.dom.Button;
import org.jboss.errai.common.client.dom.Heading;
import org.jboss.errai.common.client.dom.Input;
import org.jboss.errai.common.client.dom.Label;
import org.jboss.errai.ui.client.local.api.IsElement;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.SinkNative;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import javax.inject.Inject;
import javax.inject.Named;

@Templated
public class NewProjectView implements NewProjectScreen.View, IsElement {

    private NewProjectScreen presenter;


    @Named( "h1" )
    @Inject
    @DataField
    private Heading back;

    @Inject
    @DataField
    private Input projectName;

    @Inject
    @DataField
    private Input ouName;

    @Inject
    @DataField
    private Button cancel;

    @Inject
    @DataField
    private Button create;

    @Inject
    @DataField
    private Label ouLabel;

    @Override
    public void init( NewProjectScreen presenter ) {
        this.presenter = presenter;
        back.setOnmouseover( f -> back.getStyle().setProperty( "cursor", "pointer" ) );
    }

    @SinkNative( Event.ONCLICK )
    @EventHandler( "back" )
    public void back( Event e ) {
        presenter.back();
    }

    @SinkNative( Event.ONCLICK )
    @EventHandler( "cancel" )
    public void cancel( Event e ) {
        presenter.back();
    }

    @SinkNative( Event.ONCLICK )
    @EventHandler( "create" )
    public void createProject( Event e ) {
        presenter.createProject( projectName.getValue() );
    }


    @Override
    public void setOUName( String name ) {
        ouName.setValue( name );
    }

    @Override
    public void setOUAlias( String ouAlias ) {
        ouLabel.setTextContent( ouAlias );
    }
}