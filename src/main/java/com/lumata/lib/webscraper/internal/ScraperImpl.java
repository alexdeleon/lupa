/*
 * 2012 copyright Lumata
 */
package com.lumata.lib.webscraper.internal;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.buongiorno.frog.lib.http.HttpException;
import com.google.common.base.Optional;
import com.lumata.lib.webscraper.HttpService;
import com.lumata.lib.webscraper.ReadableResource;
import com.lumata.lib.webscraper.Scraper;
import com.lumata.lib.webscraper.ServiceLocator;
import com.lumata.lib.webscraper.content.WebContent;
import com.lumata.lib.webscraper.extractor.ContentExtractor;
import com.lumata.lib.webscraper.extractor.ContentExtractorFactory;

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
	public WebContent extractContentFromUrl(String url) throws IOException, HttpException {
		return extractContentFromUrl(url, null);
	}

	@Override
	public <E extends WebContent> E extractContentFromUrl(String url, Class<E> contentType) throws IOException,
			HttpException {
		ReadableResource resource = createResource(url);
		try {
			Optional<ContentExtractor<E>> extractor = getExtractor(resource, contentType);
			if (extractor.isPresent()) {
				E content = extractor.get().extractContent(resource, serviceLocator);
				if (contentType == null || contentType.isAssignableFrom(content.getClass())) {
					return content;
				}
			}
			LOG.warn("No content extractor found for {}", url);
			return null;
		} finally {
			resource.discard();
		}
	}

	@Override
	public String extractFavicon(String url) throws IOException {
		// TODO Auto-generated method stub
		return null;
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
