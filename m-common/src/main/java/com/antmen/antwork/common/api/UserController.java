package com.antmen.antwork.common.api;

import com.antmen.antwork.common.domain.entity.User;
import com.antmen.antwork.common.dto.UserCreateDto;
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

        return new ResponseEntity<>(user.getUserId(), HttpStatus.CREATED);
    }
}
