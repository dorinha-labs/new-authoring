package org.uberfire.ext.aut.client.perspective;/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import com.google.gwt.user.client.ui.Composite;
import org.jboss.errai.common.client.dom.Div;
import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.uberfire.client.annotations.Perspective;
import org.uberfire.client.annotations.WorkbenchPerspective;
import org.uberfire.client.mvp.PlaceManager;
import org.uberfire.client.workbench.docks.UberfireDocks;
import org.uberfire.ext.aut.client.util.ProjectsDocks;
import org.uberfire.ext.widgets.common.client.breadcrumbs.UberfireBreadcrumbs;
import org.uberfire.lifecycle.OnOpen;
import org.uberfire.lifecycle.OnStartup;
import org.uberfire.mvp.PlaceRequest;
import org.uberfire.mvp.impl.DefaultPlaceRequest;
import org.uberfire.workbench.model.PerspectiveDefinition;
import org.uberfire.workbench.model.impl.PartDefinitionImpl;
import org.uberfire.workbench.model.impl.PerspectiveDefinitionImpl;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
@WorkbenchPerspective( identifier = "LibraryPerspective", isDefault = true )
public class LibraryPerspective extends Composite {


    @Inject
    PlaceManager placeManager;

    @Inject
    private UberfireDocks uberfireDocks;

    @Inject
    private ProjectsDocks projectsDocks;


    @AfterInitialization
    public void setupDocks() {
        projectsDocks.start();
    }


    @Perspective
    public PerspectiveDefinition buildPerspective() {
        final PerspectiveDefinition p = new PerspectiveDefinitionImpl(
                "org.uberfire.client.workbench.panels.impl.StaticWorkbenchPanelPresenter" );
        p.setName( "Library Perspective" );
        p.getRoot().addPart(
                new PartDefinitionImpl(
                        new DefaultPlaceRequest( "LibraryScreen" ) ) );

        return p;
    }

}
