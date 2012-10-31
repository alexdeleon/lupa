/*
 * 2012 copyright Lumata
 */
package com.lumata.lib.webscraper;

/**
 * @author Alexander De Leon - alexander.leon@lumatagroup.com
 * 
 */
public interface ServiceLocator {

	Scraper getScraper();

	HttpService getHttpService();

	ImageService getImageService();
}
