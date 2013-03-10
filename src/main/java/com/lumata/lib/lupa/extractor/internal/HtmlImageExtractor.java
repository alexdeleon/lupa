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
package com.lumata.lib.lupa.extractor.internal;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.select.Elements;

import com.lumata.lib.lupa.content.Image;

/**
 * @author Alexander De Leon - alexander.leon@lumatagroup.com
 * 
 */
public interface HtmlImageExtractor {

	static final int DEFAULT_MAX_IMAGES_TO_EXPLORE = 10;
	static final int DEFAULT_MIN_IMAGE_SIZE = 65;

	/**
	 * Looks at the the first number of images (maxImagesToLook) and returns the biggest one. The size of the image is
	 * taken from the HTML tag or if absent computed by reading the image meta.
	 * 
	 * @param htmlSection
	 *            a section of the HTML page where to extract the images from
	 * @return a Image or null if none is found.
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	Image extractBestImage(URL sourceUrl, Elements htmlSection);

	/**
	 * Looks at the the first number of images (maxImagesToLook) and returns the biggest one. The size of the image is
	 * taken from the HTML tag or if absent computed by reading the image meta.
	 * 
	 * @param htmlSection
	 *            a section of the HTML page where to extract the images from
	 * @param requirements
	 *            specifies the requiRements used to select the best image.
	 * @return a Image or null if none is found.
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	Image extractBestImage(URL sourceUrl, Elements htmlSection, ImageExtractionRequirements requirements);

	class ImageExtractionRequirements {

		private int maxImagesToExplore = DEFAULT_MAX_IMAGES_TO_EXPLORE;
		private int minImageSize = DEFAULT_MIN_IMAGE_SIZE;

		public int getMaxImagesToExplore() {
			return maxImagesToExplore;
		}

		public ImageExtractionRequirements setMaxImagesToExplore(int maxImagesToExplore) {
			this.maxImagesToExplore = maxImagesToExplore;
			return this;
		}

		public int getMinImageSize() {
			return minImageSize;
		}

		public ImageExtractionRequirements setMinImageSize(int minImageSize) {
			this.minImageSize = minImageSize;
			return this;
		}

	}

}
