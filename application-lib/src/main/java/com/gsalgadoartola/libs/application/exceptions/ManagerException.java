package com.gsalgadoartola.libs.application.exceptions;

public class ManagerException extends Exception {

    public ManagerException(String message) {
        super(message);
    }

    public ManagerException(String message, Throwable t) {
        super(message, t);
    }

}
