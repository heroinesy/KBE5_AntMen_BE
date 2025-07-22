package com.antmen.antwork.domain.payment.service;

import com.antmen.antwork.domain.board.repository.BoardRepository;
import com.antmen.antwork.domain.payment.dto.DailyInquiryRefundResponseDto;
import com.antmen.antwork.domain.payment.repository.RefundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminInquiryRefundStatisticsService {
    private final BoardRepository boardRepository;
    private final RefundRepository refundRepository;

    public List<DailyInquiryRefundResponseDto> getDailyInquiryRefundStatistics(int recentDays) {
        LocalDateTime startDate = LocalDate.now().minusDays(recentDays - 1).atStartOfDay();
        
        // 일별 상담 통계 조회
        List<BoardRepository.DailyBoardProjection> boardStats = boardRepository.getDailyBoardStatistics(startDate);
        
        // 일별 환불 통계 조회
        List<RefundRepository.DailyRefundProjection> refundStats = refundRepository.getDailyRefundStatistics(startDate);
        
        // 상담 데이터를 Map으로 변환
        Map<LocalDate, Long> customerInquiriesMap = boardStats.stream()
                .collect(Collectors.toMap(
                        BoardRepository.DailyBoardProjection::getCreatedDate,
                        BoardRepository.DailyBoardProjection::getDailyCustomerInquiries
                ));
        
        Map<LocalDate, Long> managerInquiriesMap = boardStats.stream()
                .collect(Collectors.toMap(
                        BoardRepository.DailyBoardProjection::getCreatedDate,
                        BoardRepository.DailyBoardProjection::getDailyManagerInquiries
                ));
        
        // 환불 데이터를 Map으로 변환
        Map<LocalDate, Long> refundMap = refundStats.stream()
                .collect(Collectors.toMap(
                        RefundRepository.DailyRefundProjection::getCreatedDate,
                        RefundRepository.DailyRefundProjection::getDailyRefundCount
                ));
        
        // 최근 N일간의 모든 날짜에 대해 데이터 생성
        List<DailyInquiryRefundResponseDto> result = new ArrayList<>();
        for (int i = 0; i < recentDays; i++) {
            LocalDate date = LocalDate.now().minusDays(recentDays - 1 - i);
            
            Long customerInquiries = customerInquiriesMap.getOrDefault(date, 0L);
            Long managerInquiries = managerInquiriesMap.getOrDefault(date, 0L);
            Long refunds = refundMap.getOrDefault(date, 0L);
            
            DailyInquiryRefundResponseDto dto = DailyInquiryRefundResponseDto.builder()
                    .date(date)
                    .dailyCustomerInquiries(customerInquiries)
                    .dailyManagerInquiries(managerInquiries)
                    .dailyRefunds(refunds)
                    .build();
            
            result.add(dto);
        }
        return result;
    }
} 