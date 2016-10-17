package org.uberfire.ext.aut.client.screens;

import com.google.inject.Inject;
import org.jboss.errai.common.client.dom.*;
import org.jboss.errai.ioc.client.api.ManagedInstance;
import org.jboss.errai.ui.client.local.api.IsElement;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.uberfire.ext.aut.client.widgets.ProjectItemWidget;
import org.uberfire.mvp.Command;

@Templated
public class ProjectsDetailView implements ProjectsDetailScreen.View, IsElement {

    private ProjectsDetailScreen presenter;

    @Inject
    @DataField
    Div temp;

    @Override
    public void init( ProjectsDetailScreen presenter ) {
        this.presenter = presenter;
    }

    @Override
    public void update( String projectSelected ) {
        temp.setTextContent( projectSelected );
    }
}