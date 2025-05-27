package com.antmen.antwork.customer.api.controller;

import com.antmen.antwork.customer.api.request.CustomerSignupRequest;
import com.antmen.antwork.customer.api.request.CustomerUpdateRequest;
import com.antmen.antwork.customer.api.response.CustomerResponse;
import com.antmen.antwork.customer.api.response.CustomerProfileResponse;
import com.antmen.antwork.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/signup")
    public ResponseEntity<CustomerResponse> signUp(
            @RequestBody
            @Valid
            CustomerSignupRequest customerSignupRequest
    ) {

        customerService.signUp(customerSignupRequest);

        CustomerResponse response = CustomerResponse.<String>builder()
                .message("회원가입이 완료되었습니다.")
                .build();

        return ResponseEntity.ok(response);
    }

    // login한 user_id로 수정해야함
    @GetMapping("/me")
    public ResponseEntity<CustomerProfileResponse> getProfile() {
        CustomerProfileResponse response = customerService.getProfile(1L);

        return ResponseEntity.ok(response);

    }

    // login한 user_id로 수정해야함
    @PutMapping("/me")
    public ResponseEntity<CustomerProfileResponse> updateProfile(
            @RequestBody
            @Valid
            CustomerUpdateRequest customerUpdateRequest
    ) {
        CustomerProfileResponse response = customerService.updateProfile(1L, customerUpdateRequest);

        return ResponseEntity.ok(response);

    }

}
