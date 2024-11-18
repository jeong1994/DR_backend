package com.dr.service.chatBot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.json.JSONObject;
import org.json.JSONArray;

@Service
public class ChatBotService {
    // application.properties에서 api키와 url 가져오기
    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getChatbotResponse(String message) throws Exception {
        // OpenAI API 요청 JSON 작성
        JSONObject requestJson = new JSONObject();
        requestJson.put("model", "gpt-4"); // 모델 값 영향 안받음 어차피 gpt-3

        // 'messages' 필드로 사용자 메시지를 추가
        JSONArray messages = new JSONArray();
        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        userMessage.put("content", message);
        messages.put(userMessage);

        requestJson.put("messages", messages); // 필수 파라미터 추가
        requestJson.put("max_tokens", 1000); //최대 글자 수
        requestJson.put("temperature", 0.7); //창의성

        // HTTP 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<String> entity = new HttpEntity<>(requestJson.toString(), headers);

        // OpenAI API 호출
        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl, HttpMethod.POST, entity, String.class);

        // 응답 파싱 - 'choices' 배열의 'message' 객체 내 'content' 필드 추출
        JSONObject responseJson = new JSONObject(response.getBody());
        JSONArray choicesArray = responseJson.getJSONArray("choices");
        JSONObject messageObject = choicesArray.getJSONObject(0).getJSONObject("message");

//        System.out.println("챗봇 응답 : " + messageObject.getString("content").trim());

        // 응답 반환
//      return messageObject.getString("content").trim();

        // 받은 응답을 줄바꿈 처리를 추가해 반환
        String botReply = messageObject.getString("content").trim();
        return botReply.replace("\n", "<br>");  // \n을 <br>로 변환

    }
}
