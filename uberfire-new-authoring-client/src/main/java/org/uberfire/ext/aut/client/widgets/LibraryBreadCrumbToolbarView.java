package org.uberfire.ext.aut.client.widgets;

import com.google.inject.Inject;
import org.jboss.errai.common.client.dom.DOMUtil;
import org.jboss.errai.common.client.dom.Document;
import org.jboss.errai.common.client.dom.Option;
import org.jboss.errai.common.client.dom.Select;
import org.jboss.errai.ui.client.local.api.IsElement;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

@Templated
public class LibraryBreadCrumbToolbarView implements LibraryBreadCrumbToolbarPresenter.View, IsElement {

    private LibraryBreadCrumbToolbarPresenter presenter;

    @DataField
    @Inject
    Select teamDropdown;

    @Inject
    Document document;

    @Override
    public void init( LibraryBreadCrumbToolbarPresenter presenter ) {
        this.presenter = presenter;
        teamDropdown.setOnchange( event -> {
            presenter.selectOrganizationUnit( teamDropdown.getValue() );
        } );
    }

    @Override
    public void addOrganizationUnit( String ou ) {
        teamDropdown.add( createOption( ou ) );
    }

    @Override
    public void clearOrganizationUnits() {
        DOMUtil.removeAllChildren( teamDropdown );
    }


    private Option createOption( String ou ) {
        Option option = ( Option ) document.createElement( "option" );
        option.setText( ou );
        return option;
    }
}