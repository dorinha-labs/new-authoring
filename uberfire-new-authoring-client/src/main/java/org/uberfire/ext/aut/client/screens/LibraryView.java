package org.uberfire.ext.aut.client.screens;

import com.google.inject.Inject;
import org.jboss.errai.common.client.dom.*;
import org.jboss.errai.ioc.client.api.ManagedInstance;
import org.jboss.errai.ui.client.local.api.IsElement;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.SinkNative;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.uberfire.ext.aut.client.widgets.ProjectItemWidget;
import org.uberfire.mvp.Command;

@Templated
public class LibraryView implements LibraryScreen.View, IsElement {

    private LibraryScreen presenter;

    @DataField
    @Inject
    Select teamDropdown;

    @DataField
    @Inject
    Div projectList;

    @DataField
    @Inject
    Button newProjectButton;

    @Inject
    Document document;

    @Inject
    ManagedInstance<ProjectItemWidget> itemWidgetsInstances;

    @Override
    public void init( LibraryScreen presenter ) {
        this.presenter = presenter;
        teamDropdown.setOnchange( event -> presenter.selectOrganizationUnit( teamDropdown.getValue() ) );
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
    public void addOrganizationUnit( String ou ) {
        teamDropdown.add( createOption( ou ) );
    }

    @Override
    public void clearOrganizationUnits() {
        DOMUtil.removeAllChildren( teamDropdown );
    }


    @SinkNative( com.google.gwt.user.client.Event.ONCLICK )
    @EventHandler( "newProjectButton" )
    public void newProject( com.google.gwt.user.client.Event e ) {
        presenter.newProject();
    }

    private Option createOption( String ou ) {
        Option option = ( Option ) document.createElement( "option" );
        option.setText( ou );
        return option;
    }
}