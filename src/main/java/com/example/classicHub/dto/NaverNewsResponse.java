package com.example.classicHub.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

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
        
        public String getPubDate() {
	        // 1) 입력 포맷 정의 (RFC 822)
	        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(
	                "EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);

	        // 2) 출력 포맷 정의 (DB 형식)
	        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(
	                "yyyy-MM-dd HH:mm:ss");

	        // 3) 변환
	        ZonedDateTime zonedDateTime = ZonedDateTime.parse(this.pubDate, inputFormatter);
	        String formatted = zonedDateTime.format(outputFormatter);
	        
	        return formatted;
        }
    }
}
