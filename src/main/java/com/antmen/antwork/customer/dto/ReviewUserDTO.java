package com.antmen.antwork.customer.dto;

import com.antmen.antwork.entity.ReviewUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ReviewUserDTO {

    private Integer id;         // 리뷰 ID

    private Integer userId;     // 사용자 ID
    private String userEmail;   // 사용자 이메일

    private Integer reservationId; // 예약 ID
    private LocalDateTime regDate; // 등록일
    private LocalDateTime modDate; // 수정일

    private byte rating;        // 평점
    private String comment;     // 리뷰 내용

    // 엔티티를 매개변수로 받는 생성자
    public ReviewUserDTO(ReviewUser reviewUser) {
        this.id = reviewUser.getId();
        this.userId = reviewUser.getUser() != null ? reviewUser.getUser().getId() : null;
        this.userEmail = reviewUser.getUser() != null ? reviewUser.getUser().getEmail() : null;
        this.reservationId = reviewUser.getReservation() != null ? reviewUser.getReservation().getId() : null;
        this.rating = reviewUser.getRating();
        this.comment = reviewUser.getComment();
        this.regDate = reviewUser.getRegDate();
        this.modDate = reviewUser.getModDate();
    }

}
