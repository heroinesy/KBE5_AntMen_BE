package com.antmen.antwork.customer.api.controller;

import com.antmen.antwork.customer.api.request.CustomerSignupRequest;
import com.antmen.antwork.customer.api.response.CommonResponse;
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
    public ResponseEntity<CommonResponse<String>> signUp(
            @RequestBody
            @Valid
            CustomerSignupRequest customerSignupRequest
    ){

        customerService.signUp(customerSignupRequest);

        CommonResponse<String> response = CommonResponse.<String>builder()
                .message("회원가입이 완료되었습니다.")
                .build();

        return ResponseEntity.ok(response);
    }

    // login한 user_id로 수정해야함
    @GetMapping("/me")
    public ResponseEntity<CommonResponse<CustomerProfileResponse>> getProfile(){
        CustomerProfileResponse response = customerService.getProfile(1L);

        CommonResponse<CustomerProfileResponse> profileResponseCommonResponse = CommonResponse.<CustomerProfileResponse>builder()
                .body(response)
                .message("my profile")
                .build();

        return ResponseEntity.ok(profileResponseCommonResponse);

    }

}
