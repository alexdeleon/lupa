package com.lumata.lib.webscraper.extractor.internal;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buongiorno.frog.lib.http.HttpException;
import com.lumata.lib.webscraper.ReadableResource;
import com.lumata.lib.webscraper.ServiceLocator;
import com.lumata.lib.webscraper.content.Image;

public class ImageExtractor extends AbstractExtractor<Image> {

	private static final Logger LOG = LoggerFactory.getLogger(ImageExtractor.class);

	@Override
	public Image extractContent(ReadableResource resource, ServiceLocator serviceLocator) throws IOException,
			HttpException {
		Image image = serviceLocator.getImageService().getImageFromResource(resource);
		LOG.debug("Extracting image from {} {}", resource.getUrl(), image != null ? "succeeded" : "failed");
		return image;
	}

	@Override
	public Class<Image> getExtractableWebContentType() {
		return Image.class;
	}

}
