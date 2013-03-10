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
