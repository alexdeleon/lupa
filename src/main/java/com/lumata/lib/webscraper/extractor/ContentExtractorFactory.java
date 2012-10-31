/*
 * 2011 copyright Buongiorno SpA
 */
package com.lumata.lib.webscraper.extractor;

import com.google.common.base.Optional;
import com.lumata.lib.webscraper.ReadableResource;
import com.lumata.lib.webscraper.content.WebContent;

/**
 * @author Alexander De Leon - alexander.leon@buongiorno.com
 * 
 */
public interface ContentExtractorFactory {
	/**
	 * @param resource
	 *            the resource to extract the content from.
	 * @param scraper
	 *            the scraper to use internally by the content scraper.
	 * @return
	 */
	<E extends WebContent> Optional<ContentExtractor<E>> getExtractor(ReadableResource resource, Class<E> webContentType);

	/**
	 * @param resource
	 *            the resource to extract the content from.
	 * @param scraper
	 *            the scraper to use internally by the content scraper.
	 * @return
	 */
	Optional<ContentExtractor<WebContent>> getExtractor(ReadableResource resource);

}
