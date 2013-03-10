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
package com.lumata.lib.lupa.content;

import java.util.Arrays;
import java.util.Set;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;

/**
 * A Web Content is an abstraction that describes (using metadata) the content of a web resource.
 * 
 * @author Alexander De Leon - alexander.leon@lumatagroup.com
 * 
 */
public abstract class WebContent {

	public static enum Type {
		WEBPAGE,
		IMAGE,
		VIDEO,
		AUDIO,
		FEED
	}

	private final String url;
	private final String[] aliasUrls;
	private Set<String> keywords;

	WebContent(String url, String... aliasUrls) {
		this.url = url;
		this.aliasUrls = aliasUrls;
	}

	/**
	 * Gets the preferred URL of this content
	 * 
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Gets the known alias URLs where this content can be also located. The order is defined by the redirection path.
	 * 
	 * @return the referingUrls
	 */
	public String[] getAliasUrls() {
		return aliasUrls;
	}

	/**
	 * @return the type of this web content.
	 */
	public abstract Type getType();

	/**
	 * @return the keywords of this web content.
	 */
	public Set<String> getKeywords() {
		return keywords;
	}

	/**
	 * @param keywords
	 *            the keywords to set
	 */
	public void setKeywords(Set<String> keywords) {
		this.keywords = keywords;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(url, getType());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof WebContent)) {
			return false;
		}
		WebContent that = (WebContent) obj;
		return Objects.equal(this.url, that.url) && Objects.equal(this.getType(), that.getType())
				&& Arrays.equals(this.aliasUrls, that.aliasUrls) && Objects.equal(this.keywords, that.keywords);
	}

	protected ToStringHelper toStringHelper(Object instance) {
		return Objects.toStringHelper(this).omitNullValues().add("url", url)
				.add("aliasUrls", Arrays.toString(aliasUrls)).add("keywords", keywords);
	}

}
