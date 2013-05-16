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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Optional;
import com.lumata.lib.lupa.HttpService;
import com.lumata.lib.lupa.ReadableResource;
import com.lumata.lib.lupa.Scraper;
import com.lumata.lib.lupa.ServiceLocator;
import com.lumata.lib.lupa.content.WebContent;
import com.lumata.lib.lupa.extractor.ContentExtractor;
import com.lumata.lib.lupa.extractor.ContentExtractorFactory;

/**
 * @author Alexander De Leon - alexander.leon@lumatagroup.com
 * 
 */
public class ScraperImpl implements Scraper {

	private static final Logger LOG = LoggerFactory.getLogger(ScraperImpl.class);

	private final ContentExtractorFactory extractorFactory;
	private final ServiceLocator serviceLocator;

	@Autowired
	public ScraperImpl(ContentExtractorFactory extractorFactory, ServiceLocator serviceLocator) {
		this.extractorFactory = extractorFactory;
		this.serviceLocator = serviceLocator;
	}

	@Override
	public WebContent scrapContent(String url) throws IOException {
		return scrapContent(url, null);
	}

	@Override
	public <E extends WebContent> E scrapContent(String url, Class<E> contentType) throws IOException {
		ReadableResource resource = createResource(url);
		return scrapContent(resource, contentType);
	}

	@Override
	public WebContent scrapContent(ReadableResource resource) throws IOException {
		return scrapContent(resource, null);
	}

	@Override
	public <E extends WebContent> E scrapContent(ReadableResource resource, Class<E> contentType) throws IOException {
		try {
			Optional<ContentExtractor<E>> extractor = getExtractor(resource, contentType);
			if (extractor.isPresent()) {
				E content = extractor.get().extractContent(resource, serviceLocator);
				if (contentType == null || contentType.isAssignableFrom(content.getClass())) {
					return content;
				}
			}
			LOG.warn("No content extractor found for {}", resource.getUrl());
			return null;
		} finally {
			resource.discard();
		}
	}

	/* ------------------------ helper methods -- */
	private ReadableResource createResource(String url) throws MalformedURLException {
		return getHttpService().getRawResource(new URL(url));
	}

	private HttpService getHttpService() {
		return serviceLocator.getHttpService();
	}

	private <E extends WebContent> Optional<ContentExtractor<E>> getExtractor(ReadableResource resource,
			Class<E> webContentType) {
		return extractorFactory.getExtractor(resource, webContentType);
	}

}
