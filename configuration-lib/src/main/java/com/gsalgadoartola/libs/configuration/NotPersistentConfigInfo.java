package com.gsalgadoartola.libs.configuration;

import java.util.LinkedHashMap;
import java.util.Map;

public class NotPersistentConfigInfo extends ConfigInfo {

	public NotPersistentConfigInfo(String configName, Map<String, String> properties) {
		super(configName);
		this.properties = new LinkedHashMap<>(properties);
	}

}
