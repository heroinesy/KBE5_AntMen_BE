package com.antmen.antwork.domain.reservation.mapper;

import com.antmen.antwork.common.api.response.reservation.ReservationAdminListDto;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@SqlResultSetMapping(
        name = "ReservationAdminListMapping",
        classes = @ConstructorResult(
                targetClass = ReservationAdminListDto.class,
                columns = {
                        @ColumnResult(name = "reservation_id", type = Long.class),
                        @ColumnResult(name = "customer_id", type = Long.class),
                        @ColumnResult(name = "customerName", type = String.class),
                        @ColumnResult(name = "category_name", type = String.class),
                        @ColumnResult(name = "reservation_status", type = String.class),
                        @ColumnResult(name = "reservation_created_at", type = LocalDateTime.class),
                        @ColumnResult(name = "reservation_date", type = LocalDate.class),
                        @ColumnResult(name = "reservation_time", type = LocalTime.class),
                }
        )
)
public class ReservationAdminListMapping {
        @Id
        private Long dummyId;
}
