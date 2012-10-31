/*
 * 2012 copyright Lumata
 */
package com.lumata.lib.webscraper.internal;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import com.buongiorno.frog.lib.http.HttpException;
import com.google.common.net.MediaType;
import com.lumata.lib.webscraper.HttpService;
import com.lumata.lib.webscraper.ImageService;
import com.lumata.lib.webscraper.ReadableResource;
import com.lumata.lib.webscraper.content.Image;

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
	public Image getImageFromUrl(URL imageUrl) throws IOException, HttpException {
		ReadableResource resource = httpService.getRawResource(imageUrl);
		// read before checking content type to avoid doing an HTTP HEAD.
		InputStream imageStream = resource.read();

		// check that the URL returns something that appears that is expected to
		// be an image.
		if (!isImageMimeType(resource)) {
			return null;
		}

		Image imageMetadata = new Image(imageUrl.toExternalForm());
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
