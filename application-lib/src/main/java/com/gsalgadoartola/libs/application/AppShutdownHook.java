package com.gsalgadoartola.libs.application;

import com.gsalgadoartola.libs.application.exceptions.AppManagerException;
import com.gsalgadoartola.libs.application.exceptions.ManagerException;

public class AppShutdownHook implements Runnable {

    private AbstractAppManager appManager;

    public AppShutdownHook(AbstractAppManager appManager) {
        this.appManager = appManager;
    }

    @Override
    public void run() {
        try {
            appManager.stop();
        } catch (ManagerException | AppManagerException e) {
            appManager.logShutdownHookException(e);
        }
    }
}
