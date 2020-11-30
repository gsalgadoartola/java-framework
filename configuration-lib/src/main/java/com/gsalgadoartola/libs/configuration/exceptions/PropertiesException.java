package com.gsalgadoartola.libs.configuration.exceptions;

public class PropertiesException extends Exception {

    public PropertiesException(String message) {
        super(message);
    }

    public PropertiesException(String message, Throwable t){
        super(message, t);
    }

}