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

package org.uberfire.ext.aut.client.widgets;

import org.jboss.errai.ui.client.local.spi.TranslationService;
import org.uberfire.client.mvp.UberElement;
import org.uberfire.ext.aut.api.LibraryInfo;
import org.uberfire.ext.aut.client.resources.i18n.NewAuthoringConstants;
import org.uberfire.mvp.ParameterizedCommand;

import javax.inject.Inject;

public class LibraryBreadCrumbToolbarPresenter {


    public interface View extends UberElement<LibraryBreadCrumbToolbarPresenter> {

        void addOrganizationUnit( String ou );

        void clearOrganizationUnits();

        void setOrganizationUnitSelected( String identifier );

        void setTeamDropdownLabel( String label );
    }

    @Inject
    private View view;

    private ParameterizedCommand<String> selectCommand;

    public void init( ParameterizedCommand<String> selectCommand, LibraryInfo libraryInfo ) {
        this.selectCommand = selectCommand;
        view.init( this );
        view.setTeamDropdownLabel( libraryInfo.getOuAlias() );
        clearOrganizationUnits();
        libraryInfo.getOrganizationUnits()
                .forEach( ou -> addOrganizationUnit( ou.getIdentifier() ) );
        setOrganizationUnitSelected( libraryInfo.getDefaultOrganizationUnit().getIdentifier() );
    }

    void selectOrganizationUnit( String value ) {
        selectCommand.execute( value );
    }

    private void addOrganizationUnit( String identifier ) {
        view.addOrganizationUnit( identifier );
    }

    private void clearOrganizationUnits() {
        view.clearOrganizationUnits();
    }

    private void setOrganizationUnitSelected( String identifier ) {
        view.setOrganizationUnitSelected( identifier );
    }

    public UberElement<LibraryBreadCrumbToolbarPresenter> getView() {
        return view;
    }
}
