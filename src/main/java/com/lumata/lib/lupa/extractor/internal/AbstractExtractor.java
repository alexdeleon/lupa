/*
 * 2011 copyright Buongiorno SpA
 */
package com.lumata.lib.lupa.extractor.internal;

import java.util.Collections;
import java.util.Map;

import com.lumata.lib.lupa.content.WebContent;
import com.lumata.lib.lupa.extractor.ContentExtractor;

/**
 * @author Alexander De Leon - alexander.leon@buongiorno.com
 * 
 */
public abstract class AbstractExtractor<E extends WebContent> implements ContentExtractor<E> {

	private Map<String, String> configuration;

	@Override
	public final void setConfiguration(Map<String, String> config) {
		this.configuration = config;
	}

	public final Map<String, String> getConfiguration() {
		if (configuration == null) {
			return Collections.emptyMap();
		}
		return configuration;
	}

	protected String getConfigValue(String key) {
		return getConfigValue(key, null);
	}

	protected String getConfigValue(String key, String defaultValue) {
		String value = getConfiguration().get(key);
		return value != null ? value : defaultValue;
	}

	protected Boolean getConfigBooleanValue(String key) {
		return getConfigBooleanValue(key, null);
	}

	protected Boolean getConfigBooleanValue(String key, Boolean defaultValue) {
		String value = getConfigValue(key);
		if (value == null) {
			return defaultValue;
		}
		try {
			return Boolean.valueOf(value);
		} catch (Exception e) {
			// invalid value in configuration
			return defaultValue;
		}
	}
}
