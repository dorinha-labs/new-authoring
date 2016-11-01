/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
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

package org.uberfire.ext.aut.api;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.uberfire.ext.preferences.shared.annotations.Property;
import org.uberfire.ext.preferences.shared.annotations.WorkbenchPreference;
import org.uberfire.ext.preferences.shared.bean.BasePreference;

@WorkbenchPreference( identifier = "LibraryPreferences",
        bundleKey = "LibraryPreferences.Label" )
@Portable
public class LibraryPreferences implements BasePreference<LibraryPreferences> {

    @Property( bundleKey = "LibraryPreferences.OuName" )
    String ouName;

    @Property( bundleKey = "LibraryPreferences.OuOwner" )
    String oUOwner;

    @Property( bundleKey = "LibraryPreferences.OUGroupId" )
    String oUGroupId;

    @Property( bundleKey = "LibraryPreferences.OUAlias" )
    String ouAlias;

    @Property( bundleKey = "LibraryPreferences.RepositoryID" )
    String repositoryId;

    @Property( bundleKey = "LibraryPreferences.RepositoryDefaultScheme" )
    String repositoryDefaultScheme;

    @Property( bundleKey = "LibraryPreferences.ProjectGroupId" )
    String projectGroupId;

    @Property( bundleKey = "LibraryPreferences.ProjectVersion" )
    String projectVersion;

    @Property( bundleKey = "LibraryPreferences.ProjectDescription" )
    String projectDescription;

    @Property( bundleKey = "LibraryPreferences.ProjectDefaultBranch" )
    String projectDefaultBranch;




    @Override
    public LibraryPreferences defaultValue( final LibraryPreferences defaultValue ) {
        defaultValue.ouName = "default-ou";
        defaultValue.oUOwner = "admin";
        defaultValue.oUGroupId = "org.default";
        defaultValue.ouAlias = "Team";
        defaultValue.repositoryId = "default-repo";
        defaultValue.repositoryDefaultScheme = "git";
        defaultValue.projectGroupId = "org.default";
        defaultValue.projectVersion = "1.0.0";
        defaultValue.projectDescription = "default description";
        defaultValue.projectDefaultBranch = "master";
        return defaultValue;
    }

    public String getOuName() {
        return ouName;
    }

    public String getoUOwner() {
        return oUOwner;
    }

    public String getoUGroupId() {
        return oUGroupId;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public String getRepositoryDefaultScheme() {
        return repositoryDefaultScheme;
    }

    public String getProjectGroupId() {
        return projectGroupId;
    }

    public String getProjectVersion() {
        return projectVersion;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public String getProjectDefaultBranch() {
        return projectDefaultBranch;
    }

    public String getOuAlias() {
        return ouAlias;
    }
}