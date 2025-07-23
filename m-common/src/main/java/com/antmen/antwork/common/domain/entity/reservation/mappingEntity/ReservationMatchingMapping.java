package com.antmen.antwork.common.domain.entity.reservation.mappingEntity;

import com.antmen.antwork.common.api.response.reservation.ReservationMatchingListDto;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@SqlResultSetMapping(
        name = "ReservationMatchingMapping",
        classes = @ConstructorResult(
                targetClass = ReservationMatchingListDto.class,
                columns = {
                        @ColumnResult(name = "reservationId", type = Long.class),
                        @ColumnResult(name = "customerId", type = Long.class),
                        @ColumnResult(name = "customerName", type = String.class),
                        @ColumnResult(name = "categoryName", type = String.class),
                        @ColumnResult(name = "reservationCreatedAt", type = LocalDateTime.class),
                        @ColumnResult(name = "reservationDate", type = LocalDate.class),
                        @ColumnResult(name = "reservationTime", type = LocalTime.class),
                        @ColumnResult(name = "totalRequests", type = Long.class),
                        @ColumnResult(name = "totalManagerResponses", type = Long.class),
                        @ColumnResult(name = "totalManagerAccepts", type = Long.class),
                        @ColumnResult(name = "matchingStatus", type = String.class),
                    }
        )
)
public class ReservationMatchingMapping {
    @Id
    private Long dummyId;
}
