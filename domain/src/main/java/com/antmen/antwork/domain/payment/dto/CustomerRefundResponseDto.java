package com.antmen.antwork.domain.payment.dto;










@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRefundResponseDto {
    private Long payId;
    private String refundReason;
    private Integer refundAmount;
    private RefundStatus refundStatus;
    private LocalDateTime refundCreatedAt;

    public static CustomerRefundResponseDto from(Refund refund) {
        return CustomerRefundResponseDto.builder()
                .payId(refund.getPayId())
                .refundReason(refund.getRefundReason())
                .refundAmount(refund.getRefundAmount())
                .refundStatus(refund.getRefundStatus())
                .refundCreatedAt(refund.getRefundCreatedAt())
                .build();
    }
}