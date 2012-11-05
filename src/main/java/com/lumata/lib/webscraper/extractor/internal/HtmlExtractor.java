package com.lumata.lib.webscraper.extractor.internal;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tika.parser.html.HtmlEncodingDetector;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buongiorno.frog.lib.http.HttpException;
import com.google.common.net.MediaType;
import com.lumata.lib.webscraper.ImageService;
import com.lumata.lib.webscraper.ReadableResource;
import com.lumata.lib.webscraper.Scraper;
import com.lumata.lib.webscraper.ServiceLocator;
import com.lumata.lib.webscraper.content.Feed;
import com.lumata.lib.webscraper.content.Image;
import com.lumata.lib.webscraper.content.Video;
import com.lumata.lib.webscraper.content.WebContent;
import com.lumata.lib.webscraper.content.Webpage;
import com.lumata.lib.webscraper.util.URLUtil;

public class HtmlExtractor extends AbstractExtractor<WebContent> {

	private static final Logger LOG = LoggerFactory.getLogger(HtmlExtractor.class);

	private static final String CONFIG_CONTENT_SELECTOR = "contentSelector";
	private static final String CONFIG_CONTENT_SELECTOR_DEFAULT = "article, div[id~=.*article.*ontent.*], div[class~=.*media-instance-container.*],div[class~=.*article.*ontent.*], div[id~=.*article.*], div[class~=.*article.*], div[id~=.*post.*], div[class~=.*post.*], div[class~=.*Contenido.*], div[id~=.*content.*], div[class~=.*content.*], div[id~=.*main.*], div[class~=.*main.*], div[id~=.*divsinglecolumnminwidth.*]";

	private static final String CONFIG_SCRAP_BODY = "scrapBody";
	private static final Boolean CONFIG_SCRAP_BODY_DEFAULT = true;

	private static final String CONFIG_EXCLUDED_ELEMENTS = "excludedElements";
	private static final String CONFIG_EXCLUDED_ELEMENTS_DEFAULT = "div[id~=.*nav.*], div[id~=.*bar.*], div[id~=.*share.*], div[class~=.*nav.*], div[class~=.*share.*], div[class~=.*tags.*], *[style~=.*display:none.*],div[id~=ad-area]";

	private static final Pattern REFRESH_URL_PATTERN = Pattern.compile(".*URL=(.*)");
	private static final int BUFFER_2KB = 2048;
	private static final String UTF8 = "utf8";

	@Override
	public WebContent extractContent(ReadableResource resource, ServiceLocator serviceLocator) throws IOException,
			HttpException {
		try {
			Document doc = parseWithDeclaredCharset(resource);
			return scrapDocument(doc, resource, serviceLocator.getScraper(), serviceLocator.getImageService());
		} catch (RedirectDetected redirection) {
			LOG.debug("An HTML declared redirection has been detected from {} to {}", resource.getUrl(),
					redirection.redirectUrl);
			// TODO can be use the same resource?
			return serviceLocator.getScraper().extractContentFromUrl(redirection.redirectUrl);
		}
	}

	@Override
	public Class<WebContent> getExtractableWebContentType() {
		return WebContent.class;
	}

	/* -----helper methods ---------------------------------- */
	private Document parseWithDeclaredCharset(ReadableResource resource) throws RedirectDetected, IOException,
			HttpException {

		// 1) Create a buffered stream to be able to read ahead
		InputStream stream = new BufferedInputStream(resource.read());
		stream.mark(BUFFER_2KB);

		// 2) detect encoding from HTTP headers or use default (UTF-8)
		Charset httpCharset = getResourceCharsetOrDefaultCharset(resource);
		LOG.debug("Initial charset (http header or default) for {} is {}", resource.getUrl(), httpCharset);

		// 3) Read head section ahead
		String headText = readHead(stream, httpCharset.name(), BUFFER_2KB);
		stream.reset();

		// 4) Look for encodings declared on the HTML and use this one if found
		Charset htmlCharset = getHtmlDeclaredCharset(headText);
		LOG.debug("Final charset (after HTML charset extraction) used for parsing {} is {}", resource.getUrl(),
				htmlCharset);
		String encoding = htmlCharset != null ? htmlCharset.name() : httpCharset.name();

		// 5) Detect any possible redirects declared on the HTML
		detectHtmlDeclaredRedirects(headText, encoding, resource.getUrl());

		// 6) Parse the document
		Document doc = Jsoup.parse(stream, encoding, resource.getUrl().toString());

		return doc;

	}

	private Charset getResourceCharsetOrDefaultCharset(ReadableResource resource) {
		return resource.getContentType().or(MediaType.HTML_UTF_8).charset().or(Charset.forName(UTF8));
	}

