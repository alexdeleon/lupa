/*
 * 2012 copyright Lumata
 */
package com.lumata.lib.webscraper.content;

import java.util.Set;

import com.google.common.base.Objects;

/**
 * A Web Content is an abstraction that describes (using metadata) the content of a web resource.
 * 
 * @author Alexander De Leon - alexander.leon@lumatagroup.com
 * 
 */
public abstract class WebContent {

	public static enum Type {
		WEBPAGE,
		IMAGE,
		VIDEO,
		AUDIO,
		FEED
	}

	private final String url;
	private final String[] aliasUrls;
	private Set<String> keywords;

	WebContent(String url, String... aliasUrls) {
		this.url = url;
		this.aliasUrls = aliasUrls;
	}

	/**
	 * Gets the preferred URL of this content
	 * 
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Gets the known alias URLs where this content can be also located. The order is defined by the redirection path.
	 * 
	 * @return the referingUrls
	 */
	public String[] getAliasUrls() {
		return aliasUrls;
	}

	/**
	 * @return the type of this web content.
	 */
	public abstract Type getType();

	/**
	 * @return the keywords of this web content.
	 */
	public Set<String> getKeywords() {
		return keywords;
	}

	/**
	 * @param keywords
	 *            the keywords to set
	 */
	public void setKeywords(Set<String> keywords) {
		this.keywords = keywords;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(url);
	}

}
