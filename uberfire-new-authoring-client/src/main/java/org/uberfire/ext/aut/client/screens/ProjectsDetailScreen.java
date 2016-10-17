/*
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

package org.uberfire.ext.aut.client.screens;

import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;
import org.uberfire.client.mvp.UberElement;
import org.uberfire.ext.aut.client.events.ProjectDetailEvent;
import org.uberfire.lifecycle.OnOpen;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

@WorkbenchScreen( identifier = "ProjectsDetailScreen" )
public class ProjectsDetailScreen {

    public interface View extends UberElement<ProjectsDetailScreen> {

        void update( String projectSelected );
    }

    @Inject
    private View view;

    @OnOpen
    public void onOpen() {

    }
    //https://bluejeans.com/2934419266/

    public void update( @Observes ProjectDetailEvent event){
        view.update(event.getProjectSelected());
    }

    @WorkbenchPartTitle
    public String getTitle() {
        return "ProjectsDetailScreen";
    }

    @WorkbenchPartView
    public UberElement<ProjectsDetailScreen> getView() {
        return view;
    }
}
