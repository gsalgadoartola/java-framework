package com.gsalgadoartola.libs.configuration;

import com.gsalgadoartola.libs.configuration.exceptions.PropertiesException;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

abstract class ConfigManager {

    protected Map<String, ConfigInfo> configs = new HashMap<>();

    public String getProperty(String configName, String key) throws PropertiesException {
	    if (configs.containsKey(configName)) {
		    ConfigInfo configInfo = configs.get(configName);
		    ReadWriteLock lock = configInfo.getLock();
	        lock.readLock().lock();
	        try {
	            String value = configInfo.getProperty(key);
	            if (value == null) {
	                throw new PropertiesException("Not found key " + key + " on configuration: " + configName);
	            }
	            return value.trim();
	        } finally {
	            lock.readLock().unlock();
	        }
	    } else {
		    throw new PropertiesException("There is no configuration with name: " + configName);
	    }
    }

    public Map<String, String> getProperties(String configName) throws PropertiesException {
	    if (configs.containsKey(configName)) {
		    ConfigInfo configInfo = configs.get(configName);
		    ReadWriteLock lock = configInfo.getLock();
	        lock.readLock().lock();
	        try {
		        return trimAll(configInfo.getProperties());
	        } finally {
	            lock.readLock().unlock();
	        }
	    } else {
		    throw new PropertiesException("There is no configuration with name: " + configName);
	    }
    }

	public Map<String, String> getAllProperties() throws PropertiesException {
		Map<String, String> allProperties = new LinkedHashMap<>();
		for (String configName : configs.keySet()) {
			allProperties.putAll(getProperties(configName));
		}

		return allProperties;
	}

	public boolean propertiesChanged(String configName, Map<String, String> map) throws PropertiesException {
		if (configs.containsKey(configName)) {
			ConfigInfo configInfo = configs.get(configName);
			ReadWriteLock lock = configInfo.getLock();
			lock.readLock().lock();
			try {
				Map<String, String> properties = configInfo.getProperties();
				for (Map.Entry<String, String> entry : map.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue();
					if (properties.containsKey(key)) {
						String propValue = properties.get(key);
						if (!propValue.equals(value)) {
							return true;
						}
					}
				}
				return false;
			} finally {
				lock.readLock().unlock();
			}
		} else {
			throw new PropertiesException("There is no configuration with name: " + configName);
		}
	}

    public synchronized void setProperties( String configName,
											Map<String, String> map,
											String changeMessage) throws PropertiesException {
	    if(map == null) {
		    throw new PropertiesException("Properties map is null");
	    }
	    updateChangedProperties(configName, map);
    }

    public synchronized void updateExistingProperty(String configName, String key, String value, String changeMessage) throws PropertiesException {
		if (configs.containsKey(configName)) {
			ConfigInfo configInfo = configs.get(configName);
			ReadWriteLock lock = configInfo.getLock();
			lock.writeLock().lock();
			try {
				Map<String, String> properties = configInfo.getProperties();
				String property = properties.get(key);
				if (property == null) {
					throw new PropertiesException("Not found key " + key + " at configuration.");
				}
				if (!property.equals(value)) {
					properties.put(key, toUTF8(value));
				}
				configInfo.setProperties(properties);
			} finally {
				lock.writeLock().unlock();
			}
		} else {
			throw new PropertiesException("There is no configuration with name: " + configName);
		}
	}

    public synchronized void addNewProperty(String configName, String key, String value, String changeMessage) throws PropertiesException {
		if (configs.containsKey(configName)) {
			ConfigInfo configInfo = configs.get(configName);
			ReadWriteLock lock = configInfo.getLock();
			lock.writeLock().lock();
			try {
				Map<String, String> properties = configInfo.getProperties();
				properties.put(key, toUTF8(value));
				configInfo.setProperties(properties);
			} finally {
				lock.writeLock().unlock();
			}
		} else {
			throw new PropertiesException("There is no configuration with name: " + configName);
		}
	}

	public synchronized void deleteProperty(String configName, String key) throws PropertiesException {
		if (configs.containsKey(configName)) {
			ConfigInfo configInfo = configs.get(configName);
			ReadWriteLock lock = configInfo.getLock();
			lock.writeLock().lock();
			try {
				Map<String, String> properties = configInfo.getProperties();
				String property = properties.get(key);
				if (property == null) {
					throw new PropertiesException("Not found key " + key + " at configuration.");
				}

				properties.remove(key);
				configInfo.setProperties(properties);
			} finally {
				lock.writeLock().unlock();
			}
		} else {
			throw new PropertiesException("There is no configuration with name: " + configName);
		}
	}

	protected void addConfigsInfos(ConfigInfo... configsInfo) {
		for (ConfigInfo configInfo : configsInfo) {
			configInfo.setLock(new ReentrantReadWriteLock());
			configs.put(configInfo.getConfigName(), configInfo);
		}
	}

	protected String toUTF8(String value) throws PropertiesException {
		try {
			return new String(value.getBytes("UTF-8"));
		} catch (Exception ex) {
			throw new PropertiesException("Could not parse to UTF-8");
		}
	}

	private void updateChangedProperties(String configName, Map<String, String> map) throws PropertiesException {
		if (configs.containsKey(configName)) {
			ConfigInfo configInfo = configs.get(configName);
			ReadWriteLock lock = configInfo.getLock();
			lock.writeLock().lock();
			try {
				Map<String, String> properties = configInfo.getProperties();
				for (Map.Entry<String, String> entry : map.entrySet()) {
					String property = properties.get(entry.getKey());
					if (property == null) {
						throw new PropertiesException("Not found key " + entry.getKey() + " at configuration.");
					}

					if (!property.equals(entry.getValue())) {
						properties.put(entry.getKey(), toUTF8(entry.getValue()));
					}
				}
				configInfo.setProperties(properties);
			} finally {
				lock.writeLock().unlock();
			}
		} else {
			throw new PropertiesException("There is no configuration with name: " + configName);
		}
	}

	private Map<String, String> trimAll(Map<String, String> properties) {
		Map<String, String> trimmedProperties = new LinkedHashMap<>();
		for (Map.Entry<String, String> entry : properties.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			trimmedProperties.put(key, value.trim());
		}

		return trimmedProperties;
	}
}