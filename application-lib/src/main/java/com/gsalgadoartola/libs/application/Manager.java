package com.gsalgadoartola.libs.application;

import com.gsalgadoartola.libs.application.exceptions.ManagerException;

import java.util.Map;

public interface Manager {

    void start(Map<String, Map<String, String>> configs) throws ManagerException;

    void update(String configName, Map<String, String> config) throws ManagerException;

    void stop() throws ManagerException;

}