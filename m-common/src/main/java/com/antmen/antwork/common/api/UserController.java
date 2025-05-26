package com.antmen.antwork.common.api;

import com.antmen.antwork.common.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
public class UserController {

    private final UserService userService;

    private UserController(final UserService userService) {
        this.userService = userService;
    }
}
