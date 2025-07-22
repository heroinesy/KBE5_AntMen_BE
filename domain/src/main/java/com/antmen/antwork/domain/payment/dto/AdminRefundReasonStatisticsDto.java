package com.antmen.antwork.domain.payment.dto;





@Getter
@AllArgsConstructor
public class AdminRefundReasonStatisticsDto {
    private String refundReason;
    private Long count;

    public static AdminRefundReasonStatisticsDto from(RefundRepository.RefundReasonStatisticsDto p) {
        return new AdminRefundReasonStatisticsDto(p.getReason(), p.getCount());
    }
}