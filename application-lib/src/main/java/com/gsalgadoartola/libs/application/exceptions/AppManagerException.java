package com.gsalgadoartola.libs.application.exceptions;

public class AppManagerException extends Exception {

    public AppManagerException(String message) {
        super(message);
    }

    public AppManagerException(String message, Throwable t) {
        super(message, t);
    }

}
