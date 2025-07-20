package admin;

import com.antmen.antwork.admin.service.AdminRefundService;
import com.antmen.antwork.domain.matching.dto.MatchingOverviewDto;
import com.antmen.antwork.domain.matching.dto.MatchingResponseRequestDto;
import com.antmen.antwork.domain.matching.service.MatchingService;
import com.antmen.antwork.domain.matching.dto.MatchingStatDto;
import com.antmen.antwork.domain.reservation.dto.ReservationStatusChangeRequestDto;
import com.antmen.antwork.domain.reservation.service.ReservationService;
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
    private final MatchingService matchingService;
    private final AdminRefundService adminRefundService;

    /**
     * 예약 상태 변경
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> changeReservationStatusByAdmin(
            @PathVariable Long id,
            @RequestBody ReservationStatusChangeRequestDto dto
    ) {
        reservationService.changeStatusByAdmin(id, dto);

        // 취소시 환불
        if (dto.getStatus().equals(ReservationStatus.CANCEL.name())){
            adminRefundService.autoRefund(id, "[자동환불] 관리자 취소: " + dto.getReason());
        }
        return ResponseEntity.ok().build();
    }

    /**
     * 전체 예약 목록 조회
     */
    @GetMapping
    public ResponseEntity<ReservationAdminOverviewDto> getAllReservations(
            @RequestParam(required = false) String reservationStatus,
            @RequestParam(required = false) String searchName,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate
    ) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(reservationService.getAllReservations(reservationStatus, searchName, category, startDate,endDate));
    }

//    // 예약 상태에 따라서 예약 리스트 조회
//    @GetMapping("/{reservationStatus}")
//    public ResponseEntity<List<ReservationResponseDto>> getReservations(@PathVariable String reservationStatus) {
//        ReservationStatus status = ReservationStatus.valueOf(reservationStatus.toUpperCase());
//        return ResponseEntity.status(HttpStatus.OK).body(reservationService.getReservationsByStatus(status));
//    }

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

    // 관리자의 매칭 수정
    // 고객 대신 수락
    @PutMapping("/matching/{id}/accept")
    public ResponseEntity<Void> adminMachingAccept(@PathVariable Long id) {
        MatchingResponseRequestDto dto = MatchingResponseRequestDto.builder()
                .matchingIsFinal(true)
                .matchingRefuseReason(null)
                .build();
        matchingService.customerResponseMatching(id, dto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 관리자가 매니저 대신 수락 (매니저가 응답하지 않은 경우)
    @PutMapping("/matching/{id}/admin-accept")
    public ResponseEntity<Void> adminAcceptMatching(@PathVariable Long id) {
        matchingService.adminAcceptMatching(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 관리자의 매니저 변경
    @PutMapping("/managerChange")
    public ResponseEntity<Void> adminChangeManager(@RequestBody Long reservationId, @RequestParam Long managerId) {
        reservationService.changeManager(reservationId,managerId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 요청보내기
    @PutMapping("/matching-request")
    public ResponseEntity<Void> adminMatchingRequest(@RequestBody Long matchingId) {
        matchingService.adminMatchingRequest(matchingId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 새 매칭 만들기 - 직접 지정
    @PutMapping("/add-matching")
    public ResponseEntity<Void> adminAddMatching(@RequestBody Long reservationId, @RequestParam Long managerId){
        matchingService.adminAddMatching(reservationId, managerId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 새 매칭 만들기 - 자동추천
    @PutMapping("/add-matching/auto")
    public ResponseEntity<Void> adminAddMatchingAuto(@RequestBody Long reservationId){
        matchingService.adminAddMatchingAuto(reservationId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}