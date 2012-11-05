package com.lumata.lib.webscraper.extractor.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import com.lumata.lib.webscraper.Scraper;
import com.lumata.lib.webscraper.ServiceLocator;
import com.lumata.lib.webscraper.content.Feed;
import com.lumata.lib.webscraper.content.Image;
import com.lumata.lib.webscraper.content.Video;
import com.lumata.lib.webscraper.content.WebContent;
import com.lumata.lib.webscraper.content.Webpage;

public class HtmlExtractorTest {

	private static final String TEST_URL = "http://example.org";

	private HtmlExtractor ext;
	private ServiceLocator serviceLocatorMock;
	private Scraper scraperMock;

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
		stubbScraping("http://web.com/myweb.png", new Image("http://web.com/myweb.png"), Image.class);
		String html = "<html><head><meta/><link rel=\"image_src\" href=\"http://web.com/myweb.png\" /></head></html>";
		Webpage content = (Webpage) ext.extractContent(createMockResource(TEST_URL, html), serviceLocatorMock);
		assertEquals("http://web.com/myweb.png", content.getPreviewImage().getUrl());

		// og:image
		stubbScraping("http://ia.media-imdb.com/images/rock.jpg",
				new Image("http://ia.media-imdb.com/images/rock.jpg"), Image.class);
		html = "<html><head><meta property=\"og:image\" content=\"http://ia.media-imdb.com/images/rock.jpg\" /></head></html>";
		content = (Webpage) ext.extractContent(createMockResource(TEST_URL, html), serviceLocatorMock);
		assertEquals("http://ia.media-imdb.com/images/rock.jpg", content.getPreviewImage().getUrl());

		// twitter:image
		stubbScraping("http://ia.media-imdb.com/images/rock2.jpg", new Image(
				"http://ia.media-imdb.com/images/rock2.jpg"), Image.class);
		html = "<html><head><meta name=\"twitter:image\" content=\"http://ia.media-imdb.com/images/rock2.jpg\" /></head></html>";
		content = (Webpage) ext.extractContent(createMockResource(TEST_URL, html), serviceLocatorMock);
		assertEquals("http://ia.media-imdb.com/images/rock2.jpg", content.getPreviewImage().getUrl());

