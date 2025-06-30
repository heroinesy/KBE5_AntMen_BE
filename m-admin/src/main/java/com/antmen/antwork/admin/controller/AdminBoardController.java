package com.antmen.antwork.admin.controller;

import com.antmen.antwork.common.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/board")
@RequiredArgsConstructor
public class AdminBoardController {

    private final BoardService boardService;


}
