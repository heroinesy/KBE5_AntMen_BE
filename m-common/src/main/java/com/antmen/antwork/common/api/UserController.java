package com.antmen.antwork.common.api;

import com.antmen.antwork.common.api.request.UserRedirectDto;
import com.antmen.antwork.common.api.response.UserAccessTokenDto;
import com.antmen.antwork.common.api.response.UserGoogleProfileDto;
import com.antmen.antwork.common.domain.entity.User;
import com.antmen.antwork.common.api.request.UserCreateDto;
import com.antmen.antwork.common.api.request.UserLoginDto;
import com.antmen.antwork.common.service.UserGoogleService;
import com.antmen.antwork.common.service.UserService;
import com.antmen.antwork.common.util.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserGoogleService userGoogleService;

    private UserController(UserService userService, JwtTokenProvider jwtTokenProvider, UserGoogleService userGoogleService) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userGoogleService = userGoogleService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> userCreate(@RequestBody UserCreateDto userCreateDto) {
        log.info("userCreate()-1-1");
        User user = userService.create(userCreateDto);

        return new ResponseEntity<>(user.getUserId(), HttpStatus.CREATED);
    }

    /**
     * email, password 검증 후 일반 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<?> dologin(@RequestBody UserLoginDto userLoginDto) {
        log.info("login()");

        // email, password 일치한지 검증
        User user = userService.login(userLoginDto);

        // 일치할 경우 jwt accesstoken 생성
        String jwtToken = jwtTokenProvider.createToken(user.getUserEmail(), user.getUserRole().toString());

        Map<String, Object> loginInfo = new HashMap<>();
        // loginInfo.put("id", user.getUserId());
        loginInfo.put("token", jwtToken);
        loginInfo.put("success", true);
        return new ResponseEntity<>(loginInfo, HttpStatus.OK);
    }

    /**
     * google 인가 코드 검증 후 google 로그인
     */
    @PostMapping("/google/login")
    public ResponseEntity<?> googledologin(@RequestBody UserRedirectDto userRedirectDto) {
        log.info("googlelogin()");

        // accesstoken 발금
        UserAccessTokenDto accessTokenDto = userGoogleService.getAccessToken(userRedirectDto.getCode());

        // 사용자정보 얻기
        UserGoogleProfileDto userGoogleProfileDto = userGoogleService.getGoogleProfile(accessTokenDto.getAccess_token());

        // 회원가입이 되어 있지 않다면 회원가입으로 이동
        User user = userService.getUserByUserId(userGoogleProfileDto.getSub(), userGoogleProfileDto.getEmail(), "GooGle");

        if (user == null) {
            Map<String, Object> loginInfo = new HashMap<>();
            loginInfo.put("user_id", userGoogleProfileDto.getSub());
            loginInfo.put("user_email", userGoogleProfileDto.getEmail());
            loginInfo.put("user_type", "google");
            loginInfo.put("success", false);
            log.info("login_fail()");
            return new ResponseEntity<>(loginInfo, HttpStatus.OK);
        }

        //회원가입이 되어있다면 토큰발급
        String jwtToken = jwtTokenProvider.createToken(user.getUserEmail(), user.getUserRole().toString());

        Map<String, Object> loginInfo = new HashMap<>();
        // loginInfo.put("id", user.getUserId());
        loginInfo.put("token", jwtToken);
        loginInfo.put("success", true);
        log.info("login_success()");
        return new ResponseEntity<>(loginInfo, HttpStatus.OK);
    }
}
