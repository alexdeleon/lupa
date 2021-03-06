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
package com.lumata.lib.lupa.extractor.internal.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.data.Link;
import com.google.gdata.data.TextConstruct;
import com.google.gdata.data.extensions.Rating;
import com.google.gdata.data.media.mediarss.MediaCategory;
import com.google.gdata.data.media.mediarss.MediaContent;
import com.google.gdata.data.media.mediarss.MediaDescription;
import com.google.gdata.data.media.mediarss.MediaGroup;
import com.google.gdata.data.media.mediarss.MediaKeywords;
import com.google.gdata.data.media.mediarss.MediaThumbnail;
import com.google.gdata.data.youtube.UserProfileEntry;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.util.ServiceException;
import com.lumata.lib.lupa.content.Video;

/**
 * @author Alexander De Leon - alexander.leon@lumatagroup.com
 * 
 */
public class YoutubeService {

	private static final Logger LOG = LoggerFactory.getLogger(YoutubeService.class);

	private static final String BASE_URL = "http://gdata.youtube.com/";

	private static final String YOUTUBE = "youtube.com";

	private final String developerKey;
	private final String applicationName;

	public YoutubeService(String applicationName, String developerKey) {
		this.applicationName = applicationName;
		this.developerKey = developerKey;
	}

	public Video getFeatureVideFromUser(String userId) {
		String profileUrl = BASE_URL + "feeds/api/users/" + userId;
		try {
			YouTubeService service = getService();
			try {
				UserProfileEntry userProfileEntry = service.getEntry(new URL(profileUrl), UserProfileEntry.class);
				if (userProfileEntry == null) {
					LOG.warn("Youtube user profile not found for {}", userId);
				}
				String videoUrl = userProfileEntry.getFeaturedVideoLink().getHref();
				LOG.debug("Dicovered channel {} featured video: {}", userId, videoUrl);
				try {
					return userProfileEntry.getFeaturedVideoLink() != null ? getVideoFromUrl(videoUrl) : null;
				} catch (Exception e) {
					LOG.error("Exception while geting video ({}) info from Youtube: {}", videoUrl, e.getMessage());
				}
			} catch (Exception e) {
				LOG.error("Exception while geting youtube profile for channelId {}: {}", userId, e.getMessage());
			}
		} catch (Exception e) {
			LOG.error("Unable to connecto to youtube api: {}", e.getMessage());
		}
		return null;
	}

	public Video getVideoAsContent(String videoId) {
		String videoUrl = BASE_URL + "feeds/api/videos/" + videoId;
		try {
			return getVideoFromUrl(videoUrl);
		} catch (Exception e) {
			LOG.error("Exception while geting video ({}) info from Youtube: {}", videoUrl, e.getMessage());
		}
		return null;
	}

	/* ------------------------ helper methods -- */
	private Video getVideoFromUrl(String videoUrl) throws MalformedURLException, IOException, ServiceException {
		YouTubeService service = getService();

		VideoEntry entry = service.getEntry(new URL(videoUrl), VideoEntry.class);
		MediaGroup mg = entry.getMediaGroup();
		if (mg.getContents() != null && !mg.getContents().isEmpty()) {
			MediaContent firstContent = mg.getContents().get(0);
			if (firstContent != null && StringUtils.isNotBlank(firstContent.getUrl())) {
				Video video = new Video(firstContent.getUrl());
				video.setHostingService(YOUTUBE);
				TextConstruct title = entry.getTitle();
				if (title != null) {
					video.setTitle(title.getPlainText());
				}
				MediaDescription description = mg.getDescription();
				if (description != null) {
					video.setDescription(description.getPlainTextContent());
				}
				List<MediaThumbnail> thumbnails = mg.getThumbnails();
				if (thumbnails != null && !mg.getThumbnails().isEmpty()) {
					MediaThumbnail firstThumbnail = mg.getThumbnails().get(0);
					video.setThumbnailUrl(firstThumbnail.getUrl());
				}
				Rating rating = entry.getRating();
				if (rating != null) {
					video.setAverageRating(rating.getAverage());
				}
				video.setCategories(validateCategories(mg));
				video.setDuration(firstContent.getDuration());
				MediaKeywords keywords = mg.getKeywords();
				if (keywords != null) {
					video.setKeywords(new HashSet<String>(keywords.getKeywords()));
				}
				Link htmlLink = entry.getHtmlLink();
				if (htmlLink != null) {
					video.setHtmlUrl(htmlLink.getHref());
				}
				return video;
			}
		}
		LOG.warn("Invalid content recieved from youtube while trying to get video entry for {}", videoUrl);
		return null;
	}

	private List<String> validateCategories(MediaGroup mg) {
		if (null != mg.getCategories()) {
			List<String> categories = new ArrayList<String>(mg.getCategories().size());
			// categories
			for (MediaCategory cat : mg.getCategories()) {
				categories.add(cat.getLabel());
			}

			return categories;
		}
		return null;
	}

	private YouTubeService getService() {
		return new YouTubeService(applicationName, developerKey);
	}

}
