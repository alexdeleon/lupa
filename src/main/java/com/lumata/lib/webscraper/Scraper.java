/*
 * 2012 copyright Lumata
 */
package com.lumata.lib.webscraper;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.buongiorno.frog.lib.http.HttpException;
import com.lumata.lib.webscraper.content.WebContent;

/**
 * Scraps the content of a URL and returns metadata information about it.
 * 
 * @author Alexander De Leon - alexander.leon@lumatagroup.com
 * 
 */
@Service
public interface Scraper {

	/**
	 * Extract the list of structured contents from the given URL.
	 * 
	 * @param url
	 *            of the web resource
	 * @return the {@link WebContent} object describing the contents of the URL and all alias URLs. Or null if no
	 *         content is found.
	 * @throws IOException
	 *             is an IO error occurs while connecting to the URL.
	 * @throws HttpException
	 */
	WebContent extractContentFromUrl(String url) throws IOException, HttpException;

	/**
	 * Extract the list of structured contents from the given URL.
	 * 
	 * @param url
	 *            of the web resource
	 * @return the {@link WebContent} object describing the contents of the URL and all alias URLs. Or null if no
	 *         content is found.
	 * @throws IOException
	 *             is an IO error occurs while connecting to the URL.
	 * @throws HttpException
	 */
	<E extends WebContent> E extractContentFromUrl(String url, Class<E> contentType) throws IOException, HttpException;

	/**
	 * Returns the favicon for a website given any url
	 * 
	 * @param url
	 *            The URL of any web page
	 * @return the url of the favicon
	 */
	String extractFavicon(String url) throws IOException;

}
