/*
 * 2012 copyright Lumata
 */
package com.lumata.lib.webscraper;

import java.io.IOException;
import java.net.URL;

import com.buongiorno.frog.lib.http.HttpException;
import com.lumata.lib.webscraper.content.Image;

/**
 * @author Alexander De Leon - alexander.leon@lumatagroup.com
 * 
 */
public interface ImageService {

	/**
	 * Extracts image metadata from the specified URL. If it is unable to determine that the given URL contains an image
	 * this methods returns <strong>null</strong>.
	 * 
	 * @param imageUrl
	 * @return an Image object or null if no image found at the given URL.
	 * @throws IOException
	 *             if something goes wrong connecting to the image source.
	 * @throws HttpException
	 */
	Image getImageFromUrl(URL imageUrl) throws IOException, HttpException;

}
