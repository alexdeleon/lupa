/*
 * 2012 copyright Lumata
 */
package com.lumata.lib.webscraper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.buongiorno.frog.lib.http.HttpException;
import com.google.common.base.Optional;
import com.google.common.net.MediaType;

/**
 * Represents a readable resource on the web resolvable by its URL.
 * 
 * @author Alexander De Leon - alexander.leon@lumatagroup.com
 * 
 */
public interface ReadableResource {

	URL getUrl();

	/**
	 * Move the current URL to the redirection path and set the specified url as the actual resource url.
	 * 
	 * @param url
	 *            the redirectionUrl
	 */
	void makeRedirection(URL url);

	Optional<URL[]> getRedirectionPath();

	Optional<MediaType> getContentType();

	InputStream read() throws IOException, HttpException;

	void discard();
}
