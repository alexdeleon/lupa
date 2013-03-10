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

import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lumata.lib.lupa.ImageService;
import com.lumata.lib.lupa.content.Image;

/**
 * @author Alexander De Leon - alexander.leon@lumatagroup.com
 * 
 */
public class HtmlBiggestImageExtractor implements HtmlImageExtractor {

	private static final Logger LOG = LoggerFactory.getLogger(HtmlBiggestImageExtractor.class);

	private static final String WIDTH_ATTRIBUTE = "width";
	private static final String HEIGHT_ATTRIBUTE = "height";

	private final ImageService imageService;

	public HtmlBiggestImageExtractor(ImageService imageService) {
		this.imageService = imageService;
	}

	@Override
	public Image extractBestImage(URL sourceUrl, Elements htmlSection) {
		return extractBestImage(sourceUrl, htmlSection, new ImageExtractionRequirements());
	}

	@Override
	public Image extractBestImage(URL sourceUrl, Elements htmlSection, ImageExtractionRequirements requirements) {
		Map<String, Image> imagesToExplore = new HashMap<String, Image>();
		Set<ImageDownloadTask> imagesToDownload = new HashSet<ImageDownloadTask>();
		Iterator<org.jsoup.nodes.Element> it = htmlSection.iterator();

		// collect valid images
		while (it.hasNext() && imagesToExplore.size() < requirements.getMaxImagesToExplore()) {
			Element imageElement = it.next();
			String imageUrl = imageElement.absUrl("src");

			// Do not process empty img tags, duplicated images or tracking
			// pixels and other assorted ads
			if (imageUrl == null || imagesToExplore.containsKey(imageUrl) || isTrackingPixelOrAd(imageUrl)) {
				continue;
			}

			// remember this image
			Image imageContent = new Image(imageUrl);
			if (imageElement.hasAttr(WIDTH_ATTRIBUTE)) {
				// TODO: We need to convert other picture size units supported by html (there must be a lib for this)
				imageContent.setWidth(Integer.parseInt(imageElement.attr(WIDTH_ATTRIBUTE).replace("px", "")));
			}
			if (imageElement.hasAttr(HEIGHT_ATTRIBUTE)) {
				imageContent.setHeight(Integer.parseInt(imageElement.attr(HEIGHT_ATTRIBUTE).replace("px", "")));
			}
			if (imageContent.getWidth() == null || imageContent.getHeight() == null) {// mark image to download
				imagesToDownload.add(new ImageDownloadTask(imageContent));
			}
			imagesToExplore.put(imageUrl, imageContent);
		}

		// if dimensions are empty -> download image
		if (CollectionUtils.isNotEmpty(imagesToDownload)) {
			try {
				ExecutorService pool = Executors.newFixedThreadPool(imagesToDownload.size(),
						getThreadFactory(sourceUrl));
				pool.invokeAll(imagesToDownload);
				pool.shutdown();
			} catch (InterruptedException e) {
				LOG.error("InterruptedException while downloading images", e);
			}
		}

		// select biggest image
		Image biggestImage = null;
		try {
			biggestImage = Collections.max(imagesToExplore.values(), new Comparator<Image>() {
				@Override
				public int compare(Image o1, Image o2) {
					return getSquarePixels(o1) - getSquarePixels(o2);
				}
			});
		} catch (NoSuchElementException e) {
			return null;
		}

		// if image is too small, discard
		return (biggestImage.getWidth() < requirements.getMinImageSize() || biggestImage.getHeight() < requirements
				.getMinImageSize()) ? null : biggestImage;
	}

	/* ------------------------- helper methods -- */
	private ThreadFactory getThreadFactory(URL url) {
		return new BasicThreadFactory.Builder().namingPattern("HtmlBiggestImageExtractor[" + url + "]: %d").build();
	}

	private class ImageDownloadTask implements Callable<Void> {

		private final Image imageContent;

		public ImageDownloadTask(Image imageContent) {
			this.imageContent = imageContent;
		}

		@Override
		public Void call() throws Exception {
			Image imageDownloaded = imageService.getImageFromUrl(new URL(imageContent.getUrl()));
			if (imageDownloaded != null) {
				imageContent.setWidth(imageDownloaded.getWidth());
				imageContent.setHeight(imageDownloaded.getHeight());
			}
			return null;
		}

		@Override
		public int hashCode() {
			return imageContent.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof ImageDownloadTask) {
				ImageDownloadTask that = (ImageDownloadTask) obj;
				return that.imageContent.equals(this.imageContent);
			}
			return false;
		}
	}

	private int getSquarePixels(Image image) {
		int width = (image.getWidth() == null) ? 0 : image.getWidth();
		int height = (image.getHeight() == null) ? 0 : image.getHeight();

		return width * height;
	}

	private boolean isTrackingPixelOrAd(String imageUrl) {
		return imageUrl.contains("ad.doubleclick.net");
	}

}
