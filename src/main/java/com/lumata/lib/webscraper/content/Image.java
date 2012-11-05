/*
 * 2012 copyright Lumata
 */
package com.lumata.lib.webscraper.content;


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
		return toStringHelper(this).add("with", width).add("height", height).toString();
	}
}
