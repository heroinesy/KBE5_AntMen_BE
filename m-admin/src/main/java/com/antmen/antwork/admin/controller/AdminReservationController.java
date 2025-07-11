package com.antmen.antwork.admin.controller;

import com.antmen.antwork.common.api.request.reservation.ReservationStatusChangeRequestDto;
import com.antmen.antwork.common.api.response.reservation.*;
import com.antmen.antwork.common.domain.entity.reservation.ReservationStatus;
import com.antmen.antwork.common.service.serviceReservation.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/reservations")
public class AdminReservationController {
    private final ReservationService reservationService;

    /**
     * 예약 상태 변경
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> changeReservationStatusByAdmin(
            @PathVariable Long id,
            @RequestBody ReservationStatusChangeRequestDto dto
    ) {
        reservationService.changeStatusByAdmin(id, dto);
        return ResponseEntity.ok().build();
    }

    /**
     * 전체 예약 목록 조회
     */
    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> getAllReservations() {
        List<ReservationResponseDto> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }

    // 예약 상태에 따라서 예약 리스트 조회
    @GetMapping("/{reservationStatus}")
    public ResponseEntity<List<ReservationResponseDto>> getReservations(@PathVariable String reservationStatus) {
        ReservationStatus status = ReservationStatus.valueOf(reservationStatus.toUpperCase());
        return ResponseEntity.status(HttpStatus.OK).body(reservationService.getReservationsByStatus(status));
    }

    // 예약 한 건 단독 조회
    @GetMapping("/{id}/detail")
    public ResponseEntity<ReservationMatchingDetailDto> getReservationMatchingDetail(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(reservationService.getReservationMatchingDetail(id));
    }

    // 매칭 전 예약 매칭 디테일 확인
    @GetMapping("/about-matching")
    public ResponseEntity<MatchingOverviewDto> getReservationMatchingList(
            @RequestParam(required = false) String matchingStatus,
            @RequestParam(required = false) String searchName,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) LocalDate reservatedStartDate,
            @RequestParam(required = false) LocalDate reservatedEndDate
            ) {
        List<MatchingStatDto> matchingStatDtos = reservationService.getMatchingStat(searchName, category, reservatedStartDate, reservatedEndDate);
        List<ReservationMatchingListDto> reservationMatchingListDtoList = reservationService.getReservationMatching(
                matchingStatus, searchName, category, reservatedStartDate, reservatedEndDate);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new MatchingOverviewDto(matchingStatDtos, reservationMatchingListDtoList));
    }


}