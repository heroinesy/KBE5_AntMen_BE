package com.antmen.antwork.admin.controller;

import com.antmen.antwork.common.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/board")
@RequiredArgsConstructor
public class AdminBoardController {

    private final BoardService boardService;

    @GetMapping("list/{usertype}/{boardType}")
    public ResponseEntity getBoardList(
            @PathVariable String usertype,
            @PathVariable String boardType,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String filter,
            @PageableDefault(size = 10) Pageable pageable
            ){
        return ResponseEntity.status(HttpStatus.OK)
                .body(boardService.getBoardAdminList(usertype, boardType, name, sortBy, filter, pageable));
    }
}
