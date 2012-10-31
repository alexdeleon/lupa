package com.lumata.lib.webscraper.extractor.internal.config;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import com.lumata.lib.webscraper.extractor.ContentExtractor;

public class ExtractorConfiguration {

	private Class<? extends ContentExtractor> extractorClass;
	private Map<String, String> constraints = new HashMap<String, String>();
	private Map<String, String> parameters;
	private int priority;

	public ExtractorConfiguration() {
		super();
	}

	public ExtractorConfiguration(Class<? extends ContentExtractor> extractorClass, Map<String, String> constraints,
			Map<String, String> parameters, byte priority, boolean needsFetch) {
		super();
		this.extractorClass = extractorClass;
		this.constraints = constraints;
		this.parameters = parameters;
		this.priority = priority;
	}

	public void setExtractorClass(Class<? extends ContentExtractor> extractorClass) {
		this.extractorClass = extractorClass;
	}

	public Class<? extends ContentExtractor> getExtractorClass() {
		return extractorClass;
	}

	
	public @Nonnull Map<String, String> getConstraints() {
		return constraints;
	}

	public void setConstraints(Map<String, String> constraints) {
		this.constraints = constraints;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

}
