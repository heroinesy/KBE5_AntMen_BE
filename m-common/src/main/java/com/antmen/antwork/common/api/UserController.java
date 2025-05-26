package com.antmen.antwork.common.api;

import com.antmen.antwork.common.domain.entity.User;
import com.antmen.antwork.common.api.request.UserCreateDto;
import com.antmen.antwork.common.api.request.UserLoginDto;
import com.antmen.antwork.common.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
@Slf4j
public class UserController {

    private final UserService userService;

    private UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> userCreate(@RequestBody UserCreateDto userCreateDto) {
        log.info("userCreate()");
        User user = userService.create(userCreateDto);

        String msg = "test";
        ResponseEntity.status(HttpStatus.CREATED).body(msg);

        return new ResponseEntity<>(user.getUserId(), HttpStatus.CREATED);
    }

    /**
     * email, password 검증 후 로그인
     */
    @PostMapping("/dologin")
    public ResponseEntity<?> dologin(@RequestBody UserLoginDto userLoginDto) {
        log.info("dologin()");

        // email, password 일치한지 검증
        User user = userService.login(userLoginDto);

        // 일치할 경우 jwt accesstoken 생성


        return new ResponseEntity<>(user.getUserId(), HttpStatus.CREATED);
    }
}
