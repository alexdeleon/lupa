/*
 * 2012 copyright Lumata
 */
package com.lumata.lib.lupa.http;

import java.util.Locale;

/**
 * @author Alexander De Leon - alexander.leon@lumatagroup.com
 * 
 */
public class HttpClientConfiguration {

	private int socketTimeoutInMillis = 5000;
	private int connectionTimeoutInMillis = 5000;
	private int connectionTTLinMillis = 60000;
	private int connectionPoolSize = 200;
	private int maxPerRoute = 5;
	private int maxRedirects = 4;
	private int connectionRetries = 3;
	private Locale defaultlocale = Locale.ENGLISH;

	/**
	 * @return the socketTimeoutInMillis (default 5000)
	 */
	public int getSocketTimeoutInMillis() {
		return socketTimeoutInMillis;
	}

	/**
	 * @param socketTimeoutInMillis
	 *            the socketTimeoutInMillis to set
	 */
	public void setSocketTimeoutInMillis(int socketTimeoutInMillis) {
		this.socketTimeoutInMillis = socketTimeoutInMillis;
	}

	/**
	 * @return the connectionTimeoutInMillis (default 5000)
	 */
	public int getConnectionTimeoutInMillis() {
		return connectionTimeoutInMillis;
	}

	/**
	 * @param connectionTimeoutInMillis
	 *            the connectionTimeoutInMillis to set
	 */
	public void setConnectionTimeoutInMillis(int connectionTimeoutInMillis) {
		this.connectionTimeoutInMillis = connectionTimeoutInMillis;
	}

	/**
	 * @return the connectionTTLinMillis (default 60000)
	 */
	public int getConnectionTTLinMillis() {
		return connectionTTLinMillis;
	}

	/**
	 * @param connectionTTLinMillis
	 *            the connectionTTLinMillis to set
	 */
	public void setConnectionTTLinMillis(int connectionTTLinMillis) {
		this.connectionTTLinMillis = connectionTTLinMillis;
	}

	/**
	 * @return the connectionPoolSize (default 200)
	 */
	public int getConnectionPoolSize() {
		return connectionPoolSize;
	}

	/**
	 * @param connectionPoolSize
	 *            the connectionPoolSize to set
	 */
	public void setConnectionPoolSize(int connectionPoolSize) {
		this.connectionPoolSize = connectionPoolSize;
	}

	/**
	 * @return the maxPerRoute (default 5)
	 */
	public int getMaxPerRoute() {
		return maxPerRoute;
	}

	/**
	 * @param maxPerRoute
	 *            the maxPerRoute to set
	 */
	public void setMaxPerRoute(int maxPerRoute) {
		this.maxPerRoute = maxPerRoute;
	}

	/**
	 * @return the maxRedirects (default 3)
	 */
	public int getMaxRedirects() {
		return maxRedirects;
	}

	/**
	 * @param maxRedirects
	 *            the maxRedirects to set
	 */
	public void setMaxRedirects(int maxRedirects) {
		this.maxRedirects = maxRedirects;
	}

	/**
	 * @return the connectionRetry (default 3)
	 */
	public int getConnectionRetries() {
		return connectionRetries;
	}

	/**
	 * @param numberOfRetries
	 *            the connectionRetry to set
	 */
	public void setConnectionRetries(int numberOfRetries) {
		this.connectionRetries = numberOfRetries;
	}

	/**
	 * @return the defaultlocale
	 */
	public Locale getDefaultlocale() {
		return defaultlocale;
	}

	/**
	 * @param defaultlocale
	 *            the locale used by default on the HTTP headers.
	 */
	public void setDefaultlocale(Locale defaultlocale) {
		this.defaultlocale = defaultlocale;
	}

}
