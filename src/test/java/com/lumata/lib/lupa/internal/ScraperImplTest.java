package com.lumata.lib.lupa.internal;

import static org.mockito.Mockito.mock;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.buongiorno.frog.lib.http.HttpException;
import com.lumata.lib.lupa.ServiceLocator;
import com.lumata.lib.lupa.content.WebContent;
import com.lumata.lib.lupa.extractor.ContentExtractorFactory;
import com.lumata.lib.lupa.internal.ScraperImpl;

public class ScraperImplTest {

	private static final String TEST_URL = "http://example.com";

	private ScraperImpl scraper;
	private ContentExtractorFactory extractorFactoryMock;
	private ServiceLocator serviceLocatorMock;

	@Before
	public void setup() {
		extractorFactoryMock = mock(ContentExtractorFactory.class);
		serviceLocatorMock = mock(ServiceLocator.class);
		scraper = new ScraperImpl(extractorFactoryMock, serviceLocatorMock);
	}

	@Test
	private void testScrapFromUrl() throws IOException, HttpException {
		WebContent content = scraper.scrapContent(TEST_URL);
	}

}
