package com.lumata.lib.lupa.extractor.internal;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lumata.lib.lupa.ReadableResource;
import com.lumata.lib.lupa.ServiceLocator;
import com.lumata.lib.lupa.content.Image;

public class ImageExtractor extends AbstractExtractor<Image> {

	private static final Logger LOG = LoggerFactory.getLogger(ImageExtractor.class);

	@Override
	public Image extractContent(ReadableResource resource, ServiceLocator serviceLocator) throws IOException {
		Image image = serviceLocator.getImageService().getImageFromResource(resource);
		LOG.debug("Extracting image from {} {}", resource.getUrl(), image != null ? "succeeded" : "failed");
		return image;
	}

	@Override
	public Class<Image> getExtractableWebContentType() {
		return Image.class;
	}

}
