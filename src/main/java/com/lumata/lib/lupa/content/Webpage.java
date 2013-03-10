/**
 * Copyright (c) 2013 Lumata
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.lumata.lib.lupa.content;

import java.util.ArrayList;
import java.util.List;

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
		return toStringHelper(this).add("title", title).add("description", description)
				.add("previewImage", previewImage).add("feeds", feeds).add("embeddedContent", embeddedContent)
				.toString();
	}

}
