/*
 * 2012 copyright Lumata
 */
package com.lumata.lib.webscraper.internal;

import java.net.URL;

import org.apache.http.client.HttpClient;

import com.lumata.lib.webscraper.HttpService;
import com.lumata.lib.webscraper.ReadableResource;
import com.lumata.lib.webscraper.TextReadableResource;

/**
 * @author Alexander De Leon - alexander.leon@lumatagroup.com
 * 
 */
public class HttpServiceImpl implements HttpService {

	private final HttpClient httpClient;

	public HttpServiceImpl(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	@Override
	public ReadableResource getRawResource(URL url) {
		return new ProxiedReadableResource(url, httpClient);
	}

	@Override
	public TextReadableResource getTextResource(URL url) {
		return new TextReadableResourceDecorator(getRawResource(url));
	}

	@Override
	public TextReadableResource getTextResource(ReadableResource resource) {
		return getTextResource(resource.getUrl());
	}

}
