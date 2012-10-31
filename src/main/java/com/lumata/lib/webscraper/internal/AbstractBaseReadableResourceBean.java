/*
 * 2012 copyright Lumata
 */
package com.lumata.lib.webscraper.internal;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.net.MediaType;
import com.lumata.lib.webscraper.ReadableResource;

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
