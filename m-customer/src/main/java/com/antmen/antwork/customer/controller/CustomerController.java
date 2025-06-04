package com.antmen.antwork.customer.controller;

import com.antmen.antwork.common.api.request.CustomerAddressRequest;
import com.antmen.antwork.common.api.request.CustomerSignupRequest;
import com.antmen.antwork.common.api.request.CustomerUpdateRequest;
import com.antmen.antwork.common.api.response.CustomerAddressResponse;
import com.antmen.antwork.common.api.response.CustomerProfileResponse;
import com.antmen.antwork.common.api.response.CustomerResponse;
import com.antmen.antwork.common.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    // google, facebook 가입을 하게 되면 어떤 값을 받게 될지 확인 후 추가 필요
    @PostMapping("/signup")
    public ResponseEntity<CustomerResponse> signUp(
            @RequestBody
            @Valid
            CustomerSignupRequest customerSignupRequest
    ) {

        customerService.signUp(customerSignupRequest);

        CustomerResponse response = CustomerResponse.builder()
                .message("회원가입이 완료되었습니다.")
                .build();

        return ResponseEntity.ok(response);
    }

    // login한 user_id로 수정해야함
    @GetMapping("/me")
    public ResponseEntity<CustomerProfileResponse> getProfile(
    ) {
        CustomerProfileResponse response = customerService.getProfile(2L);

        return ResponseEntity.ok(response);

    }

    // login한 user_id로 수정해야함
    @PutMapping("/me")
    public ResponseEntity<CustomerProfileResponse> updateProfile(
            @RequestBody
            @Valid
            CustomerUpdateRequest customerUpdateRequest
    ) {
        CustomerProfileResponse response = customerService.updateProfile(2L, customerUpdateRequest);

        return ResponseEntity.ok(response);

    }

    // login한 user_id로 수정해야함
    @GetMapping("/address")
    public ResponseEntity<List<CustomerAddressResponse>> getAddress() {

        List<CustomerAddressResponse> list = customerService.getAddress(2L);

        return ResponseEntity.ok().body(list);

    }

    // login한 user_id로 수정해야함
    @PostMapping("/address")
    public ResponseEntity<CustomerResponse> addAddress(
            @RequestBody
            @Valid
            CustomerAddressRequest customerAddressRequest
    ) {
        customerService.addAddress(2L, customerAddressRequest);

        CustomerResponse response = CustomerResponse.builder()
                .message("주소등록이 완료되었습니다.")
                .build();

        return ResponseEntity.ok(response);
    }

    // login한 user_id로 수정해야함
    @PutMapping("/address/{addressId}")
    public ResponseEntity<CustomerAddressResponse> updateAddress(
            @PathVariable
            Long addressId,
            @RequestBody
            @Valid
            CustomerAddressRequest customerAddressRequest
    ){
        CustomerAddressResponse response = customerService.updateAddress(2L, addressId, customerAddressRequest);

        return ResponseEntity.ok(response);
    }

    // login한 user_id로 수정해야함
    @DeleteMapping("/address/{addressId}/delete")
    public ResponseEntity<CustomerResponse> deleteAddress(
            @PathVariable
            Long addressId
    ){
        customerService.deleteAddress(2L, addressId);

        CustomerResponse response = CustomerResponse.builder()
                .message("주소삭제가 완료되었습니다.")
                .build();

        return ResponseEntity.ok(response);
    }

}
