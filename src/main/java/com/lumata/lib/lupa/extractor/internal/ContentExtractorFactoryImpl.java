/*
 * 2011 copyright Buongiorno SpA
 */
package com.lumata.lib.lupa.extractor.internal;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.lumata.lib.lupa.ReadableResource;
import com.lumata.lib.lupa.content.WebContent;
import com.lumata.lib.lupa.extractor.ContentExtractor;
import com.lumata.lib.lupa.extractor.ContentExtractorFactory;
import com.lumata.lib.lupa.extractor.internal.config.ExtractorConfiguration;
import com.lumata.lib.lupa.extractor.internal.config.ExtractorConfigurationModule;

/**
 * @author Alexander De Leon - alexander.leon@buongiorno.com
 * 
 */
@ThreadSafe
public class ContentExtractorFactoryImpl implements ContentExtractorFactory {

	private static final Logger LOG = LoggerFactory.getLogger(ContentExtractorFactoryImpl.class);

	/**
	 * A set of extractors matchers
	 */
	private final SortedSet<ContentExtractorMatcher<? extends WebContent>> matchers;

	public ContentExtractorFactoryImpl(ExtractorConfigurationModule module) {
		this(module.configurations());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ContentExtractorFactoryImpl(ExtractorConfiguration... configurations) {
		matchers = new TreeSet<ContentExtractorMatcher<? extends WebContent>>();
		LOG.info("Loading Extractors configurations");

		// Create the configuration matchers which are added to the sorted set in natural order.
		for (ExtractorConfiguration configuration : configurations) {
			LOG.debug("Loading configuration {}", configuration);
			ContentExtractor<? extends WebContent> contentExtractor = instanciateAndConfigureContentExtractor(configuration);
			if (contentExtractor != null) {
				if (!matchers.add(new ContentExtractorMatcher(contentExtractor, configuration.getConstraints(),
						configuration.getPriority()))) {
					LOG.warn("This should not happen! Found duplicated configuration {}", configuration);
				}
			} else {
				LOG.warn("Failed to load webscraper configuration {}", configuration);
			}
		}

		LOG.info("Extractors configurations loaded: found " + matchers.size());
	}

	@Override
	public Optional<ContentExtractor<WebContent>> getExtractor(ReadableResource resource) {
		return getExtractor(resource, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <E extends WebContent> Optional<ContentExtractor<E>> getExtractor(ReadableResource resource,
			Class<E> webContentType) {
		for (ContentExtractorMatcher<? extends WebContent> matcher : matchers) {
			if ((webContentType == null || matcher.isCaplableOfExtracting(webContentType)) && matcher.matches(resource)) {
				return Optional.of((ContentExtractor<E>) matcher.getContentExtractor());
			}
		}
		return Optional.absent();
	}

	private ContentExtractor<? extends WebContent> instanciateAndConfigureContentExtractor(
			ExtractorConfiguration configuration) {
		try {
			ContentExtractor<? extends WebContent> extractor = configuration.getExtractorClass().newInstance();
			extractor.setConfiguration(configuration.getParameters());
			return extractor;
		} catch (Exception e) {
			LOG.error("Error while instanciating content extractor: {}", configuration.getExtractorClass(), e);
			return null;
		}
	}

}
