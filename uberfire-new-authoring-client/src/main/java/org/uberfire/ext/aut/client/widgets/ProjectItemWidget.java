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
    Div projectListItemHeading;

    @Inject
    @DataField
    Div projectListItemKebab;

    @Inject
    KebabWidget kebabWidget;

    public void init( String project, Command details, Command select ) {
        projectName.setTextContent( project );
        projectName.setOnclick( e -> {
            e.stopImmediatePropagation();
            select.execute();
        } );
        projectListItemHeading.setOnclick( e -> details.execute() );
        kebabWidget.init( details, select );
        projectListItemKebab.appendChild( kebabWidget.getElement() );
    }
}
