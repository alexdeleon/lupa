package com.lumata.lib.webscraper.extractor.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.buongiorno.frog.lib.http.HttpException;
import com.google.common.base.Optional;
import com.google.common.net.MediaType;
import com.lumata.lib.webscraper.HttpService;
import com.lumata.lib.webscraper.ImageService;
import com.lumata.lib.webscraper.ReadableResource;
import com.lumata.lib.webscraper.ServiceLocator;
import com.lumata.lib.webscraper.content.Image;
import com.lumata.lib.webscraper.content.Webpage;

public class HtmlExtractorTest {

	private static final String TEST_URL = "http://example.org";

	private HtmlExtractor ext;
	private ServiceLocator serviceLocatorMock;

	@Before
	public void setup() throws IOException, HttpException {
		ext = new HtmlExtractor();
		serviceLocatorMock = createServiceLocatorMock();
	}

	@Test
	public void extractTitle() throws IOException, HttpException {

		// title head
		Webpage content = (Webpage) ext
				.extractContent(createMockResource(TEST_URL, "<html><head><title>test title</title></head></html>"),
						serviceLocatorMock);
		assertEquals("test title", content.getTitle());

		// empty head
		content = (Webpage) ext.extractContent(createMockResource(TEST_URL, "<html><head></head></html>"),
				serviceLocatorMock);
		assertNull(content.getTitle());

		// og:title
		content = (Webpage) ext.extractContent(
				createMockResource(TEST_URL,
						"<html><head><meta property=\"og:title\" content=\"The Rock\" /></head></html>"),
				serviceLocatorMock);
		assertEquals("The Rock", content.getTitle());

		// twitter:title
		content = (Webpage) ext.extractContent(
				createMockResource(TEST_URL,
						"<html><head><meta name=\"twitter:title\" content=\"The Rock2\" /></head></html>"),
				serviceLocatorMock);
		assertEquals("The Rock2", content.getTitle());
	}

	@Test
	public void extractKeywordsFromMeta() throws IOException, HttpException {
		String html = "<html><head><meta name=\"keywords\" content=\"HTML,CSS, XML,JavaScript\" /></head></html>";
		Webpage content = (Webpage) ext.extractContent(createMockResource(TEST_URL, html), serviceLocatorMock);

		Set<String> expected = new HashSet<String>();
		expected.add("HTML");
		expected.add("CSS");
		expected.add("XML");
		expected.add("JavaScript");

		assertEquals(expected, content.getKeywords());
	}

	@Test
	public void extractDescriptionFromMeta() throws IOException, HttpException {
		// meta description
		String html = "<html><head><meta name=\"description\" content=\"Free Web tutorials\" /></head></html>";
		Webpage content = (Webpage) ext.extractContent(createMockResource(TEST_URL, html), serviceLocatorMock);

		assertEquals("Free Web tutorials", content.getDescription());

		// og:description
		html = "<html><head><meta property=\"og:description\" content=\"The Rock rocks\" /></head></html>";
		content = (Webpage) ext.extractContent(createMockResource(TEST_URL, html), serviceLocatorMock);
		assertEquals("The Rock rocks", content.getDescription());

		// twitter:description
		html = "<html><head><meta name=\"twitter:description\" content=\"The Rock rocks2\" /></head></html>";
		content = (Webpage) ext.extractContent(createMockResource(TEST_URL, html), serviceLocatorMock);
		assertEquals("The Rock rocks2", content.getDescription());

		// blank
		html = "<html><head></head></html>";
		content = (Webpage) ext.extractContent(createMockResource(TEST_URL, html), serviceLocatorMock);
		assertNull(content.getDescription());
	}

	@Test
	public void extractPreviewUrlFromMeta() throws IOException, HttpException {

		// meta link image
		String html = "<html><head><meta/><link rel=\"image_src\" href=\"http://web.com/myweb.png\" /></head></html>";
		Webpage content = (Webpage) ext.extractContent(createMockResource(TEST_URL, html), serviceLocatorMock);
		assertEquals("http://web.com/myweb.png", content.getPreviewImage().getUrl());

		// og:image
		html = "<html><head><meta property=\"og:image\" content=\"http://ia.media-imdb.com/images/rock.jpg\" /></head></html>";
		content = (Webpage) ext.extractContent(createMockResource(TEST_URL, html), serviceLocatorMock);
		assertEquals("http://ia.media-imdb.com/images/rock.jpg", content.getPreviewImage().getUrl());

		// twitter:image
		html = "<html><head><meta name=\"twitter:image\" content=\"http://ia.media-imdb.com/images/rock2.jpg\" /></head></html>";
		content = (Webpage) ext.extractContent(createMockResource(TEST_URL, html), serviceLocatorMock);
		assertEquals("http://ia.media-imdb.com/images/rock2.jpg", content.getPreviewImage().getUrl());

		// blank
		html = "<html><head></head></html>";
		content = (Webpage) ext.extractContent(createMockResource(TEST_URL, html), serviceLocatorMock);
		assertNull(content.getPreviewImage());
	}

	private ReadableResource createMockResource(String url, String content) throws IOException, HttpException {
		ReadableResource resource = mock(ReadableResource.class);

		when(resource.getUrl()).thenReturn(new URL(url));
		when(resource.getContentType()).thenReturn(Optional.of(MediaType.HTML_UTF_8));
		when(resource.read()).thenReturn(new ByteArrayInputStream(content.getBytes(Charset.forName("utf8"))));
		when(resource.getRedirectionPath()).thenReturn(Optional.<URL[]> absent());
		return resource;
	}

	private ServiceLocator createServiceLocatorMock() throws IOException, HttpException {
		ServiceLocator mock = Mockito.mock(ServiceLocator.class);
		HttpService httpMock = Mockito.mock(HttpService.class);
		when(mock.getHttpService()).thenReturn(httpMock);
		ImageService imageMock = Mockito.mock(ImageService.class);
		when(imageMock.getImageFromUrl(Mockito.any(URL.class))).thenAnswer(new Answer<Image>() {
			@Override
			public Image answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				return null;
			}
		});
		when(mock.getImageService()).thenReturn(imageMock);
		return mock;
	}
}