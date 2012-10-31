/*
 * 2012 copyright Lumata
 */
package com.lumata.lib.webscraper.internal;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buongiorno.frog.lib.http.HttpException;
import com.google.common.base.Optional;
import com.google.common.net.MediaType;
import com.lumata.lib.webscraper.ReadableResource;
import com.lumata.lib.webscraper.TextReadableResource;

/**
 * @author Alexander De Leon - alexander.leon@lumatagroup.com
 * 
 */
public class TextReadableResourceDecorator implements TextReadableResource {

	private static final Logger LOG = LoggerFactory.getLogger(TextReadableResourceDecorator.class);

	private static final int BUFFER_SIZE = 1024;

	private final ReadableResource resource;
	private InputStream bufferedInputStream;

	public TextReadableResourceDecorator(ReadableResource resource) {
		this.resource = resource;
	}

	@Override
	public URL getUrl() {
		return resource.getUrl();
	}

	@Override
	public Optional<URL[]> getRedirectionPath() {
		return resource.getRedirectionPath();
	}

	@Override
	public Optional<MediaType> getContentType() {
		return resource.getContentType();
	}

	@Override
	public void makeRedirection(URL url) {
		resource.makeRedirection(url);
	}

	@Override
	public InputStream read() throws IOException, HttpException {
		return getBufferedInputStream();
	}

	@Override
	public Reader readAsText() throws IOException, HttpException {
		Optional<String> inferEncoding = inferEncoding();
		try {
			if (inferEncoding.isPresent()) {
				return readAsText(Charset.forName(inferEncoding.get()));
			}
		} catch (UnsupportedEncodingException e) {
			LOG.warn("Detected invalid encoding ({}) faling back to platform default ({})", inferEncoding,
					Charset.defaultCharset());
		}
		return readAsText(Charset.defaultCharset());
	}

	@Override
	public Reader readAsText(Charset charset) throws IOException, HttpException {
		LOG.debug("Reading {} using encoding {}", getUrl(), charset);
		try {
			return new InputStreamReader(read(), charset.name());
		} catch (UnsupportedEncodingException e) {
			LOG.warn("Invalid encoding {}", charset);
			return new InputStreamReader(read());
		}
	}

	@Override
	public void discard() {
		resource.discard();
	}

	private Optional<String> inferEncoding() throws IOException, HttpException {
		Optional<MediaType> possibleMediaType = getContentType();
		if (possibleMediaType.isPresent()) {
			LOG.debug("Inferting encoding of {} from returned content type", getUrl());
			Optional<Charset> possibleCharset = possibleMediaType.get().charset();
			if (possibleCharset.isPresent()) {
				String encoding = possibleCharset.get().name();
				LOG.debug("Encoding for {} is {}", getUrl(), encoding);
				return Optional.fromNullable(encoding);
			}
		}
		LOG.debug("Infering encoding of {} by guessing from stream", getUrl());
		InputStream in = read();
		in.mark(BUFFER_SIZE);
		String guestEncoding = URLConnection.guessContentTypeFromStream(in);
		LOG.debug("Encoding for {} is {}", getUrl(), guestEncoding);
		in.reset();
		return Optional.fromNullable(guestEncoding);
	}

	private InputStream getBufferedInputStream() throws IOException, HttpException {
		if (bufferedInputStream == null) {
			InputStream originalStream = resource.read();
			if (originalStream.markSupported()) {
				bufferedInputStream = originalStream;
			} else {
				bufferedInputStream = new BufferedInputStream(originalStream);
			}
		}
		return bufferedInputStream;
	}
}
