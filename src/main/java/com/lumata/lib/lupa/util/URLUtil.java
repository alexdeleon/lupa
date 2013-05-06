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