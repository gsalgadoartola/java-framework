package com.gsalgadoartola.libs.application.managers;

import com.gsalgadoartola.libs.application.Manager;
import com.gsalgadoartola.libs.application.exceptions.ManagerException;

import java.util.Map;

public class ErrorManager implements Manager {

    @Override
    public void start(Map<String, Map<String, String>> configs) throws ManagerException {
        throw new ManagerException("Error starting manager");
    }

    @Override
    public void update(String configName, Map<String, String> config) throws ManagerException {
        throw new ManagerException("Error updating manager");
    }

    @Override
    public void stop() throws ManagerException {
        throw new ManagerException("Error stopping manager");
    }

}
