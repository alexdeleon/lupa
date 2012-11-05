/*
 * 2012 copyright Lumata
 */
package com.lumata.lib.webscraper.content;

import java.util.Arrays;

import com.google.common.base.Objects;

/**
 * @author Alexander De Leon - alexander.leon@lumatagroup.com
 * 
 */
public class Image extends WebContent {

	private Integer width, height;

	public Image(String url, String... aliasUrls) {
		super(url, aliasUrls);
	}

	/**
	 * @return the width in pixels
	 */
	public Integer getWidth() {
		return width;
	}

	/**
	 * @param width
	 *            the width in pixels to set
	 */
	public void setWidth(Integer width) {
		this.width = width;
	}

	/**
	 * @return the height in pixels
	 */
	public Integer getHeight() {
		return height;
	}

	/**
	 * @param height
	 *            the height in pixels to set
	 */
	public void setHeight(Integer height) {
		this.height = height;
	}

	@Override
	public Type getType() {
		return Type.IMAGE;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).omitNullValues().add("url", getUrl())
				.add("aliasUrls", Arrays.toString(getAliasUrls())).add("with", width).add("height", height)
				.add("keywords", getKeywords()).toString();
	}
}
