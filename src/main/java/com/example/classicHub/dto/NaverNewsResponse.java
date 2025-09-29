package com.example.classicHub.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // JSON에 없는 필드 무시
public class NaverNewsResponse {
    private String lastBuildDate;
    private int total;
    private int start;
    private int display;
    private List<NaverNewsItem> items;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NaverNewsItem {
        private String title;        // 기사 제목 (HTML 태그 포함됨 <b>)
        private String originallink; // 원본 기사 URL
        private String link;         // 네이버 뉴스 URL
        private String description;  // 요약
        private String pubDate;      // 발행일
    }
}
