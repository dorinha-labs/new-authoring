package org.uberfire.ext.aut.client.widgets;

import com.google.gwt.user.client.Event;
import org.jboss.errai.common.client.dom.Anchor;
import org.jboss.errai.common.client.dom.Div;
import org.jboss.errai.ui.client.local.api.IsElement;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.SinkNative;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.uberfire.mvp.Command;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
@Templated
public class KebabWidget implements IsElement {

    @Inject
    @DataField
    private Anchor details;

    @Inject
    @DataField
    private Anchor selectProject;

    private Command detailsCommand;

    private Command selectCommand;

    public void init( Command detailsCommand, Command selectCommand ) {

        this.detailsCommand = detailsCommand;
        this.selectCommand = selectCommand;
    }

    @SinkNative( Event.ONCLICK )
    @EventHandler( "details" )
    public void detailsClick( Event e ) {
        detailsCommand.execute();
    }

    @SinkNative( Event.ONCLICK )
    @EventHandler( "selectProject" )
    public void selectCommand( Event e ) {
        selectCommand.execute();
    }


}
