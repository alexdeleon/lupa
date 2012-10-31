/*
 * 2012 copyright Lumata
 */
package com.lumata.lib.webscraper.internal;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.concurrent.NotThreadSafe;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.net.MediaType;
import com.lumata.lib.webscraper.ReadableResource;

/**
 * @author Alexander De Leon - alexander.leon@lumatagroup.com
 * 
 */
@NotThreadSafe
public class ProxiedReadableResource extends AbstractBaseReadableResourceBean implements ReadableResource {

	private static final Logger LOG = LoggerFactory.getLogger(ProxiedReadableResource.class);

	private final HttpClient client;
	private final Set<InputStream> openInputStreams;

	ProxiedReadableResource(URL url, HttpClient client) {
		super(url);
		this.client = client;
		openInputStreams = new HashSet<InputStream>();
	}

	@Override
	public Optional<MediaType> getContentType() {
		if (!super.getContentType().isPresent()) {
			doHttpHeadToReadContentTypeFromHeader();
		}
		return super.getContentType();
	}

	@Override
	public InputStream read() throws IOException {
		HttpContext locaContext = new BasicHttpContext();
		String requestUrl = getUrl().toString();
		HttpGet get = new HttpGet(requestUrl);
		HttpResponse response = client.execute(get, locaContext);

		String targetUrl = getTargetUrl(locaContext);
		if (!targetUrl.equals(requestUrl)) {
			makeRedirection(new URL(targetUrl));
		}

		if (response.containsHeader(HttpHeaders.CONTENT_TYPE)) {
			setContentTypeFromHeader(response);
		}
		InputStream stream = response.getEntity().getContent();
		markAsOpen(stream);
		return stream;
	}

	@Override
	public void discard() {
		for (InputStream stream : openInputStreams) {
			try {
				stream.close();
			} catch (IOException e) {
				LOG.warn("Unable to close stream  on this resource {}", this, e);
			}
		}
	}

	private void markAsOpen(InputStream stream) {
		openInputStreams.add(stream);
	}

	private void doHttpHeadToReadContentTypeFromHeader() {
		String url = getUrl().toString();
		LOG.debug("Invoking HTTP HEAD on {}", url);
		HttpHead head = new HttpHead(url);
		try {
			HttpResponse response = client.execute(head);
			if (response.getStatusLine().getStatusCode() < 300) {
				setContentTypeFromHeader(response);
			}
		} catch (IOException e) {
			LOG.error("Unable to fetch content type using HTTP HEAD", e);
		}
	}

	private void setContentTypeFromHeader(HttpResponse response) {
		Header header = response.getFirstHeader(HttpHeaders.CONTENT_TYPE);
		if (header != null && header.getValue() != null) {
			setContentType(MediaType.parse(header.getValue()));
		}
	}

	/**
	 * Gets the final URL reached on the current request after redirections
	 * 
	 * @param locaContext
	 *            the context used on the HTTP request execution
	 * @return the target URL
	 */
	private String getTargetUrl(HttpContext locaContext) {
		HttpUriRequest currentReq = (HttpUriRequest) locaContext.getAttribute(ExecutionContext.HTTP_REQUEST);
		HttpHost currentHost = (HttpHost) locaContext.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
		return currentHost.toURI() + currentReq.getURI();
	}

}
