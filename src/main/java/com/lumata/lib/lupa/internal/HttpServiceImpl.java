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

import java.net.URL;

import org.apache.http.client.HttpClient;

import com.lumata.lib.lupa.HttpService;
import com.lumata.lib.lupa.ReadableResource;
import com.lumata.lib.lupa.TextReadableResource;

/**
 * @author Alexander De Leon - alexander.leon@lumatagroup.com
 * 
 */
public class HttpServiceImpl implements HttpService {

	private final HttpClient httpClient;

	public HttpServiceImpl(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	@Override
	public ReadableResource getRawResource(URL url) {
		return new ProxiedReadableResource(url, httpClient);
	}

	@Override
	public TextReadableResource getTextResource(URL url) {
		return new TextReadableResourceDecorator(getRawResource(url));
	}

	@Override
	public TextReadableResource getTextResource(ReadableResource resource) {
		return getTextResource(resource.getUrl());
	}

}
