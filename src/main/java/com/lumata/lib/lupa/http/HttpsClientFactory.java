package com.lumata.lib.lupa.http;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SchemeSocketFactory;
import org.apache.http.impl.client.AutoRetryHttpClient;
import org.apache.http.impl.client.DecompressingHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultServiceUnavailableRetryStrategy;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpsClientFactory {

	private static final Logger LOG = LoggerFactory.getLogger(HttpsClientFactory.class);

	private final ServiceUnavailableRetryStrategy retryHandler;

	private final ClientConnectionManager cm;

	private final HttpParams clientParams;

	/**
	 * Constructor using default configuration
	 */
	public HttpsClientFactory() {
		this(new HttpClientConfiguration());
	}

	public HttpsClientFactory(HttpClientConfiguration config) {
		SchemeRegistry schemeRegistry = new SchemeRegistry();

		SchemeSocketFactory ssf = org.apache.http.conn.ssl.SSLSocketFactory.getSocketFactory();
		schemeRegistry.register(new Scheme("https", 443, ssf));
		schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
		if (config.getConnectionPoolSize() > 1) {
			PoolingClientConnectionManager poolCm = new PoolingClientConnectionManager(schemeRegistry,
					config.getConnectionTTLinMillis(), TimeUnit.MICROSECONDS);
			poolCm.setMaxTotal(config.getConnectionPoolSize());
			poolCm.setDefaultMaxPerRoute(config.getMaxPerRoute());
			cm = poolCm;
		} else {
			cm = null;
		}

		clientParams = new BasicHttpParams();
		DefaultHttpClient.setDefaultHttpParams(clientParams);
		HttpConnectionParams.setConnectionTimeout(clientParams, config.getConnectionTimeoutInMillis());
		HttpConnectionParams.setSoTimeout(clientParams, config.getSocketTimeoutInMillis());
		clientParams.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		clientParams.setParameter(ClientPNames.MAX_REDIRECTS, config.getMaxRedirects());
		clientParams.setParameter(ClientPNames.DEFAULT_HEADERS, createDefaultHeaders(config));

		retryHandler = new DefaultServiceUnavailableRetryStrategy(config.getConnectionRetries(),
				(int) TimeUnit.SECONDS.toMillis(1));
	}

	/**
	 * Returns a thread safe HTTP client having a request-retry handler that retries sending a request up to <b>3</b>
	 * times if it’s safe to do so (if we’re dealing with idempotent requests), and so forth. <br/>
	 * It also registers a handler for HTTPS (HTTP over SSL) URLs on port 443. A free connection is taken from a
	 * connection pool whenever a thread wants to send an HTTP request. Once the thread closes the connection, the
	 * manager doesn’t close it, but puts it back into the pool for other threads to reuse.
	 */
	public org.apache.http.client.HttpClient getHttpsThreadSafeClient() {
		LOG.debug("Creating HttpClient instance");
		HttpClient httpsClient = new DefaultHttpClient(cm, clientParams);
		return new DecompressingHttpClient(new AutoRetryHttpClient(httpsClient, retryHandler));
	}

	/**
	 * Constructs the HTTP headers used by default on the clients constructed with this factory.
	 * 
	 * @param config
	 * @return
	 */
	private Collection<BasicHeader> createDefaultHeaders(HttpClientConfiguration config) {
		return Collections.singleton(new BasicHeader("Accept-Language", config.getDefaultlocale().toString()));
	}

}
