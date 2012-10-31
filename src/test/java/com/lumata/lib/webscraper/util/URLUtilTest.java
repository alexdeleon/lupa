/*
 * 2012 copyright Lumata
 */
package com.lumata.lib.webscraper.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

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
