package com.lumata.lib.webscraper.extractor.internal;

import java.io.IOException;

import com.buongiorno.frog.lib.http.HttpException;
import com.lumata.lib.webscraper.ReadableResource;
import com.lumata.lib.webscraper.ServiceLocator;
import com.lumata.lib.webscraper.content.Image;

public class ImageExtractor extends AbstractExtractor<Image> {

	@Override
	public Image extractContent(ReadableResource resource, ServiceLocator serviceLocator) throws IOException,
			HttpException {
		return serviceLocator.getImageService().getImageFromResource(resource);
	}

	@Override
	public Class<Image> getExtractableWebContentType() {
		return Image.class;
	}

}
