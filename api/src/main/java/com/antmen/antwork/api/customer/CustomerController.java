package com.antmen.antwork.api.customer;

import com.antmen.antwork.core.security.dto.AuthUserDto;
import com.antmen.antwork.domain.user.dto.*;
import com.antmen.antwork.domain.user.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CustomerResponse> signUp(
            @Valid @ModelAttribute CustomerSignupRequest customerSignupRequest) {

        try {
            customerService.signUp(customerSignupRequest);
        } catch (IOException e) {
            throw new RuntimeException("S3 업로드 중 오류가 발생했습니다."); // custom으로 수정 필요
        }

        CustomerResponse response = CustomerResponse.builder()
                .message("회원가입이 완료되었습니다.")
                .build();

        return ResponseEntity.ok(response);
    }

    @Value("${test.value}")
    private String testValue;

    @GetMapping("/test")
    public String test() {
        return "test" + testValue;
    }

    // 전체조회
    @GetMapping
    public ResponseEntity<List<CustomerProfileResponse>> getCustomer() {
        return ResponseEntity.ok(customerService.getCustomers());
    }

    // 1건 조회
    @GetMapping("/{userId}")
    public ResponseEntity<CustomerProfileResponse> getCustomer(@PathVariable("userId") Long id) {
        return ResponseEntity.ok(customerService.getCustomer(id));
    }

    @GetMapping("/me")
    public ResponseEntity<CustomerProfileResponse> getProfile(
            @AuthenticationPrincipal AuthUserDto authUserDto
    ) {
        CustomerProfileResponse response = customerService.getProfile(authUserDto.getUserIdAsLong());

        return ResponseEntity.ok(response);

    }

    @PutMapping("/me")
    public ResponseEntity<CustomerProfileResponse> updateProfile(
            @AuthenticationPrincipal AuthUserDto authUserDto,
            @RequestBody @Valid CustomerUpdateRequest customerUpdateRequest) {
        CustomerProfileResponse response = customerService.updateProfile(authUserDto.getUserIdAsLong(), customerUpdateRequest);

        return ResponseEntity.ok(response);

    }

    // 프로필만 수정
    @PutMapping("/me/profile")
    public ResponseEntity<Map<String, String>> updateProfile(
            @AuthenticationPrincipal AuthUserDto authUserDto,
            @RequestParam MultipartFile userProfile
    ) {
        try {
            customerService.updateProfileImage(authUserDto.getUserIdAsLong(), userProfile);
            return ResponseEntity.ok(Map.of("message", "프로필 수정 완료"));
        } catch (IOException e) {
            throw new RuntimeException("S3 업로드 중 오류가 발생했습니다.");
        }
    }

    @GetMapping("/address")
    public ResponseEntity<List<CustomerAddressResponse>> getAddress(
            @AuthenticationPrincipal AuthUserDto authUserDto
    ) {

        List<CustomerAddressResponse> list = customerService.getAddress(authUserDto.getUserIdAsLong());

        return ResponseEntity.ok().body(list);

    }

    @PostMapping("/address")
    public ResponseEntity<CustomerResponse> addAddress(
            @AuthenticationPrincipal AuthUserDto authUserDto,
            @RequestBody @Valid CustomerAddressRequest customerAddressRequest) {
        customerService.addAddress(authUserDto.getUserIdAsLong(), customerAddressRequest);

        CustomerResponse response = CustomerResponse.builder()
                .message("주소등록이 완료되었습니다.")
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/address/{addressId}")
    public ResponseEntity<CustomerAddressResponse> updateAddress(
            @AuthenticationPrincipal AuthUserDto authUserDto,
            @PathVariable Long addressId,
            @RequestBody @Valid CustomerAddressRequest customerAddressRequest) {
        CustomerAddressResponse response = customerService.updateAddress(authUserDto.getUserIdAsLong(), addressId,
                customerAddressRequest);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/address/{addressId}/delete")
    public ResponseEntity<CustomerResponse> deleteAddress(
            @AuthenticationPrincipal AuthUserDto authUserDto,
            @PathVariable Long addressId) {
        customerService.deleteAddress(authUserDto.getUserIdAsLong(), addressId);

        CustomerResponse response = CustomerResponse.builder()
                .message("주소삭제가 완료되었습니다.")
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/confirm")
    public ResponseEntity<CustomerSimpleDto> confirm(@AuthenticationPrincipal AuthUserDto authUserDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(customerService.checkUser(authUserDto.getUserIdAsLong()));
    }

}
