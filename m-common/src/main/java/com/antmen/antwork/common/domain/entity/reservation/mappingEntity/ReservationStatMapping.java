package com.antmen.antwork.common.domain.entity.reservation.mappingEntity;

import com.antmen.antwork.common.api.response.reservation.ReservationStatDto;
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
