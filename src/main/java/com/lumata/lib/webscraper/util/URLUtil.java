/*
 * 2012 copyright Lumata
 */
package com.lumata.lib.webscraper.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

/**
 * @author Alexander De Leon - alexander.leon@lumatagroup.com
 * 
 */
public class URLUtil {

	public static String asString(URL url) {
		return url.toString();
	}

	public static String[] asString(URL[] urls) {
		if (urls == null) {
			return null;
		}
		return asString(Arrays.asList(urls)).toArray(new String[] {});
	}

	public static Collection<String> asString(Collection<URL> urls) {
		if (urls == null) {
			return null;
		}
		Collection<String> ret = Collections2.transform(urls, new Function<URL, String>() {
			@Override
			public String apply(URL input) {
				return asString(input);
			}
		});
		return ret;
	}

	public static URL getAbsoluteUrl(URL baseUrl, String relativeUrl) throws MalformedURLException {
		if (isAbsolute(relativeUrl)) {
			return new URL(relativeUrl);
		}
		return new URL(baseUrl, relativeUrl);
	}

	public static boolean isAbsolute(String url) {
		return url.matches("^.*://.*");
	}

}
