/**
 * Copyright (c) 2013 Lumata
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.lumata.lib.lupa.extractor.internal;

import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.google.common.base.Objects;
import com.google.common.net.MediaType;
import com.lumata.lib.lupa.ReadableResource;
import com.lumata.lib.lupa.content.WebContent;
import com.lumata.lib.lupa.extractor.ContentExtractor;

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
		int priorityDifference = o.priority - priority;
		if (priorityDifference != 0) {
			return priorityDifference;
		}
		return this.equals(o) ? 0 : 1;
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

	@Override
	public int hashCode() {
		return Objects.hashCode(contentExtractor, constraints);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof ContentExtractorMatcher)) {
			return false;
		}
		ContentExtractorMatcher<?> that = (ContentExtractorMatcher<?>) obj;
		return Objects.equal(this.contentExtractor, that.contentExtractor)
				&& Objects.equal(this.constraints, that.constraints) && Objects.equal(this.priority, that.priority);
	}
}