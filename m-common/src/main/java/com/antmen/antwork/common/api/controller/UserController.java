package com.antmen.antwork.common.api.controller;

import com.antmen.antwork.common.api.request.account.UserRedirectDto;
import com.antmen.antwork.common.api.response.account.UserAccessTokenDto;
import com.antmen.antwork.common.api.response.account.UserGoogleProfileDto;
import com.antmen.antwork.common.domain.entity.account.User;
import com.antmen.antwork.common.api.request.account.UserLoginDto;
import com.antmen.antwork.common.domain.entity.account.UserRole;
import com.antmen.antwork.common.service.serviceAccount.ManagerService;
import com.antmen.antwork.common.service.serviceAccount.UserGoogleService;
import com.antmen.antwork.common.service.serviceAccount.UserService;
import com.antmen.antwork.common.util.AuthUserDto;
import com.antmen.antwork.common.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserGoogleService userGoogleService;
    private final ManagerService managerService;

    /**
     * email, password 검증 후 일반 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<?> dologin(@RequestBody UserLoginDto userLoginDto) {
        // email, password 일치한지 검증
        User user = userService.login(userLoginDto);

        // 일치할 경우 jwt accesstoken 생성
        String jwtToken = jwtTokenProvider.createToken(user.getUserId(), user.getUserRole().toString());

        Map<String, Object> loginInfo = new HashMap<>();
        // loginInfo.put("id", user.getUserId());
        loginInfo.put("token", jwtToken);
        loginInfo.put("success", true);

        if (user.getUserRole() == UserRole.MANAGER) {
            loginInfo.put("managerStatus", managerService.getManagerStatus(user.getUserId()));
        }

        return new ResponseEntity<>(loginInfo, HttpStatus.OK);
    }

    /**
     * google 인가 코드 검증 후 google 로그인
     */
    @PostMapping("/google/login")
    public ResponseEntity<?> googledologin(@RequestBody UserRedirectDto userRedirectDto) {
        // accesstoken 발금
        UserAccessTokenDto accessTokenDto = userGoogleService.getAccessToken(userRedirectDto.getCode());

        // 사용자정보 얻기
        UserGoogleProfileDto userGoogleProfileDto = userGoogleService.getGoogleProfile(accessTokenDto.getAccess_token());

        log.info("userGoogleProfileDto.getSub() : {}",userGoogleProfileDto.getSub());
        // 회원가입이 되어 있지 않다면 회원가입으로 이동
        User user = userService.getUserByUserLoginId(userGoogleProfileDto.getSub(), userGoogleProfileDto.getEmail(), "GooGle");

        if (user == null) {
            Map<String, Object> loginInfo = new HashMap<>();
            loginInfo.put("user_id", userGoogleProfileDto.getSub());
            loginInfo.put("user_email", userGoogleProfileDto.getEmail());
            loginInfo.put("user_type", "GOOGLE");
            loginInfo.put("success", false);
            log.info("login_fail()");
            return new ResponseEntity<>(loginInfo, HttpStatus.OK);
        }

        //회원가입이 되어있다면 토큰발급
        String jwtToken = jwtTokenProvider.createToken(user.getUserId(), user.getUserRole().toString());

        Map<String, Object> loginInfo = new HashMap<>();
        // loginInfo.put("id", user.getUserId());
        loginInfo.put("token", jwtToken);
        loginInfo.put("success", true);
        log.info("login_success()");
        return new ResponseEntity<>(loginInfo, HttpStatus.OK);
    }

    @GetMapping("/check-id")
    public ResponseEntity<Map<String, Boolean>> checkLoginId(@RequestParam String loginId) {
        boolean exists = userService.existsByLoginId(loginId);

        Map<String, Boolean> response = new HashMap<>();
        response.put("available", !exists);

        return ResponseEntity.ok(response);
    }


}
