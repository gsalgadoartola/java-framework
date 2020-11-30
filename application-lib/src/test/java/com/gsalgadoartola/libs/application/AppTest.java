package com.gsalgadoartola.libs.application;

import org.junit.Test;
import com.gsalgadoartola.libs.application.appmanagers.ExternalErrorManager;
import com.gsalgadoartola.libs.application.appmanagers.InternalErrorAppManager;
import com.gsalgadoartola.libs.application.appmanagers.SuccessfulAppManager;
import com.gsalgadoartola.libs.application.exceptions.AppManagerException;
import com.gsalgadoartola.libs.application.exceptions.ManagerException;

import java.util.HashMap;
import java.util.Map;

public class AppTest {

    private final String CONFIG_NAME = "example.properties";

    private Map<String, String> getUpdateConfig() {
        Map<String, String> config = new HashMap<>(2);
        config.put("key1", "value3");
        config.put("key2", "value4");
        return config;
    }

    @Test
    public void start() throws Exception {
        SuccessfulAppManager successfulAppManager = new SuccessfulAppManager();
        successfulAppManager.start();
    }

    @Test
    public void update() throws Exception {
        SuccessfulAppManager successfulAppManager = new SuccessfulAppManager();
        successfulAppManager.start();
        successfulAppManager.update(CONFIG_NAME, getUpdateConfig());
    }

    @Test
    public void stop() throws Exception {
        SuccessfulAppManager successfulAppManager = new SuccessfulAppManager();
        successfulAppManager.stop();
    }

    @Test(expected = AppManagerException.class)
    public void startAppManagerException() throws Exception {
        InternalErrorAppManager internalErrorAppManager = new InternalErrorAppManager();
        internalErrorAppManager.start();
    }

    @Test(expected = AppManagerException.class)
    public void updateAppManagerException() throws Exception {
        InternalErrorAppManager internalErrorAppManager = new InternalErrorAppManager();
        internalErrorAppManager.start();
        internalErrorAppManager.update(CONFIG_NAME, getUpdateConfig());
    }

    @Test(expected = AppManagerException.class)
    public void stopAppManagerException() throws Exception {
        InternalErrorAppManager internalErrorAppManager = new InternalErrorAppManager();
        internalErrorAppManager.start();
        internalErrorAppManager.stop();
    }

    @Test(expected = ManagerException.class)
    public void startManagerException() throws Exception {
        ExternalErrorManager externalErrorManager = new ExternalErrorManager();
        externalErrorManager.start();
    }

    @Test(expected = ManagerException.class)
    public void updateManagerException() throws Exception {
        ExternalErrorManager externalErrorManager = new ExternalErrorManager();
        externalErrorManager.start();
        externalErrorManager.update(CONFIG_NAME, getUpdateConfig());
    }

    @Test(expected = ManagerException.class)
    public void stopManagerException() throws Exception {
        ExternalErrorManager externalErrorManager = new ExternalErrorManager();
        externalErrorManager.start();
        externalErrorManager.stop();
    }

}