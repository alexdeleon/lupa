/*
 * 2012 copyright Lumata
 */
package com.lumata.lib.webscraper;

import java.net.URL;

import org.springframework.stereotype.Service;

/**
 * @author Alexander De Leon - alexander.leon@lumatagroup.com
 * 
 */
@Service
public interface HttpService {

	ReadableResource getRawResource(URL url);

	TextReadableResource getTextResource(URL url);

	TextReadableResource getTextResource(ReadableResource resource);

}
