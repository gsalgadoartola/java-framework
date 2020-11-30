package com.gsalgadoartola.libs.application.appmanagers;

import com.gsalgadoartola.libs.application.AbstractAppManager;
import com.gsalgadoartola.libs.application.Manager;
import com.gsalgadoartola.libs.application.exceptions.AppManagerException;

import java.util.List;
import java.util.Map;

public class InternalErrorAppManager extends AbstractAppManager {

    private static InternalErrorAppManager instance = new InternalErrorAppManager();

    @Override
    protected AbstractAppManager getAppManager() throws AppManagerException {
        return instance;
    }

    @Override
    protected Map<String, Map<String, String>> getConfigs() throws AppManagerException {
        throw new AppManagerException("Error getting the configs");
    }

    @Override
    protected Map<Manager, List<String>> getManagers() throws AppManagerException {
        throw new AppManagerException("Error getting the managers");
    }

    @Override
    protected void startTracer() throws AppManagerException {

    }

    @Override
    public void logShutdownHookException(Exception e) {

    }

}