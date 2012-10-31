/*
 * 2012 copyright Lumata
 */
package com.lumata.lib.webscraper.content;

/**
 * @author Alexander De Leon - alexander.leon@lumatagroup.com
 * 
 */
public class Feed extends WebContent {

	private String title;

	public Feed(String url, String... aliasUrls) {
		super(url, aliasUrls);
	}

	@Override
	public Type getType() {
		return Type.FEED;
	}

	/**
	 * @return the title of this feed
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

}
