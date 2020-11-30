package com.gsalgadoartola.libs.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigManager {

    public static Map<String, Map<String, String>> getConfigs() {
        Map<String, String> config = new HashMap<>(2);
        config.put("key1", "value1");
        config.put("key2", "value2");

        Map<String, Map<String, String>> configs = new HashMap<>(1);
        configs.put("example.properties", config);

        return configs;
    }

    public static Map<Manager, List<String>> getManagers(Manager manager) {
        List<String> configsManager = new ArrayList<>(1);
        configsManager.add("example.properties");

        Map<Manager, List<String>> managers = new HashMap<>(1);
        managers.put(manager, configsManager);

        return managers;
    }

}
