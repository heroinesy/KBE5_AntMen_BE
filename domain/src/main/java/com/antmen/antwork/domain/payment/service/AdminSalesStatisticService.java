package com.antmen.antwork.domain.payment.service;


















@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminSalesStatisticService {
    private final PaymentRepository paymentRepository;

    public AdminSalesSummaryResponseDto getFinalSalesProfit() {
        Long total = Optional.ofNullable(paymentRepository.findSum()).orElse(0L);
        Long month = Optional.ofNullable(paymentRepository.findMonth()).orElse(0L);
        Long days = Optional.ofNullable(paymentRepository.findPaymentDays()).orElse(0L);
        Long dailyAverage = total / days;

        Long totalProfit = Math.round(total * 0.9);
        Long monthProfit = Math.round(month * 0.9);
        Long daysProfit = totalProfit / days;

        LocalDate today = LocalDate.now();
        LocalDate start = today.minusDays(6);

        List<Payment> payments = paymentRepository.findAllByPayCreatedTimeBetweenAndPayStatus(
                start.atStartOfDay(),
                today.atTime(LocalTime.MAX),
                PaymentStatus.DONE);

        Map<LocalDate, Long> SalesMap = payments.stream()
                .collect(Collectors.groupingBy
                        (p-> p.getPayCreatedTime().toLocalDate(),
                                Collectors.summingLong(Payment::getPayAmount)));

        List<AdminDailySaleResponseDto> dailySalesProfitList = IntStream.rangeClosed(0, 6)
                .mapToObj(i -> {
                    LocalDate dailyDate = start.plusDays(i);
                    Long dailySales = SalesMap.getOrDefault(dailyDate, 0L);
                    Long dailyProfit = Math.round(dailySales * 0.9);
                    return AdminDailySaleResponseDto.builder()
                            .dailyDate(dailyDate)
                            .dailySales(dailySales)
                            .dailyFee(dailySales - dailyProfit)
                            .dailyProfit(dailyProfit)
                            .build();
                }).collect(Collectors.toList());

        return AdminSalesSummaryResponseDto.builder()
                .totalSales(total)
                .currentMonthSales(month)
                .averageDailySales(dailyAverage)
                .totalProfit(totalProfit)
                .currentMonthProfit(monthProfit)
                .averageDailyProfit(daysProfit)
                .recentWeeklySalesProfit(dailySalesProfitList).build();
    }
}