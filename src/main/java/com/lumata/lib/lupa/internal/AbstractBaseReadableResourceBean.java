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
package com.lumata.lib.lupa.internal;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.net.MediaType;
import com.lumata.lib.lupa.ReadableResource;

/**
 * @author Alexander De Leon - alexander.leon@lumatagroup.com
 * 
 */
public abstract class AbstractBaseReadableResourceBean implements ReadableResource {

	private URL url;
	private List<URL> redirects;
	private MediaType contentType;

	AbstractBaseReadableResourceBean(URL url) {
		Validate.notNull(url);
		this.url = url;
	}

	@Override
	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	@Override
	public Optional<URL[]> getRedirectionPath() {
		if (redirects == null) {
			return Optional.absent();
		}
		return Optional.of(redirects.toArray(new URL[redirects.size()]));
	}

	@Override
	public Optional<MediaType> getContentType() {
		return Optional.fromNullable(contentType);
	}

	public void setContentType(MediaType contentType) {
		this.contentType = contentType;
	}

	/**
	 * Move the current URL to the redirection path and set the specified url as the actual resource url.
	 * 
	 * @param url
	 *            the redirectionUrl
	 */
	@Override
	public void makeRedirection(URL url) {
		if (redirects == null) {
			redirects = new ArrayList<URL>();
		}
		redirects.add(getUrl());
		this.url = url;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(url);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof AbstractBaseReadableResourceBean)) {
			return false;
		}
		AbstractBaseReadableResourceBean that = (AbstractBaseReadableResourceBean) obj;
		return Objects.equal(this.url, that.url);
	}
}
