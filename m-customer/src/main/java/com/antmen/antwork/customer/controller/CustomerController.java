package com.antmen.antwork.customer.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.antmen.antwork.common.api.request.account.CustomerAddressRequest;
import com.antmen.antwork.common.api.request.account.CustomerSignupRequest;
import com.antmen.antwork.common.api.request.account.CustomerUpdateRequest;
import com.antmen.antwork.common.api.response.account.CustomerAddressResponse;
import com.antmen.antwork.common.api.response.account.CustomerProfileResponse;
import com.antmen.antwork.common.api.response.account.CustomerResponse;
import com.antmen.antwork.common.service.serviceAccount.CustomerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    // ⭐️ 추가된 부분: 현재 로그인한 사용자의 ID를 가져오는 메서드
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return Long.parseLong(userDetails.getUsername());
    }

    // google, facebook 가입을 하게 되면 어떤 값을 받게 될지 확인 후 추가 필요
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

    // login한 user_id로 수정해야함
    @GetMapping("/me")
    public ResponseEntity<CustomerProfileResponse> getProfile() {
        CustomerProfileResponse response = customerService.getProfile(2L);

        return ResponseEntity.ok(response);

    }

    // login한 user_id로 수정해야함
    @PutMapping("/me")
    public ResponseEntity<CustomerProfileResponse> updateProfile(
            @RequestBody @Valid CustomerUpdateRequest customerUpdateRequest) {
        CustomerProfileResponse response = customerService.updateProfile(2L, customerUpdateRequest);

        return ResponseEntity.ok(response);

    }

    // login한 user_id로 수정해야함
    @GetMapping("/address")
    public ResponseEntity<List<CustomerAddressResponse>> getAddress() {

        List<CustomerAddressResponse> list = customerService.getAddress(getCurrentUserId());

        return ResponseEntity.ok().body(list);

    }

    // login한 user_id로 수정해야함
    @PostMapping("/address")
    public ResponseEntity<CustomerResponse> addAddress(
            @RequestBody @Valid CustomerAddressRequest customerAddressRequest) {
        customerService.addAddress(getCurrentUserId(), customerAddressRequest);

        System.out.println("주소등록이 진행중.");

        CustomerResponse response = CustomerResponse.builder()
                .message("주소등록이 완료되었습니다.")
                .build();

        return ResponseEntity.ok(response);
    }

    // login한 user_id로 수정해야함
    @PutMapping("/address/{addressId}")
    public ResponseEntity<CustomerAddressResponse> updateAddress(
            @PathVariable Long addressId,
            @RequestBody @Valid CustomerAddressRequest customerAddressRequest) {
        CustomerAddressResponse response = customerService.updateAddress(getCurrentUserId(), addressId,
                customerAddressRequest);

        return ResponseEntity.ok(response);
    }

    // login한 user_id로 수정해야함
    @DeleteMapping("/address/{addressId}/delete")
    public ResponseEntity<CustomerResponse> deleteAddress(
            @PathVariable Long addressId) {
        customerService.deleteAddress(getCurrentUserId(), addressId);

        CustomerResponse response = CustomerResponse.builder()
                .message("주소삭제가 완료되었습니다.")
                .build();

        return ResponseEntity.ok(response);
    }

}
