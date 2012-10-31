/*
 * 2012 copyright Lumata
 */
package com.lumata.lib.webscraper.content;

/**
 * @author Alexander De Leon - alexander.leon@lumatagroup.com
 * 
 */
public class Video extends WebContent {

	public Video(String url, String... aliasUrls) {
		super(url, aliasUrls);
	}

	@Override
	public Type getType() {
		return Type.VIDEO;
	}

}