		// blank
		html = "<html><head></head></html>";
		content = (Webpage) ext.extractContent(createMockResource(TEST_URL, html), serviceLocatorMock);
		assertNull(content.getPreviewImage());
	}

	@Test
	public void getRssFeedAddress() throws IOException, HttpException {
		// rss and atom
		String html = "<html><head><link rel=\"alternate\" type=\"application/rss+xml\" href=\"http://web.com/myweb.rss\" />"
				+ "<link rel=\"alternate\" title=\"hey\" type=\"application/atom+xml\" href=\"http://web.com/myweb2.xml\" /></head></html>";
		Webpage content = (Webpage) ext.extractContent(createMockResource(TEST_URL, html), serviceLocatorMock);

		List<Feed> expected = new ArrayList<Feed>();
		Feed feed1 = new Feed("http://web.com/myweb.rss");
		expected.add(feed1);
		Feed feed2 = new Feed("http://web.com/myweb2.xml");
		feed2.setTitle("hey");
		expected.add(feed2);

		assertEquals(expected.size(), content.getFeeds().size());
		assertTrue(content.getFeeds().containsAll(expected));

		// empty feeds
		html = "<html><head></head></html>";
		content = (Webpage) ext.extractContent(createMockResource(TEST_URL, html), serviceLocatorMock);
		assertNull(content.getFeeds());
	}

	@Test
	public void extractEmbededYoutubeVideo() throws IOException, HttpException {
		// normal http
		stubbScraping("http://www.youtube.com/v/rWPz4f-QgU0?fs=1&hl=en_GB&rel=0", new Video(
				"http://www.youtube.com/v/rWPz4f-QgU0?fs=1&hl=en_GB&rel=0"), Video.class);
		String html = "<html><body><embed type=\"application/x-shockwave-flash\" width=\"210\" height=\"182\" src=\"http://www.youtube.com/v/rWPz4f-QgU0?fs=1&hl=en_GB&rel=0\" allowscriptaccess=\"always\" allowfullscreen=\"true\"></body></html>";
		Webpage content = (Webpage) ext.extractContent(createMockResource(TEST_URL, html), serviceLocatorMock);

		Video expected = new Video("http://www.youtube.com/v/rWPz4f-QgU0?fs=1&hl=en_GB&rel=0");
		assertEquals(expected, content.getEmbeddedContent().get(0));

		// normal https
		stubbScraping("https://www.youtube.com/v/rWPz4f-QgU0?fs=1&hl=en_GB&rel=0", new Video(
				"https://www.youtube.com/v/rWPz4f-QgU0?fs=1&hl=en_GB&rel=0"), Video.class);
		html = "<html><body><embed type=\"application/x-shockwave-flash\" width=\"210\" height=\"182\" src=\"https://www.youtube.com/v/rWPz4f-QgU0?fs=1&hl=en_GB&rel=0\" allowscriptaccess=\"always\" allowfullscreen=\"true\"></body></html>";
		content = (Webpage) ext.extractContent(createMockResource(TEST_URL, html), serviceLocatorMock);
		expected = new Video("https://www.youtube.com/v/rWPz4f-QgU0?fs=1&hl=en_GB&rel=0");
		assertEquals(expected, content.getEmbeddedContent().get(0));

		// watch
		stubbScraping("http://www.youtube.com/watch?v=L9szn1QQfas&fs=0", new Video(
				"http://www.youtube.com/v/L9szn1QQfas"), Video.class);
		html = "<html><body><embed id=\"yt\" src=\"http://www.youtube.com/watch?v=L9szn1QQfas&fs=0\" type=\"application/x-shockwave-flash\" width=\"%width%\" height=\"%height%\"></embed></body></html>";
		content = (Webpage) ext.extractContent(createMockResource(TEST_URL, html), serviceLocatorMock);
		expected = new Video("http://www.youtube.com/v/L9szn1QQfas");
		assertEquals(expected, content.getEmbeddedContent().get(0));

		// html 5
		stubbScraping("http://www.youtube.com/embed/u1zgFlCw8Aw?enablejsapi=1&origin=http://example.com", new Video(
				"http://www.youtube.com/v/u1zgFlCw8Aw"), Video.class);
		html = "<html><body><iframe id=\"player\" type=\"text/html\" width=\"640\" height=\"390\" src=\"http://www.youtube.com/embed/u1zgFlCw8Aw?enablejsapi=1&origin=http://example.com\" frameborder=\"0\"></iframe></body></html>";
		content = (Webpage) ext.extractContent(createMockResource(TEST_URL, html), serviceLocatorMock);
		expected = new Video("http://www.youtube.com/v/u1zgFlCw8Aw");
		assertEquals(expected, content.getEmbeddedContent().get(0));

		// iframe
		stubbScraping("http://www.youtube.com/embed/CDeG4S-mJts",
				new Video("http://www.youtube.com/embed/CDeG4S-mJts"), Video.class);
		html = "<html><body><iframe width=\"560\" height=\"315\" src=\"http://www.youtube.com/embed/CDeG4S-mJts\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
		content = (Webpage) ext.extractContent(createMockResource(TEST_URL, html), serviceLocatorMock);
		expected = new Video("http://www.youtube.com/embed/CDeG4S-mJts");
		assertEquals(expected, content.getEmbeddedContent().get(0));

		// blank
		html = "<html><head></head></html>";
		content = (Webpage) ext.extractContent(createMockResource(TEST_URL, html), serviceLocatorMock);
		assertNull(content.getEmbeddedContent());
	}

	@Test
	public void extractEmbededVimeoVideo() throws IOException, HttpException {
		stubbScraping("http://player.vimeo.com/video/52525176", new Video("http://player.vimeo.com/video/52525176"),
				Video.class);
		String html = "<html><body><iframe src=\"http://player.vimeo.com/video/52525176\" width=\"500\" height=\"281\" frameborder=\"0\" webkitAllowFullScreen mozallowfullscreen allowFullScreen></iframe> <p><a href=\"http://vimeo.com/52525176\">JOEY BADA$$ FT. CHUCK STRANGERS - FROMDATOMB$</a> from <a href=\"http://vimeo.com/davidmhelman\">david m. helman</a> on <a href=\"http://vimeo.com\">Vimeo</a>.</p></body></html>";
		Webpage content = (Webpage) ext.extractContent(createMockResource(TEST_URL, html), serviceLocatorMock);

		Video expected = new Video("http://player.vimeo.com/video/52525176");
		assertEquals(expected, content.getEmbeddedContent().get(0));
	}

	@Test
	public void testHtmlRedirection() throws IOException, HttpException {
		Webpage theRedirectedPage = new Webpage("http://www.the-redirect.com");
		stubbScraping("http://www.the-redirect.com", theRedirectedPage, Webpage.class);
		String html = "<html><head><meta http-equiv=\"REFRESH\" content=\"0;url=http://www.the-redirect.com\"></head></html>";
		Webpage content = (Webpage) ext.extractContent(createMockResource(TEST_URL, html), serviceLocatorMock);
		assertEquals(theRedirectedPage, content);
	}

	@Test
	public void testHtmlRelativeRedirection() throws IOException, HttpException {
		Webpage theRedirectedPage = new Webpage(TEST_URL + "/redirected");
		stubbScraping(theRedirectedPage.getUrl(), theRedirectedPage, Webpage.class);
		String html = "<html><head><meta http-equiv=\"refresh\" content=\"0;url=/redirected\"></head></html>";
		Webpage content = (Webpage) ext.extractContent(createMockResource(TEST_URL, html), serviceLocatorMock);
		assertEquals(theRedirectedPage, content);
	}

	/* --------------------------------- helper methods -- */
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
				return new Image(((URL) invocation.getArguments()[0]).toExternalForm());
			}
		});
		when(mock.getImageService()).thenReturn(imageMock);
		scraperMock = mock(Scraper.class);
		when(mock.getScraper()).thenReturn(scraperMock);
		return mock;
	}

	private <E extends WebContent> void stubbScraping(String url, E mockedContent, Class<E> type) throws IOException,
			HttpException {
		when(scraperMock.extractContentFromUrl(url)).thenReturn(mockedContent);
		when(scraperMock.extractContentFromUrl(url, type)).thenReturn(mockedContent);
	}
}
