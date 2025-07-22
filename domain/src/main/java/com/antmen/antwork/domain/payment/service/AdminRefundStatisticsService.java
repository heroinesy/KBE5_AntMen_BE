package com.antmen.antwork.domain.payment.service;
















@Service
@RequiredArgsConstructor
public class AdminRefundStatisticsService {
    private final RefundRepository refundRepository;
    private final ReservationRepository reservationRepository;

    public AdminRefundStatisticsSummaryDto getRefundSummary(){
        long totalRefundCount = refundRepository.count();
        Long approveRefundCount = refundRepository.countByRefundStatus(RefundStatus.APPROVED);
        Long approveRefundAmount = refundRepository.TotalRefundAmountByStatus(RefundStatus.APPROVED);
        long totalReservationCount = reservationRepository.count();

        double rawRate = (double) totalRefundCount / totalReservationCount * 100;
        double refundRate = Math.round(rawRate * 100.0) / 100.0;

        return AdminRefundStatisticsSummaryDto.builder()
                .refundRate(refundRate)
                .approveRefundCount(approveRefundCount)
                .totalRefundCount(totalRefundCount)
                .totalRefundAmount(approveRefundAmount)
                .build();
    }

    @Cacheable(value = "refundReasonStatistics", unless = "#result.isEmpty()")
    public List<AdminRefundReasonStatisticsDto> getRefundReasonStatistics() {
        return refundRepository.CountByRefundReason().stream()
                .map(AdminRefundReasonStatisticsDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 상위 환불 사유를 반환 (기타 비율 50% 이하 유지, 자동환불 그룹화)
     * 성능 최적화: 단일 순회로 처리 + 캐싱
     */
    @Cacheable(value = "topRefundReasons", key = "#maxTopCount", unless = "#result.isEmpty()")
    public List<AdminRefundReasonStatisticsDto> getTopRefundReasons(int maxTopCount) {
        List<AdminRefundReasonStatisticsDto> allReasons = getRefundReasonStatistics();
        
        // 단일 순회로 자동환불 그룹화 및 전체 건수 계산
        long autoRefundCount = 0;
        long totalCount = 0;
        List<AdminRefundReasonStatisticsDto> nonAutoReasons = new ArrayList<>();
        
        for (AdminRefundReasonStatisticsDto reason : allReasons) {
            totalCount += reason.getCount();
            if (reason.getRefundReason().contains("[자동환불]")) {
                autoRefundCount += reason.getCount();
            } else {
                nonAutoReasons.add(reason);
            }
        }
        
        // 결과 리스트 초기화
        List<AdminRefundReasonStatisticsDto> topReasons = new ArrayList<>();
        long selectedCount = 0;
        
        // 자동환불이 있으면 먼저 추가
        if (autoRefundCount > 0) {
            topReasons.add(new AdminRefundReasonStatisticsDto("자동환불", autoRefundCount));
            selectedCount += autoRefundCount;
        }
        
        // 나머지 사유들을 건수 순으로 정렬 (내림차순)
        nonAutoReasons.sort((a, b) -> Long.compare(b.getCount(), a.getCount()));
        
        // 기타 비율을 50% 이하로 유지하면서 주요 사유 선별
        for (AdminRefundReasonStatisticsDto reason : nonAutoReasons) {
            // 이미 최대 개수에 도달했으면 중단
            if (topReasons.size() >= maxTopCount) {
                break;
            }
            
            long currentCount = reason.getCount();
            // 현재 사유를 추가했을 때 기타 비율이 50% 이하인지 확인
            if ((selectedCount + currentCount) * 2 <= totalCount) { // 50% = totalCount/2
                topReasons.add(reason);
                selectedCount += currentCount;
            } else {
                break; // 더 이상 추가하면 기타 비율이 50%를 넘어감
            }
        }
        
        return topReasons;
    }

    /**
     * 자동환불 세부 사유들을 반환
     */
    @Cacheable(value = "autoRefundDetails", unless = "#result.isEmpty()")
    public List<AdminRefundReasonStatisticsDto> getAutoRefundDetails() {
        List<AdminRefundReasonStatisticsDto> allReasons = getRefundReasonStatistics();
        
        return allReasons.stream()
                .filter(reason -> reason.getRefundReason().contains("[자동환불]"))
                .sorted((a, b) -> Long.compare(b.getCount(), a.getCount())) // 건수 순으로 정렬
                .collect(Collectors.toList());
    }

    public List<AdminCustomerRefundStatisticsDto> getTopApprovedRefundCustomers() {
        return refundRepository.getTopApprovedRefundCustomers(RefundStatus.APPROVED).stream()
                .map(p -> new AdminCustomerRefundStatisticsDto(
                        p.getCustomerId(),
                        p.getCustomerName(),
                        p.getRefundCount(),
                        p.getTotalRefundAmount()
                ))
                .collect(Collectors.toList());
    }

    public List<AdminManagerRefundStatisticsDto> getTopApprovedRefundManagers() {
        return refundRepository.getTopApprovedRefundManagers(RefundStatus.APPROVED).stream()
                .map(p -> new AdminManagerRefundStatisticsDto(
                        p.getManagerId(),
                        p.getManagerName(),
                        p.getRefundCount(),
                        p.getTotalRefundAmount()
                ))
                .collect(Collectors.toList());
    }
}