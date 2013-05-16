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
package com.lumata.lib.lupa;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.lumata.lib.lupa.content.WebContent;

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
	WebContent scrapContent(String url) throws IOException;

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
	<E extends WebContent> E scrapContent(String url, Class<E> contentType) throws IOException;

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
	WebContent scrapContent(ReadableResource resource) throws IOException;

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
	<E extends WebContent> E scrapContent(ReadableResource resource, Class<E> contentType) throws IOException;

}
