/*
 * 2012 copyright Lumata
 */
package com.lumata.lib.webscraper.extractor.internal;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.select.Elements;

import com.lumata.lib.webscraper.content.Image;

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
