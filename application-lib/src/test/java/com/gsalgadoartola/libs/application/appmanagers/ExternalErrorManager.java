package com.gsalgadoartola.libs.application.appmanagers;

import com.gsalgadoartola.libs.application.AbstractAppManager;
import com.gsalgadoartola.libs.application.Manager;
import com.gsalgadoartola.libs.application.ConfigManager;
import com.gsalgadoartola.libs.application.exceptions.AppManagerException;
import com.gsalgadoartola.libs.application.managers.ErrorManager;

import java.util.List;
import java.util.Map;

public class ExternalErrorManager extends AbstractAppManager {

    private static ExternalErrorManager instance = new ExternalErrorManager();

    @Override
    protected AbstractAppManager getAppManager() throws AppManagerException {
        return instance;
    }

    @Override
    protected Map<String, Map<String, String>> getConfigs() throws AppManagerException {
        return ConfigManager.getConfigs();
    }

    @Override
    protected Map<Manager, List<String>> getManagers() throws AppManagerException {
        return ConfigManager.getManagers(new ErrorManager());
    }

    @Override
    protected void startTracer() throws AppManagerException {

    }

    @Override
    public void logShutdownHookException(Exception e) {

    }

}
