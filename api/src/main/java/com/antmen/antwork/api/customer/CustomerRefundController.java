package com.antmen.antwork.api.customer;

import com.antmen.antwork.domain.payment.dto.CustomerRefundRequestDto;
import com.antmen.antwork.domain.payment.service.CustomerRefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/customer/refunds")
public class CustomerRefundController {
    private final CustomerRefundService customerRefundService;

    @PostMapping
    public ResponseEntity<Void> requestRefund(@RequestBody CustomerRefundRequestDto requestDto) {
        customerRefundService.requestRefund(requestDto);
        return ResponseEntity.ok().build();
    }
}