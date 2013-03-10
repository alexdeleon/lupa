/*
 * 2011 copyright Buongiorno SpA
 */
package com.lumata.lib.lupa.extractor.internal.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.lumata.lib.lupa.extractor.ContentExtractor;

/**
 * @author Alexander De Leon - alexander.leon@buongiorno.com
 * 
 */
public abstract class AbstractExtractorConfigurationModule implements ExtractorConfigurationModule {

	private final List<ExtractorConfiguration> configurations = new ArrayList<ExtractorConfiguration>();

	@Override
	public ExtractorConfiguration[] configurations() {
		configure();
		return configurations.toArray(new ExtractorConfiguration[configurations.size()]);
	}

	protected abstract void configure();

	protected ConfigurationBuilder when(Map<String, String> constraints) {
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.configuration.setConstraints(constraints);
		return builder;
	}

	/* ----------------------- inner classes -- */
	protected class ConfigurationBuilder {
		private final ExtractorConfiguration configuration = new ExtractorConfiguration();

		public ConfigurationParametarizer use(Class<? extends ContentExtractor<?>> c) {
			configuration.setExtractorClass(c);
			configurations.add(configuration);
			return new ConfigurationParametarizer(configuration);
		}

	}

	protected class ConfigurationParametarizer {
		private final ExtractorConfiguration configuration;

		public ConfigurationParametarizer(ExtractorConfiguration configuration) {
			this.configuration = configuration;
		}

		public void withParameters(Map<String, String> parameters) {
			configuration.setParameters(parameters);
		}

		public void withPriority(int priority) {
			configuration.setPriority(priority);
		}
	}

}
