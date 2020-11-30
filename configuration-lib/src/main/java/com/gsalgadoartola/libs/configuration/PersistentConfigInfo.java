package com.gsalgadoartola.libs.configuration;


import java.util.LinkedHashMap;

public class PersistentConfigInfo extends ConfigInfo {

	private String path;

	public PersistentConfigInfo(String configName, String path) {
		super(configName);
		this.path = path;
		this.properties = new LinkedHashMap<>();
	}

	String getPath() {
		return path;
	}

}
