package com.example.ballup_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import com.example.ballup_backend.config.GoogleOAuthConfig;
import com.example.ballup_backend.entity.UserEntity;
import com.example.ballup_backend.entity.UserEntity.Role;
import com.example.ballup_backend.projection.user.UserGoogleData;
import com.example.ballup_backend.service.AuthService;
import com.example.ballup_backend.service.UserService;

import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/auth/google")
public class GoogleAuthController {
    private static final String TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v2/userinfo";

    @Autowired
    private GoogleOAuthConfig googleOAuthConfig;

    @Autowired
    private AuthService authService;

    

    @GetMapping("/callback")
    public ResponseEntity<?> googleCallback(@RequestParam("code") String code) {
        String accessToken = getAccessToken(code);
        Map<String, Object> userInfo = getUserInfo(accessToken);
        UserGoogleData userGoogleData = mapToUserGoogleData(userInfo);
        String jwtToken = authService.saveOrUpdateGoogleUser(userGoogleData);
        return ResponseEntity.ok(jwtToken);
    }

    private String getAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, String> body = new HashMap<>();
        body.put("code", code);
        body.put("client_id", googleOAuthConfig.getClientId());
        body.put("client_secret", googleOAuthConfig.getClientSecret());
        body.put("redirect_uri", googleOAuthConfig.getRedirectUri());
        body.put("grant_type", "authorization_code");

        ResponseEntity<Map> response = restTemplate.postForEntity(TOKEN_URL, body, Map.class);
        Map<String, Object> responseBody = response.getBody();

        return responseBody != null ? (String) responseBody.get("access_token") : null;
    }

    private Map<String, Object> getUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(USER_INFO_URL, HttpMethod.GET, entity, Map.class);

        return response.getBody();
    }

    private UserGoogleData mapToUserGoogleData(Map<String, Object> googleUserInfo) {
    return UserGoogleData.builder()
            .googleId((String) googleUserInfo.get("id"))
            .username((String) googleUserInfo.get("email"))
            .email((String) googleUserInfo.get("email"))
            .firstname((String) googleUserInfo.get("given_name"))
            .lastname((String) googleUserInfo.get("family_name"))
            .avatar((String) googleUserInfo.get("picture"))
            .role(Role.USER) 
            .build();
}

}