	private Charset getHtmlDeclaredCharset(String head) throws IOException {
		HtmlEncodingDetector encodingDetector = new HtmlEncodingDetector();
		return encodingDetector.detect(new ByteArrayInputStream(head.getBytes()), null);
	}

	private void detectHtmlDeclaredRedirects(String headText, String encoding, URL originalUrl) throws RedirectDetected {
		Element head = Jsoup.parse(headText, encoding);
		// if refresh tag is present do redirect
		Element refresh = head.select("meta[http-equiv=refresh]").first();
		if (refresh != null) {
			String content = refresh.attr("content");
			if (content != null) {
				Matcher matcher = REFRESH_URL_PATTERN.matcher(content);
				if (matcher.matches()) {
					String redirectUrl = matcher.group(1);
					try {
						redirectUrl = URLUtil.getAbsoluteUrl(originalUrl, redirectUrl).toString();
						// only redirect if URL is not the same to avoid infinite loops
						if (!originalUrl.toString().equals(redirectUrl)) {
							throw new RedirectDetected(redirectUrl);
						}
					} catch (MalformedURLException e) {
						LOG.warn("Redirect URL ({}) found in {} but is invalid", redirectUrl, originalUrl);
					}
				}
			}
		}

	}

	private WebContent scrapDocument(Document doc, ReadableResource resource, Scraper scraper, ImageService imageService) {
		URL url = resource.getUrl();
		LOG.debug("Scraping HTML page {}", url);
		Webpage webpage = createWebpage(resource);

		/**** Extract content from HEAD ****/
		Element head = doc.head();
		if (head != null) {
			LOG.debug("Scraping head section of the page {}", url);
			webpage.setTitle(extractTitle(head));
			webpage.setDescription(extractDescriptionFromMeta(head));

			extractPreviewImageFromSection(head, webpage, scraper);
			extractKeywordsFromSection(head, webpage);
			extractFeedsFromSection(head, webpage);
		} else {
			LOG.debug("No head section found!");
		}

		/**** Extract content from BODY ****/
		if (getConfigBooleanValue(CONFIG_SCRAP_BODY, CONFIG_SCRAP_BODY_DEFAULT)) {
			Element body = doc.body();
			if (null != body) {
				LOG.debug("Scraping body session of the page {}", url);

				// if no feeds extracted from HEAD try finding them in BODY
				if (CollectionUtils.isEmpty(webpage.getFeeds())) {
					extractFeedsFromSection(body, webpage);
				}

				// select content
				Elements content = body
						.select(getConfigValue(CONFIG_CONTENT_SELECTOR, CONFIG_CONTENT_SELECTOR_DEFAULT));

				// clean selected content
				content.select("script, noscript, style, ul").remove();

				content.select(getConfigValue(CONFIG_EXCLUDED_ELEMENTS, CONFIG_EXCLUDED_ELEMENTS_DEFAULT)).remove();
				removeComments(body);

				// If no image found so far try find one form the HTML
				if (webpage.getPreviewImage() == null) {
					Elements bodyImages = content.select("img");
					try {
						HtmlBiggestImageExtractor imageExtractor = new HtmlBiggestImageExtractor(imageService);
						webpage.setPreviewImage(imageExtractor.extractBestImage(url, bodyImages)); // TODO: set
																									// requirements from
																									// configuration
					} catch (Exception e) {
						LOG.warn("Unable to extract biggest image from {} due to previous error: {}", new Object[] {
								url, e.getMessage() }, e);
					}
				}

				try {
					Video embeddedVideo = extractVideoFromNodeList(content, scraper);
					if (embeddedVideo != null) {
						webpage.addEmbeddedContent(embeddedVideo);
					}
				} catch (Exception e) {
					LOG.warn("Unable to extract embedded video from {} due to previous error: {}", new Object[] { url,
							e.getMessage() }, e);
				}

			} else {
				LOG.debug("No body section found!");
			}
		}
		return webpage;
	}

	private void extractKeywordsFromSection(Element section, Webpage webpage) {
		Set<String> extractKeywordsFromMeta = extractKeywordsFromMeta(section);
		if (CollectionUtils.isNotEmpty(extractKeywordsFromMeta)) {
			webpage.setKeywords(extractKeywordsFromMeta);
		}
	}

	private void extractPreviewImageFromSection(Element section, Webpage webpage, Scraper scraper) {
		String previewImageUrl = extractPreviewUrlFromMeta(section);
		if (StringUtils.isNotBlank(previewImageUrl)) {
			webpage.setPreviewImage(scrapImageUrl(previewImageUrl, scraper));
		}
	}

	private void extractFeedsFromSection(Element section, Webpage webpage) {
		List<Feed> referringFeeds = getRssFeedAddress(section);
		if (CollectionUtils.isNotEmpty(referringFeeds)) {
			webpage.setFeeds(referringFeeds);
		}
	}

