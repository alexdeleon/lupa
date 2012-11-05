/*
 * 2012 copyright Lumata
 */
package com.lumata.lib.webscraper.content;

import java.util.Arrays;
import java.util.Collection;

import com.google.common.base.Objects;

/**
 * @author Alexander De Leon - alexander.leon@lumatagroup.com
 * 
 */
public class Video extends WebContent {

	private String title;
	private String hostingService;
	private String description;
	private String thumbnailUrl;
	private Integer duration;
	private Double averageRating;
	private String[] categories;
	private String htmlUrl;

	public Video(String url, String... aliasUrls) {
		super(url, aliasUrls);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public Double getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(Double averageRating) {
		this.averageRating = averageRating;
	}

	public void setAverageRating(Float averageRating) {
		this.averageRating = averageRating != null ? averageRating.doubleValue() : null;
	}

	public String[] getCategories() {
		return categories;
	}

	public void setCategories(String[] categories) {
		this.categories = categories;
	}

	public void setCategories(Collection<String> categories) {
		this.categories = categories != null ? categories.toArray(new String[categories.size()]) : null;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getHostingService() {
		return hostingService;
	}

	public void setHostingService(String hostingService) {
		this.hostingService = hostingService;
	}

	public void setHtmlUrl(String htmlUrl) {
		this.htmlUrl = htmlUrl;
	}

	public String getHtmlUrl() {
		return htmlUrl;
	}

	@Override
	public Type getType() {
		return Type.VIDEO;
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj) || !(obj instanceof Video)) {
			return false;
		}
		Video that = (Video) obj;
		return Objects.equal(this.title, that.title) && Objects.equal(this.description, that.description)
				&& Objects.equal(this.thumbnailUrl, that.thumbnailUrl)
				&& Objects.equal(this.averageRating, that.averageRating)
				&& Arrays.equals(this.categories, that.categories) && Objects.equal(this.duration, that.duration)
				&& Objects.equal(this.hostingService, that.hostingService) && Objects.equal(this.htmlUrl, that.htmlUrl);
	}

	@Override
	public String toString() {
		return toStringHelper(this).add("title", title).add("description", description)
				.add("thumbnailUrl", thumbnailUrl).add("averageRating", averageRating)
				.add("categories", Arrays.toString(categories)).add("duration", duration)
				.add("hostingService", hostingService).add("htmlUrl", htmlUrl).toString();
	}
}
