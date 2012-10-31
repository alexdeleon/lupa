/*
 * 2012 copyright Lumata
 */
package com.lumata.lib.webscraper.extractor.internal;

import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.google.common.net.MediaType;
import com.lumata.lib.webscraper.ReadableResource;
import com.lumata.lib.webscraper.content.WebContent;
import com.lumata.lib.webscraper.extractor.ContentExtractor;

class ContentExtractorMatcher<E extends WebContent> implements
		Comparable<ContentExtractorMatcher<? extends WebContent>> {

	private final ContentExtractor<E> contentExtractor;
	Map<String, String> constraints;
	int priority;
	static final String CONTENT_TYPE = "type";
	static final String URL = "url";

	ContentExtractorMatcher(ContentExtractor<E> contentExtractor, Map<String, String> constraints, int priority) {
		this.contentExtractor = contentExtractor;
		this.constraints = constraints;
		this.priority = priority;
	}

	/**
	 * @return the contentExtractor
	 */
	public ContentExtractor<E> getContentExtractor() {
		return contentExtractor;
	}

	public boolean isCaplableOfExtracting(Class<? extends WebContent> webContentType) {
		return contentExtractor.getExtractableWebContentType().isAssignableFrom(webContentType);
	}

	boolean matches(ReadableResource res) {
		if (MapUtils.isEmpty(constraints)) {
			return true;
		}

		return matchContentType(res) && matchUrl(res);
	}

	private boolean matchUrl(ReadableResource res) {
		// hasConstraint(url) -> matches(constraint(url), url)
		return !constraints.containsKey(URL) || res.getUrl().toString().matches(constraints.get(URL));
	}

	private boolean matchContentType(ReadableResource res) {
		// hasConstraint(type) -> hasContentType() ^ is(type, constraint(type))
		return !constraints.containsKey(CONTENT_TYPE)
				|| (res.getContentType().isPresent() && res.getContentType().get()
						.is(MediaType.parse(constraints.get(CONTENT_TYPE))));
	}

	@Override
	public int compareTo(ContentExtractorMatcher<? extends WebContent> o) {
		int weightDifference = getWeigth(constraints) - getWeigth(o.constraints);
		if (weightDifference != 0) {
			return weightDifference;
		}
		return o.priority - priority;

	}

	private int getWeigth(Map<String, String> constraints) {
		int weight = 0;
		if (constraints.containsKey(CONTENT_TYPE)) {
			weight -= 1;
		}
		if (constraints.containsKey(URL)) {
			weight -= 2;
		}
		return weight;
	}
}