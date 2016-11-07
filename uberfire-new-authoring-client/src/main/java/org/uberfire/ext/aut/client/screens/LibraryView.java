package org.uberfire.ext.aut.client.screens;

import com.google.gwt.user.client.Event;
import com.google.inject.Inject;
import org.jboss.errai.common.client.dom.*;
import org.jboss.errai.ioc.client.api.ManagedInstance;
import org.jboss.errai.ui.client.local.api.IsElement;
import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.SinkNative;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.uberfire.ext.aut.client.resources.i18n.NewAuthoringConstants;
import org.uberfire.ext.aut.client.widgets.ProjectItemWidget;
import org.uberfire.mvp.Command;

@Templated
public class LibraryView implements LibraryScreen.View, IsElement {

    private LibraryScreen presenter;

    @DataField
    @Inject
    Div projectList;

    @DataField
    @Inject
    Button newProjectButton;

    @DataField
    @Inject
    Button importExample;

    @DataField
    @Inject
    Input filterText;

    @Inject
    Document document;

    @Inject
    ManagedInstance<ProjectItemWidget> itemWidgetsInstances;

    @Inject
    TranslationService ts;

    @Override
    public void init( LibraryScreen presenter ) {
        this.presenter = presenter;
        filterText.setAttribute( "placeholder", ts.getTranslation( NewAuthoringConstants.LibraryView_Filter ) );
    }

    @Override
    public void clearProjects() {
        DOMUtil.removeAllChildren( projectList );
    }

    @Override
    public void addProject( String project, Command details, Command select ) {
        ProjectItemWidget projectItemWidget = itemWidgetsInstances.get();
        projectItemWidget.init( project, details, select );
        projectList.appendChild( projectItemWidget.getElement() );
    }

    @Override
    public void clearFilterText() {
        this.filterText.setValue( "" );
    }


    @SinkNative( Event.ONCLICK )
    @EventHandler( "newProjectButton" )
    public void newProject( Event e ) {

        presenter.newProject();
    }

    @SinkNative( Event.ONCLICK )
    @EventHandler( "importExample" )
    public void importExample( Event e ) {
        presenter.importExample();
    }

    @SinkNative( Event.ONKEYUP )
    @EventHandler( "filterText" )
    public void filterTextChange( Event e ) {
        presenter.filterProjects( filterText.getValue() );
    }


    private Option createOption( String ou ) {
        Option option = ( Option ) document.createElement( "option" );
        option.setText( ou );
        return option;
    }
}