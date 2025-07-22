package com.antmen.antwork.api.admin;







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
            @RequestParam(required = false) String sortBy
            ){
        return ResponseEntity.status(HttpStatus.OK)
                .body(boardService.getBoardAdminList(usertype, boardType, name, sortBy));
    }
}
