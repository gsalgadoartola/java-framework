package com.gsalgadoartola.libs.configuration;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;

abstract class ConfigInfo {

	private ReadWriteLock lock;
	private String configName;

	protected Map<String, String> properties;

	ConfigInfo(String configName) {
		this.configName = configName;
	}

	ReadWriteLock getLock() {
		return lock;
	}

	String getConfigName() {
		return configName;
	}

	void setLock(ReadWriteLock lock) {
		this.lock = lock;
	}

	String getProperty(String key) {
		return properties.get(key);
	}

	Map<String, String> getProperties() {
		return properties;
	}

	void setProperties(Map<String, String> properties) {
		this.properties = new LinkedHashMap<>(properties);
	}

}
