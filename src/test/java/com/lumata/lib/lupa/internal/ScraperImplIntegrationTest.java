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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.buongiorno.frog.lib.http.HttpException;
import com.lumata.lib.lupa.Scraper;
import com.lumata.lib.lupa.content.WebContent;
import com.lumata.lib.lupa.content.Webpage;

/**
 * @author Alexander De Leon - alexander.leon@lumatagroup.com
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:integration-test-config.xml")
public class ScraperImplIntegrationTest {

	private static final Logger LOG = LoggerFactory.getLogger(ScraperImplIntegrationTest.class);
	private static final String URLS_FILE = "/1m-urls-with-www.txt.gz";

	@Autowired
	Scraper scraper;

	@Test
	public void testVogue() throws IOException, HttpException {
		String url = "http://www.vogue.co.jp/collection";
		LOG.info("Result from scraper: {}", scraper.scrapContent(url));

	}

	@Test
	public void testTwiterUrl() throws IOException, HttpException {
		String url = "http://t.co/QMDccPr3";
		WebContent webpage = scraper.scrapContent(url);
		assertEquals("http://www.bbc.co.uk/news/science-environment-17013285", webpage.getUrl());
		LOG.info("Result from scraper: {}", webpage);
	}

	@Test
	public void testPageWithVideo() throws IOException, HttpException {
		String url = "http://mashable.com/2012/11/05/tesla-coil-fight/";
		Webpage webpage = (Webpage) scraper.scrapContent(url);
		assertTrue(CollectionUtils.isNotEmpty(webpage.getEmbeddedContent()));
		assertEquals(WebContent.Type.VIDEO, webpage.getEmbeddedContent().get(0).getType());
	}

	@Ignore("TODO: we need be able to extract CSS imges in other to get this working")
	@Test
	public void testGoogleLogoExtraction() throws IOException, HttpException {
		String url = "http://www.google.com";
		Webpage webpage = (Webpage) scraper.scrapContent(url);
		System.out.println(webpage);
		assertTrue(CollectionUtils.isNotEmpty(webpage.getEmbeddedContent()));
		assertTrue(webpage.getPreviewImage() != null);
	}

	@Test
	public void test1MLoad() throws IOException, HttpException {
		BufferedReader reader = createReader();
		String url = null;
		while ((url = reader.readLine()) != null) {
			try {
				LOG.info("Result from scraper: {}", scraper.scrapContent(url, Webpage.class));
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
