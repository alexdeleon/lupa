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
package com.lumata.lib.lupa.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

import com.lumata.lib.lupa.util.URLUtil;

/**
 * @author Alexander De Leon - alexander.leon@lumatagroup.com
 * 
 */
public class URLUtilTest {

	@Test
	public void testBuildRelativeURL() throws MalformedURLException {
		assertEquals(new URL("http://example.com/a"), URLUtil.getAbsoluteUrl(new URL("http://example.com"), "/a"));
		assertEquals(new URL("http://example.com/a"), URLUtil.getAbsoluteUrl(new URL("http://example.com/"), "/a"));
		assertEquals(new URL("http://example.com/a"), URLUtil.getAbsoluteUrl(new URL("http://example.com/"), "a"));
		assertEquals(new URL("http://example.com/a"), URLUtil.getAbsoluteUrl(new URL("http://example.com"), "a"));
		assertEquals(new URL("http://example.com/a"), URLUtil.getAbsoluteUrl(new URL("http://example.com/b"), "/a"));
		assertEquals(new URL("http://example.com/a"), URLUtil.getAbsoluteUrl(new URL("http://example.com/b"), "a"));
		assertEquals(new URL("http://example.com/b/a"), URLUtil.getAbsoluteUrl(new URL("http://example.com/b/"), "a"));
		assertEquals(new URL("http://example2.com/a"),
				URLUtil.getAbsoluteUrl(new URL("http://example.com/"), "http://example2.com/a"));
	}

	@Test
	public void testIsAbsolute() {
		assertTrue(URLUtil.isAbsolute("http://example.com"));
		assertTrue(URLUtil.isAbsolute("https://example.com"));
	}

}
