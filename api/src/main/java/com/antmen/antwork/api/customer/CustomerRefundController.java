package com.antmen.antwork.api.customer;










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