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

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.lumata.lib.lupa.extractor.internal.ContentExtractorMatcher;

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
	public void testPriority() {
		ContentExtractorMatcher m1 = new ContentExtractorMatcher(null, map("url", "http://example1.com"), 0);
		ContentExtractorMatcher m2 = new ContentExtractorMatcher(null, map("url", "http://example2.com"), 1);

		assertTrue(isGreaterThan(m1, m2));

	}

	@Test
	public void testThatIsConsistentWithEquals() {
		ContentExtractorMatcher m1 = new ContentExtractorMatcher(null, map("url", "http://example1.com"), 0);
		ContentExtractorMatcher m2 = new ContentExtractorMatcher(null, map("url", "http://example1.com"), 0);

		assertTrue(isEqual(m1, m2));

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
