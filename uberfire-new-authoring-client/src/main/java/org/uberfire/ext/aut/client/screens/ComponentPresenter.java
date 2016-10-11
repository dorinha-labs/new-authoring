package org.uberfire.ext.aut.client.screens;

import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.uberfire.client.annotations.WorkbenchPartTitle;
import org.uberfire.client.annotations.WorkbenchPartView;
import org.uberfire.client.annotations.WorkbenchScreen;
import org.uberfire.client.mvp.UberElement;
import org.uberfire.ext.aut.api.SampleServiceAPI;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@WorkbenchScreen( identifier = "ComponentPresenter1" )
@Dependent
public class ComponentPresenter {

    public interface View extends UberElement<ComponentPresenter> {

        void setValue( String value );
    }

    @Inject
    private Caller<SampleServiceAPI> myService;

    @Inject
    private View view;

    @AfterInitialization
    private void init() {
        myService.call( new RemoteCallback<String>() {
            @Override
            public void callback( String response ) {
                view.setValue( response );
            }
        } ).hello();
    }

    @WorkbenchPartTitle
    public String getTitle() {
        return "Remote Greetings";
    }

    @WorkbenchPartView
    public UberElement<ComponentPresenter> getView() {
        return view;
    }

}