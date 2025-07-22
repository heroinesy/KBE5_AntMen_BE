package com.antmen.antwork.domain.calculation.service;























@Service
@RequiredArgsConstructor
public class AdminCalculationService {
    private final CalculationRepository calculationRepository;
    private final ReservationRepository reservationRepository;

    public AdminSummaryCalculationResponseDto getCalculationSummary() {
        Long total = Optional.ofNullable(calculationRepository.findSum()).orElse(0L);

        Pair<LocalDateTime, LocalDateTime> monthRange = getCurrentMonthCalculationRange();
        LocalDate monthStart = monthRange.getFirst().toLocalDate();
        LocalDate monthEnd = monthRange.getSecond().toLocalDate();
        List<Calculation> monthCalcs = calculationRepository.findAll().stream()
                .filter(cal -> {
                    LocalDate calcStart = cal.getStartDate();
                    return !calcStart.isBefore(monthStart) && !calcStart.isAfter(monthEnd);
                })
                .toList();

        Long month = monthCalcs.stream()
                .mapToLong(Calculation::getAmount)
                .sum();

        LocalDate today = LocalDate.now();
        LocalDate lastWeekStart = today.minusWeeks(1).with(DayOfWeek.MONDAY);
        LocalDate lastWeekEnd = today.minusWeeks(1).with(DayOfWeek.SUNDAY);

        Long week = calculationRepository.findAll().stream()
                .filter(cal -> {
                    LocalDate calcStart = cal.getStartDate();
                    return !calcStart.isBefore(lastWeekStart) && !calcStart.isAfter(lastWeekEnd);
                })
                .mapToLong(Calculation::getAmount)
                .sum();

        int count = (int) calculationRepository.count();

        LocalDateTime nowTime = LocalDateTime.now();
        LocalDateTime oneMonthAgo = nowTime.minusMonths(1);
        List<Calculation> recentList = calculationRepository.findByRequestedAtBetween(oneMonthAgo, nowTime);
        List<AdminMonthCalculationResponseDto> recentMonthCalculations = recentList.stream()
                .map(cal -> {
                    User manager = cal.getManager();
                    return AdminMonthCalculationResponseDto.builder()
                            .calculationId(cal.getCalculationId())
                            .managerId(manager.getUserId())
                            .managerName(manager.getUserName())
                            .startDate(cal.getStartDate())
                            .endDate(cal.getEndDate())
                            .amount(cal.getAmount())
                            .requestedAt(cal.getRequestedAt())
                            .build();
                }).toList();

        return AdminSummaryCalculationResponseDto.builder()
                .totalAmount(total)
                .currentMonthAmount(month)
                .currentWeekAmount(week)
                .calculationCount(count)
                .recentMonthCalculations(recentMonthCalculations).build();
    }

    public AdminCalculationResponseDto getCalculationDetail(Long calculationId) {
        Calculation calculation = calculationRepository.findById(calculationId).orElseThrow(() -> new NotFoundException("정산 정보를 찾을 수 없습니다."));

        List<Long> reservationIds = Arrays.stream(calculation.getReservationIds().split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Long::parseLong).toList();

        List<Reservation> reservations = reservationRepository.findAllById(reservationIds);
        List<AdminCalculationReservationDto> reservationDtos = reservations.stream()
                .map(r -> AdminCalculationReservationDto.builder()
                        .reservationId(r.getReservationId())
                        .reservationDate(r.getReservationDate())
                        .categoryName(r.getCategory().getCategoryName())
                        .optionNames(r.getCategory().getOptions().stream().map(CategoryOption::getCoName).toList())
                        .reservationAmount(r.getReservationAmount()).build()
                ).toList();

        return AdminCalculationResponseDto.builder()
                .calculationId(calculationId)
                .startDate(calculation.getStartDate())
                .endDate(calculation.getEndDate())
                .requestedAt(calculation.getRequestedAt())
                .amount(calculation.getAmount())
                .managerId(calculation.getManager().getUserId())
                .managerName(calculation.getManager().getUserName())
                .managerLoginId(calculation.getManager().getUserLoginId())
                .totalReservationCount(reservationDtos.size())
                .totalReservationAmount(reservationDtos.stream().mapToInt(AdminCalculationReservationDto::getReservationAmount).sum())
                .reservations(reservationDtos)
                .build();
    }

    // 주차 요일 추출 메소드
    private Pair<LocalDateTime, LocalDateTime> getMonthCalculationRange(int year, int month) {
        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        LocalDate monthStart = firstDayOfMonth.with(DayOfWeek.MONDAY);
        LocalDate lastDayOfMonth = firstDayOfMonth.withDayOfMonth(firstDayOfMonth.lengthOfMonth());
        LocalDate monthEnd = lastDayOfMonth.with(DayOfWeek.SUNDAY);

        return Pair.of(
                monthStart.atStartOfDay(),
                monthEnd.atTime(23, 59, 59));
    }

    private Pair<LocalDateTime, LocalDateTime> getCurrentMonthCalculationRange() {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        return getMonthCalculationRange(year, month);
    }
}