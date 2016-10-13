package org.uberfire.ext.aut.client.screens;

import org.jboss.errai.ui.client.local.api.IsElement;
import org.jboss.errai.ui.shared.api.annotations.Templated;

@Templated
public class ProjectsView implements ProjectsScreen.View, IsElement {

    private ProjectsScreen presenter;


    @Override
    public void init( ProjectsScreen presenter ) {
        this.presenter = presenter;

    }


}