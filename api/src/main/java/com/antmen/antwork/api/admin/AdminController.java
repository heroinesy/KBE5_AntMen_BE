package com.antmen.antwork.api.admin;















@RestController
@RequestMapping("/api/v1/admin/auth")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;


    @PostMapping("/login")
    public ResponseEntity<AdminTokenDto> login(
            @RequestBody UserLoginDto userLoginDto
            ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(adminService.login(userLoginDto));
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            @RequestBody AdminPasswordChangeDto adminPasswordChangeDto,
            @AuthenticationPrincipal AuthUserDto authUserDto
            ){

        Long adminId = authUserDto.getUserIdAsLong();
        adminService.changePassword(adminId, adminPasswordChangeDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
