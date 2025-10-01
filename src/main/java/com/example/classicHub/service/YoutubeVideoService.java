package com.example.classicHub.service;

import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.classicHub.entity.Video;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class YoutubeVideoService {

	private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${youtube.api.key}")
    private String apiKey;

    // 1. videoId 목록 가져오기
    public List<String> searchVideoIds(String query, int maxResults) throws Exception {
    	String url = String.format(
    		    "https://www.googleapis.com/youtube/v3/search?part=snippet&channelId=%s&type=video&order=date&maxResults=%d&key=%s",
    		    "UCC7feCGvRmHZgmFP6aOWYBw", maxResults, apiKey
    		);


    	
    	System.out.println("com.example.classicHub.service.YoutubeVideoService : " + url);
        String response = restTemplate.getForObject(url, String.class);
        JsonNode root = objectMapper.readTree(response);

        List<String> videoIds = new ArrayList<>();
        for (JsonNode item : root.get("items")) {
            videoIds.add(item.get("id").get("videoId").asText());
        }
        return videoIds;
    }

    // 2. 상세정보 가져오기
    public List<Video> fetchVideos(List<String> videoIds) throws Exception {
        String url = String.format(
                "https://www.googleapis.com/youtube/v3/videos?part=snippet,contentDetails,statistics&id=%s&key=%s",
                String.join(",", videoIds), apiKey);

        System.out.println(url);
        String response = restTemplate.getForObject(url, String.class);
        JsonNode root = objectMapper.readTree(response);

        List<Video> videos = new ArrayList<>();
        for (JsonNode item : root.get("items")) {
            Video dto = new Video();
            dto.setYtId(item.get("id").asText());
            dto.setTitle(item.get("snippet").get("title").asText());
            dto.setDesc(item.get("snippet").get("description").asText());
            dto.setUploadAt(LocalDateTime.parse(
                    item.get("snippet").get("publishedAt").asText().replace("Z", "")));
            dto.setVideoUrl("https://www.youtube.com/watch?v=" + dto.getYtId());
            dto.setThumbnailUrl(item.get("snippet").get("thumbnails").get("high").get("url").asText());
            dto.setViewCount(item.get("statistics").get("viewCount").asInt());
            dto.setLikeCount(item.get("statistics").path("likeCount").asInt(0));
            dto.setDuration(parseDuration(item.get("contentDetails").get("duration").asText()));
            videos.add(dto);
            
            System.out.println(dto.toString());
        }
        return videos;
    }

    // ISO8601 Duration → 초 단위 변환
    private int parseDuration(String duration) {
        return (int) java.time.Duration.parse(duration).getSeconds();
    }
}
