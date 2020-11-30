package com.gsalgadoartola.libs.configuration;

import com.gsalgadoartola.libs.configuration.exceptions.PropertiesException;
import com.gsalgadoartola.libs.configuration.utils.KeyValue;

import java.io.*;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;

public class PersistentConfigManager extends ConfigManager {

	private static final String EQUALS = "=";
	private static final String COMMENT_PREFIX = "#";
	private static final String NEW_LINE = "\n";

	private static final int KEY_INDEX = 0;
	private static final int VALUE_INDEX = 1;
	private static final int NUMBER_OF_COMPLETE_PROPERTY_ELEMENTS = 2;
	private static final int FIRST_CHAR_INDEX = 0;

	private static final Object lock = new Object();
	private static PersistentConfigManager instance = null;

	private PersistentConfigManager() { }

	public static PersistentConfigManager getInstance() {
		synchronized (lock) {
			if (instance == null) {
				instance = new PersistentConfigManager();
			}
		}

		return instance;
	}

	public void setConfigsInfo(PersistentConfigInfo... configsInfo) throws PropertiesException {
		addConfigsInfos(configsInfo);
		loadPropertiesFromFiles();
	}

	public String getPath(String configName) throws PropertiesException {
		if (configs.containsKey(configName)) {
			PersistentConfigInfo configInfo = (PersistentConfigInfo) configs.get(configName);
			return configInfo.getPath();
		} else {
			throw new PropertiesException("There is no configuration with name: " + configName);
		}
	}

	@Override
	public synchronized void setProperties( String configName,
	                                        Map<String, String> map,
	                                        String changeMessage) throws PropertiesException {
		super.setProperties(configName, map, changeMessage);

		String messageToSet = getMessage(changeMessage);
		writeToFile(configName, messageToSet);

	}

	@Override
	public synchronized void updateExistingProperty(String configName, String key, String value, String changeMessage) throws PropertiesException {
		super.updateExistingProperty(configName, key, value, changeMessage);
		String messageToSet = getMessage(changeMessage);
		writeToFile(configName, messageToSet);
	}

	@Override
	public synchronized void addNewProperty(String configName, String key, String value, String changeMessage) throws PropertiesException {
		super.addNewProperty(configName, key, value, changeMessage);
		String messageToSet = getMessage(changeMessage);
		writeToFile(configName, messageToSet);
	}

	@Override
	public synchronized void deleteProperty(String configName, String key) throws PropertiesException {
		super.deleteProperty(configName, key);
		String messageToSet = getMessage("Delete property " + key);
		writeToFile(configName, messageToSet);
	}

	private synchronized void loadPropertiesFromFiles() throws PropertiesException {
		for (ConfigInfo configInfoUncasted : configs.values()) {
			PersistentConfigInfo configInfo = (PersistentConfigInfo) configInfoUncasted;

			Map<String, String> properties = loadPropertiesFromFile(configInfo.getPath());
			configInfo.setProperties(properties);
		}
	}

	private Map<String, String> loadPropertiesFromFile(String path) throws PropertiesException {

		Map<String, String> loadedProperties = new LinkedHashMap<>();

		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
			String currentLine;
			while ((currentLine = bufferedReader.readLine()) != null) {
				if (!currentLine.startsWith(COMMENT_PREFIX) && !currentLine.isEmpty()) {
					KeyValue<String, String> keyValue = getKeyValue(currentLine);
					String key = keyValue.getKey();
					String value = keyValue.getValue();
					loadedProperties.put(key, value);
				}
			}
		} catch (IOException e) {
			throw new PropertiesException("Error opening file: " + path, e);
		}

		return loadedProperties;
	}

	private KeyValue<String, String> getKeyValue(String line) throws PropertiesException {
		if (isPropertyLineWellFormed(line)) {
			String[] keyValue = line.split(EQUALS, 2);
			String key = keyValue[KEY_INDEX];
			String value = "";
			if (keyValue.length == NUMBER_OF_COMPLETE_PROPERTY_ELEMENTS) {
				value = keyValue[VALUE_INDEX];
			}
			return new KeyValue<>(key, value);
		} else {
			throw new PropertiesException("Malformed property line: " + line);
		}
	}

	private boolean isPropertyLineWellFormed(String line) {
		boolean isNotComment = !line.startsWith(COMMENT_PREFIX);
		boolean containsEquals = line.contains(EQUALS);
		boolean hasKey = line.charAt(FIRST_CHAR_INDEX) != EQUALS.charAt(FIRST_CHAR_INDEX);
		return isNotComment && containsEquals && hasKey;
	}

	private String getMessage(String changeMessage) throws PropertiesException {
		String messageToSet = "";
		if (changeMessage != null) {
			messageToSet = toUTF8(changeMessage);
		}
		return messageToSet;
	}

	private void writeToFile(String configName, String messageToSet) throws PropertiesException {
		if (configs.containsKey(configName)) {
			PersistentConfigInfo configInfo = (PersistentConfigInfo) configs.get(configName);
			ReadWriteLock lock = configInfo.getLock();
			lock.readLock().lock();
			try {
				Map<String, String> properties = configInfo.getProperties();
				writeToFile(properties, configInfo.getPath(), messageToSet);
			} finally {
				lock.readLock().unlock();
			}
		} else {
			throw new PropertiesException("There is no configuration with name: " + configName);
		}
	}

	private void writeToFile(Map<String, String> properties, String path, String message) {
		String content = getFileContent(properties, message);
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
			bw.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getFileContent(Map<String, String> properties, String message) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(COMMENT_PREFIX).append(message).append(NEW_LINE);
		stringBuilder.append(COMMENT_PREFIX).append(new Date().toString()).append(NEW_LINE);
		stringBuilder.append(NEW_LINE);

		for(Map.Entry<String, String> entry : properties.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			stringBuilder.append(key).append(EQUALS).append(value).append(NEW_LINE);
		}

		return stringBuilder.toString();
	}
}
