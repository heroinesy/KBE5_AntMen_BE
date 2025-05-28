package com.antmen.antwork.common.service;

import com.antmen.antwork.common.api.response.UserAccessTokenDto;
import com.antmen.antwork.common.api.response.UserGoogleProfileDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
public class UserGoogleService {

    @Value("${oauth.google.client-id}")
    private String userGoogleClientId;

    @Value("${oauth.google.client-secret}")
    private String userGoogleClientSecret;

    @Value("${oauth.google.redirect-uri}")
    private String userGoogleRedirectUri;

    public UserAccessTokenDto getAccessToken(String code) {
        // 인가코드, clientId, client_secret, redirect_uri, grant_type

        // spring6부터 RestTemplate 비추천상태이므로 RestClient 사용
        RestClient restClient = RestClient.create();

        // MaltiValueMap을 통해 자동으로 form-data형식으로 body 조립 가능
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", userGoogleClientId);
        params.add("client_secret", userGoogleClientSecret);
        params.add("redirect_uri", userGoogleRedirectUri);
        params.add("grant_type", "authorization_code");

        ResponseEntity<UserAccessTokenDto> response = restClient.post()
                .uri("https://oauth2.googleapis.com/token")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(params)
                .retrieve()// retrieve: 응답 body 값만을 추출
                .toEntity(UserAccessTokenDto.class);

        System.out.println("응답 JSON " + response.getBody());
        return response.getBody();
    }

    public UserGoogleProfileDto getGoogleProfile(String token){
        RestClient restClient = RestClient.create();

        ResponseEntity<UserAccessTokenDto> response = restClient.get()
                .uri("https://openidconnect.googleapis.com/v1/userinfo")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(params)
                .retrieve()// retrieve: 응답 body 값만을 추출
                .toEntity(UserAccessTokenDto.class);
        System.out.println("token : " + token);
        return null;
    }
}
