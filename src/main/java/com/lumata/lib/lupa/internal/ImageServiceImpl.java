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
package com.lumata.lib.lupa.internal;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import com.google.common.net.MediaType;
import com.lumata.lib.lupa.HttpService;
import com.lumata.lib.lupa.ImageService;
import com.lumata.lib.lupa.ReadableResource;
import com.lumata.lib.lupa.content.Image;

/**
 * @author Alexander De Leon - alexander.leon@lumatagroup.com
 * 
 */
public class ImageServiceImpl implements ImageService {

	private final HttpService httpService;

	public ImageServiceImpl(HttpService httpService) {
		super();
		this.httpService = httpService;
	}

	@Override
	public Image getImageFromUrl(URL imageUrl) throws IOException {
		ReadableResource resource = httpService.getRawResource(imageUrl);
		return getImageFromResource(resource);

	}

	@Override
	public Image getImageFromResource(ReadableResource resource) throws IOException {
		// read before checking content type to avoid doing an HTTP HEAD.
		InputStream imageStream = resource.read();

		// check that the URL returns something that appears that is expected to
		// be an image.
		if (!isImageMimeType(resource)) {
			return null;
		}

		Image imageMetadata = new Image(resource.getUrl().toExternalForm());
		populateImageDimensions(imageStream, imageMetadata);

		resource.discard();
		return imageMetadata;
	}

	/*------------------ helper methods --- */
	/**
	 * @param resource
	 * @param imageMetadata
	 * @throws IOException
	 */
	private void populateImageDimensions(InputStream stream, Image imageMetadata) throws IOException {
		ImageInputStream imageStream = ImageIO.createImageInputStream(stream);
		try {
			final Iterator<ImageReader> readers = ImageIO.getImageReaders(imageStream);
			if (readers.hasNext()) {
				ImageReader reader = readers.next();
				try {
					reader.setInput(imageStream);
					imageMetadata.setWidth(reader.getWidth(0));
					imageMetadata.setHeight(reader.getHeight(0));
				} finally {
					reader.dispose();
				}
			}
		} finally {
			if (imageStream != null) {
				imageStream.close();
			}
		}
	}

	/**
	 * @param contentType
	 * @return
	 */
	private boolean isImageMimeType(ReadableResource readableResource) {
		if (!readableResource.getContentType().isPresent()) {
			return false;
		}
		return readableResource.getContentType().get().is(MediaType.ANY_IMAGE_TYPE);
	}
}
