package com.antmen.antwork.domain.reservation.mapper;

import com.antmen.antwork.domain.reservation.dto.ReservationStatDto;
import jakarta.persistence.*;

@Entity
@SqlResultSetMapping(
        name = "ReservationStatMapping",
        classes = @ConstructorResult(
                targetClass = ReservationStatDto.class,
                columns = {
                        @ColumnResult(name = "status", type = String.class),
                        @ColumnResult(name = "count", type = long.class)
                }
        )
)
public class ReservationStatMapping {
    @Id
    private Long id;
}
