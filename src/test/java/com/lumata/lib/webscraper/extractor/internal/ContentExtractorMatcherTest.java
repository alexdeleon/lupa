/*
 * 2012 copyright Lumata
 */
package com.lumata.lib.webscraper.extractor.internal;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.lumata.lib.webscraper.extractor.internal.ContentExtractorMatcher;

/**
 * @author Alexander De Leon - alexander.leon@lumatagroup.com
 * 
 */
public class ContentExtractorMatcherTest {

	@Test
	public void testUrlIsMoreSpecifictThanContentType() {
		ContentExtractorMatcher m1 = new ContentExtractorMatcher(null, map("type", "text/plain"), 0);
		ContentExtractorMatcher m2 = new ContentExtractorMatcher(null, map("url", "http://example.com"), 0);

		assertTrue(isGreaterThan(m1, m2));

	}

	@Test
	public void testUrlAndContentTypeIsMoreSpecifictThanJustUrl() {
		ContentExtractorMatcher m1 = new ContentExtractorMatcher(null, map("type", "text/plain", "url",
				"http://example.com"), 0);
		ContentExtractorMatcher m2 = new ContentExtractorMatcher(null, map("url", "http://example.com"), 0);

		assertTrue(isGreaterThan(m2, m1));

	}

	@Test
	public void testBothUrlAndSamePriorityIsEqual() {
		ContentExtractorMatcher m1 = new ContentExtractorMatcher(null, map("url", "http://example1.com"), 0);
		ContentExtractorMatcher m2 = new ContentExtractorMatcher(null, map("url", "http://example2.com"), 0);

		assertTrue(isEqual(m2, m1));

	}

	@Test
	public void testPriority() {
		ContentExtractorMatcher m1 = new ContentExtractorMatcher(null, map("url", "http://example1.com"), 0);
		ContentExtractorMatcher m2 = new ContentExtractorMatcher(null, map("url", "http://example2.com"), 1);

		assertTrue(isGreaterThan(m1, m2));

	}

	private boolean isEqual(ContentExtractorMatcher m2, ContentExtractorMatcher m1) {
		return m1.compareTo(m2) == 0;
	}

	private boolean isGreaterThan(ContentExtractorMatcher m1, ContentExtractorMatcher m2) {
		return m1.compareTo(m2) > 0;
	}

	private Map<String, String> map(String... keyAndValues) {
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < keyAndValues.length; i += 2) {
			map.put(keyAndValues[i], keyAndValues[i + 1]);
		}
		return map;
	}
}
