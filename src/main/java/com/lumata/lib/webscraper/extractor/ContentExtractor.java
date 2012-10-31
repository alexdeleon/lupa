/*
 * 2012 copyright Lumata
 */
package com.lumata.lib.webscraper.extractor;

import java.io.IOException;
import java.util.Map;

import com.buongiorno.frog.lib.http.HttpException;
import com.lumata.lib.webscraper.ReadableResource;
import com.lumata.lib.webscraper.ServiceLocator;
import com.lumata.lib.webscraper.content.WebContent;

/**
 * @author Alexander De Leon - alexander.leon@lumatagroup.com
 * 
 */
public interface ContentExtractor<E extends WebContent> {

	E extractContent(ReadableResource resource, ServiceLocator serviceLocator) throws IOException, HttpException;

	void setConfiguration(Map<String, String> config);

	Class<E> getExtractableWebContentType();

}
