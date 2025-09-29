package com.example.classicHub.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.classicHub.dto.NaverNewsResponse;
import com.example.classicHub.dto.NaverNewsResponse.NaverNewsItem;
import com.fasterxml.jackson.databind.ObjectMapper;


// 네이버 검색 API 예제 - 블로그 검색
@Service
public class NewsSearchService {

	private static String clientId; 	//애플리케이션 클라이언트 아이디
	private static String clientSecret; //애플리케이션 클라이언트 시크릿
	
	/** 생성자 **/
	public NewsSearchService(@Value("${spring.naver.id}")String clientId
							,@Value("${spring.naver.secret}")String clientSecret) {

		this.clientId = clientId;
		this.clientSecret = clientSecret;
    }
	
    public static void main(String[] args) {
    	
        String text = null;
        try {
            text = URLEncoder.encode("임윤찬", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("검색어 인코딩 실패",e);
        }


        String apiURL = "https://openapi.naver.com/v1/search/news?sort=sim&query=" + text;    // JSON 결과
        // String apiURL = "https://openapi.naver.com/v1/search/news.xml?sort=sim&query="+ text; // XML 결과


        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);
        String responseBody = get(apiURL,requestHeaders);
        
    }


    private static String get(String apiUrl, Map<String, String> requestHeaders){
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }


            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 오류 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }


    private static HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }


    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);


        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();


            String line;
            while ((line = lineReader.readLine()) != null) {
            	// System.out.println(line);
                responseBody.append(line);
            }

            ObjectMapper objectMapper = new ObjectMapper();

	        // response.toString() 은 JSON 문자열
	        NaverNewsResponse nnr = objectMapper.readValue(responseBody.toString(), NaverNewsResponse.class);
	         
	        List<NaverNewsItem> list = nnr.getItems();

	        // 1) 입력 포맷 정의 (RFC 822)
	        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(
	                "EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);

	        // 2) 출력 포맷 정의 (DB 형식)
	        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(
	                "yyyy-MM-dd HH:mm:ss");

	        // 3) 변환
	        ZonedDateTime zonedDateTime = ZonedDateTime.parse(list.getFirst().getPubDate(), inputFormatter);
	        String formatted = zonedDateTime.format(outputFormatter);

	        System.out.println("변환 결과: " + formatted);
			/*
			 * for (NaverNewsItem naverNewsItem : list) {
			 * System.out.println(naverNewsItem.toString()); }
			 */


            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는 데 실패했습니다.", e);
        }
    }
}