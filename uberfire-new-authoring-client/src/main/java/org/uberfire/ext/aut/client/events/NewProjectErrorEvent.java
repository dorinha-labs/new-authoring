package org.uberfire.ext.aut.client.events;

import org.uberfire.workbench.events.UberFireEvent;

public class NewProjectErrorEvent implements UberFireEvent {

    private Throwable cause;
    private String message;
    private Object context;

    public NewProjectErrorEvent(Object context) {

        this.context = context;
    }

    public NewProjectErrorEvent(Object context, Throwable cause) {
        this.context = context;
        this.cause = cause;
    }

    public NewProjectErrorEvent(Object context, String message) {
        this.context = context;
        this.message = message;
    }

    public Throwable getCause() {
        return cause;
    }

    public String getMessage() {
        return message;
    }

    public Object getContext() {
        return context;
    }

    @Override
    public String toString() {
        return "ErrorEvent [context=" + getContext() + "]";
    }

}

