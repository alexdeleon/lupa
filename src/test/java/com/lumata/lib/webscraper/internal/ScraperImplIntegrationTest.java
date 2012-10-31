/*
 * 2012 copyright Lumata
 */
package com.lumata.lib.webscraper.internal;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.buongiorno.frog.lib.http.HttpException;
import com.lumata.lib.webscraper.Scraper;
import com.lumata.lib.webscraper.content.WebContent;
import com.lumata.lib.webscraper.content.Webpage;

/**
 * @author Alexander De Leon - alexander.leon@lumatagroup.com
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:com/lumata/lib/webscraper/internal/integration-test-config.xml")
public class ScraperImplIntegrationTest {

	private static final Logger LOG = LoggerFactory.getLogger(ScraperImplIntegrationTest.class);
	private static final String URLS_FILE = "/1m-urls-with-www.txt.gz";

	@Autowired
	Scraper scraper;

	@Test
	public void testVogue() throws IOException, HttpException {
		String url = "http://www.vogue.co.jp/collection";
		LOG.info("Result from scraper: {}", scraper.extractContentFromUrl(url));

	}

	@Test
	public void testTwiterUrl() throws IOException, HttpException {
		String url = "http://t.co/QMDccPr3";
		WebContent webpage = scraper.extractContentFromUrl(url);
		assertEquals("http://www.bbc.co.uk/news/science-environment-17013285", webpage.getUrl());
		LOG.info("Result from scraper: {}", webpage);
	}

	@Test
	public void test1MLoad() throws IOException, HttpException {
		BufferedReader reader = createReader();
		String url = null;
		while ((url = reader.readLine()) != null) {
			try {
				LOG.info("Result from scraper: {}", scraper.extractContentFromUrl(url, Webpage.class));
			} catch (Exception e) {
				LOG.error("Error scraping url {}", url, e);
			}
		}
	}

	private BufferedReader createReader() throws IOException {
		return new BufferedReader(new InputStreamReader(new GZIPInputStream(
				ScraperImplIntegrationTest.class.getResourceAsStream(URLS_FILE))));
	}

}
