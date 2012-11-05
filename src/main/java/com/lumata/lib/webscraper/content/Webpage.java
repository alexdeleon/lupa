/*
 * 2012 copyright Lumata
 */
package com.lumata.lib.webscraper.content;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.base.Objects;

/**
 * @author Alexander De Leon - alexander.leon@lumatagroup.com
 * 
 */
public class Webpage extends WebContent {

	private String title;
	private String description;
	private Image previewImage;
	private List<Feed> feeds;
	private List<WebContent> embeddedContent;

	public Webpage(String url, String... aliasUrls) {
		super(url, aliasUrls);
	}

	/**
	 * @return the title defined in the HEAD section of this webpage
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the page title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the description of this webpage as it appears in its meta section.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the preview image defined in the metadata of this webpage.
	 */
	public Image getPreviewImage() {
		return previewImage;
	}

	/**
	 * 
	 * @param previewImage
	 *            the preview image to set
	 */
	public void setPreviewImage(Image previewImage) {
		this.previewImage = previewImage;
	}

	/**
	 * @return the feeds (RSS/Atom) that are linked in the metadata of this webpage.
	 */
	public List<Feed> getFeeds() {
		return feeds;
	}

	/**
	 * @param feeds
	 *            the feeds to set
	 */
	public void setFeeds(List<Feed> feeds) {
		this.feeds = feeds;
	}

	public void addFeed(Feed feed) {
		if (feeds == null) {
			feeds = new ArrayList<Feed>();
		}
		feeds.add(feed);
	}

	public List<WebContent> getEmbeddedContent() {
		return embeddedContent;
	}

	public void setEmbeddedContent(List<WebContent> embeddedContent) {
		this.embeddedContent = embeddedContent;
	}

	public void addEmbeddedContent(WebContent content) {
		if (embeddedContent == null) {
			embeddedContent = new ArrayList<WebContent>();
		}
		embeddedContent.add(content);
	}

	@Override
	public Type getType() {
		return Type.WEBPAGE;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).omitNullValues().add("url", getUrl())
				.add("aliasUrls", Arrays.toString(getAliasUrls())).add("title", title).add("description", description)
				.add("previewImage", previewImage).add("feeds", feeds).add("embeddedContent", embeddedContent)
				.add("keywords", getKeywords()).toString();
	}

}
