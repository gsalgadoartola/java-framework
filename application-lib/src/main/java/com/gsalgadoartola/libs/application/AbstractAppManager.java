package com.gsalgadoartola.libs.application;

import com.gsalgadoartola.libs.application.exceptions.AppManagerException;
import com.gsalgadoartola.libs.application.exceptions.ManagerException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractAppManager {

    private Map<String, Manager> configRegistry;

    protected AbstractAppManager() {
        this.configRegistry = new HashMap<>();
    }

    protected abstract AbstractAppManager getAppManager() throws AppManagerException;

    private Map<String, Map<String, String>> buildConfigs(Manager manager, List<String> configsName,
                                                          Map<String, Map<String, String>> allConfigs) {
        Map<String, Map<String, String>> configs = new HashMap<>();
        for (String configName : configsName) {
            configs.put(configName, allConfigs.get(configName));
            configRegistry.put(configName, manager);
        }
        return configs;
    }

    protected abstract Map<String, Map<String, String>> getConfigs() throws AppManagerException;

    protected abstract Map<Manager, List<String>> getManagers() throws AppManagerException;

    public void start() throws AppManagerException, ManagerException {
        Runtime.getRuntime().addShutdownHook(new Thread(new AppShutdownHook(getAppManager())));

        Map<String, Map<String, String>> allConfigs = getConfigs();

        startTracer();

        Map<Manager, List<String>> managers = getManagers();
        for (Map.Entry<Manager, List<String>> entry : managers.entrySet()) {
            Manager manager = entry.getKey();
            List<String> configsName = entry.getValue();
            Map<String, Map<String, String>> managerConfigs = buildConfigs(manager, configsName, allConfigs);
            manager.start(managerConfigs);
        }
    }

    public void update(String configName, Map<String, String> config) throws ManagerException {
        Manager manager = configRegistry.get(configName);
        if(manager != null) {
            manager.update(configName, config);
        }
    }

    public void stop() throws AppManagerException, ManagerException {
        for (Manager manager : getManagers().keySet()) {
            manager.stop();
        }
    }

    protected abstract void startTracer() throws AppManagerException;

    public abstract void logShutdownHookException(Exception e);

}