	private Webpage createWebpage(ReadableResource res) {
		return res.getRedirectionPath().isPresent() ? new Webpage(URLUtil.asString(res.getUrl()), URLUtil.asString(res
				.getRedirectionPath().orNull())) : new Webpage(URLUtil.asString(res.getUrl()));
	}

	private String readHead(InputStream input, String charset, int maxCharsToRead) throws IOException {
		StringBuilder builder = new StringBuilder(maxCharsToRead);
		Reader reader = new InputStreamReader(input, charset);
		final int pageSize = 256;
		char[] buffer = new char[pageSize];
		int totalRead = 0, read = 0;
		String testString = "";
		do {
			read = reader.read(buffer, 0, pageSize);
			if (read != -1) {
				totalRead += read;
				builder.append(buffer, 0, read);
			}
			testString = builder.substring(Math.max(0, builder.length() - read - "</head".length()), totalRead);
		} while (totalRead < maxCharsToRead && read != -1 && !testString.contains("</head>"));

		return builder.toString();
	}

	private Video extractVideoFromNodeList(Elements nodes, Scraper scraper) throws IOException, HttpException {
		Element embedTag = nodes.select("embed[src*=www.youtube.com], iframe[src*=www.youtube.com]").first();
		if (embedTag != null) {
			return scraper.extractContentFromUrl(embedTag.absUrl("src"), Video.class);
		} else {
			return null;
		}
	}

	private Image scrapImageUrl(String imageUrl, Scraper scraper) {
		try {
			return scraper.extractContentFromUrl(imageUrl, Image.class);
		} catch (Exception e) {
			LOG.error("Exception while extracting image from url {}", imageUrl, e);
			return null; // no image could be found
		}
	}

	private Set<String> extractKeywordsFromMeta(Element head) {
		Set<String> keys = new HashSet<String>();
		Elements metaKeywords = head.select("meta[name=keywords]");
		if (!metaKeywords.isEmpty()) {
			String keywords = metaKeywords.attr("content");
			for (String keyword : keywords.split(",")) {
				if (StringUtils.isNotBlank(keyword)) {
					keys.add(keyword.trim());
				}
			}
		}
		return keys;
	}

	private void removeComments(org.jsoup.nodes.Node node) {
		for (int i = 0; i < node.childNodes().size();) {
			org.jsoup.nodes.Node child = node.childNode(i);
			if (child.nodeName().equals("#comment")) {
				child.remove();
			} else {
				removeComments(child);
				i++;
			}
		}
	}

	private String extractTitle(Element head) {
		String title = null;
		Elements titleTags = head.getElementsByTag("title");
		if (!titleTags.isEmpty()) {
			title = titleTags.text();
		} else {
			Elements metaTitle = head.select("meta[property=og:title],meta[name=twitter:title]");
			if (!metaTitle.isEmpty()) {
				title = metaTitle.attr("content");
			}
		}
		return title;
	}

	private List<Feed> getRssFeedAddress(Element element) {
		List<Feed> ret = new ArrayList<Feed>();
		Elements metaTitle = element.select("link[rel=alternate]").select(
				"link[type=application/rss+xml],link[type=application/atom+xml]");
		for (Element item : metaTitle) {
			String url = item.absUrl("href");
			if (StringUtils.isNotBlank(url)) {
				Feed feedRef = new Feed(url);
				String title = item.attr("title");
				if (StringUtils.isNotBlank(title)) {
					feedRef.setTitle(title);
				}
				ret.add(feedRef);
			}
		}
		return ret;
	}

	private String extractDescriptionFromMeta(Element head) {
		String description = null;
		Elements metaDescription = head.select("meta[name=description]");
		if (!metaDescription.isEmpty()) {
			description = metaDescription.attr("content");
		} else {
			metaDescription = head.select("meta[property=og:description],meta[name=twitter:description]");
			if (!metaDescription.isEmpty()) {
				description = metaDescription.attr("content");
			}
		}
		return description;
	}

	private String extractPreviewUrlFromMeta(Element head) {
		String previewUrl = null;
		Elements metaImage = head.select("link[rel=image_src]");
		if (!metaImage.isEmpty()) {
			previewUrl = metaImage.first().absUrl("href");
		} else {
			metaImage = head.select("meta[property=og:image],meta[name=twitter:image]");
			if (!metaImage.isEmpty() && StringUtils.isNotBlank(metaImage.attr("content"))) {
				previewUrl = metaImage.first().absUrl("content");
			}
		}

		return previewUrl;
	}

	private static class RedirectDetected extends Throwable {

		private static final long serialVersionUID = 1L;
		private final String redirectUrl;

		private RedirectDetected(String url) {
			this.redirectUrl = url;
		}
	}
}
