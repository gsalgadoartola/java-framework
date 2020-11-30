package com.gsalgadoartola.libs.configuration;

public class NotPersistentConfigManager extends ConfigManager{

	private static final Object lock = new Object();
	private static NotPersistentConfigManager instance = null;

	private NotPersistentConfigManager() { }

	public static NotPersistentConfigManager getInstance() {
		synchronized (lock) {
			if (instance == null) {
				instance = new NotPersistentConfigManager();
			}
		}

		return instance;
	}

	public void setConfigsInfo(NotPersistentConfigInfo... configsInfo) {
		super.addConfigsInfos(configsInfo);
	}
}
