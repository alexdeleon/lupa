package com.lumata.lib.webscraper.extractor.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lumata.lib.webscraper.ReadableResource;
import com.lumata.lib.webscraper.ServiceLocator;
import com.lumata.lib.webscraper.content.Video;
import com.lumata.lib.webscraper.extractor.internal.webapi.YoutubeApi;

public class YouTubeExtractor extends AbstractExtractor<Video> {

	private static final Logger LOG = LoggerFactory.getLogger(YouTubeExtractor.class);

	// a bit messy
	private static final String YOUTUBE_VIDEO_REGEX = "https?://(?:(?:youtu\\.be/|"
			+ "(?:(?:www\\.)?youtube\\.com(?:v=|/v/|/embed/|/|/watch/?\\?v=)?)" + "))([\\w\\-_]{11})(?:[^\\w\\-_]+.*)?";
	private static final Pattern YOUTUBE_VIDEO_ID_PATTERN = Pattern.compile(YOUTUBE_VIDEO_REGEX);
	private static final String YOUTUBE_CHANNEL_REGEX = "https?://(?:youtu\\.be/|(?:(?:www\\.)?youtube\\.com))/user/(.*)";
	private static final Pattern YOUTUBE_CHANNEL_ID_PATTERN = Pattern.compile(YOUTUBE_CHANNEL_REGEX);

	private static final String CONFIG_DEVELOPER_KEY = "youtube.developerKey";
	private static final String CONFIG_APP_NAME = "youtube.appName";
	private static final String CONFIG_APP_NAME_DEFAULT = "Lumata Webscraper";

	@Override
	public Class<Video> getExtractableWebContentType() {
		return Video.class;
	}

	@Override
	public Video extractContent(ReadableResource resource, ServiceLocator serviceLocator) {
		String url = resource.getUrl().toExternalForm();
		String videoId = getVideoIdFromUrl(url);
		if (videoId != null) {
			return getContentFromVideoId(videoId);
		}

		String channelId = getChannelIdFromUrl(url);
		if (channelId != null) {
			return getContentFromChannelId(channelId);
		}
		LOG.warn("Url " + url + " didn't match YouTube extraction regex. Is the Extractor configured correctly?");
		return null;
	}

	private Video getContentFromChannelId(String channelId) {
		LOG.debug("Getting from youtube featured video from channel id: {}", channelId);
		YoutubeApi api = new YoutubeApi(getConfigValue(CONFIG_APP_NAME, CONFIG_APP_NAME_DEFAULT),
				getConfigValue(CONFIG_DEVELOPER_KEY));

		return api.getFeatureVideFromUser(channelId);
	}

	private Video getContentFromVideoId(String videoId) {
		LOG.debug("Getting from youtube video with id: {}", videoId);
		YoutubeApi api = new YoutubeApi(getConfigValue(CONFIG_APP_NAME, CONFIG_APP_NAME_DEFAULT),
				getConfigValue(CONFIG_DEVELOPER_KEY));
		return api.getVideoAsContent(videoId);
	}

	public static String getVideoIdFromUrl(String url) {
		Matcher m = YOUTUBE_VIDEO_ID_PATTERN.matcher(url);
		if (!m.matches()) {
			return null;
		}
		return m.group(1);
	}

	public static String getChannelIdFromUrl(String url) {
		Matcher m = YOUTUBE_CHANNEL_ID_PATTERN.matcher(url);
		if (!m.matches()) {
			return null;
		}
		return m.group(1);
	}

}
