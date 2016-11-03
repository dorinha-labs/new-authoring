package org.uberfire.ext.aut.client.widgets;

import org.jboss.errai.common.client.dom.Anchor;
import org.jboss.errai.common.client.dom.Div;
import org.jboss.errai.ui.client.local.api.IsElement;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.uberfire.mvp.Command;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
@Templated
public class ProjectItemWidget implements IsElement {


    @Inject
    @DataField
    Anchor projectName;

    @Inject
    @DataField
    Div projectListItem;

    @Inject
    @DataField
    Div projectListItemKebab;

    @Inject
    @DataField
    Div projectListItemText;

//    @Inject
//    KebabWidget kebabWidget;

    public void init( String projectName, Command details, Command select ) {
        this.projectName.setTextContent( projectName );
        this.projectName.setOnclick( e -> {
            e.stopImmediatePropagation();
            select.execute();
        } );
        projectListItem.setOnclick( e -> details.execute() );
//        kebabWidget.init( details, select );
//        projectListItemKebab.appendChild( kebabWidget.getElement() );
    }
}
