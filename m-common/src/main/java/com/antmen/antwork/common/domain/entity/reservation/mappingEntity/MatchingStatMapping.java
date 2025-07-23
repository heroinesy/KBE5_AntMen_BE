package com.antmen.antwork.common.domain.entity.reservation.mappingEntity;

import com.antmen.antwork.common.api.response.reservation.MatchingStatDto;
import jakarta.persistence.*;

@Entity
@SqlResultSetMapping(
        name = "MatchingStatMapping",
        classes = @ConstructorResult(
                targetClass = MatchingStatDto.class,
                columns = {
                        @ColumnResult(name = "status", type = String.class),
                        @ColumnResult(name = "count", type = Long.class)
                }
        )
)
public class MatchingStatMapping {
    @Id
    private Long dummyId;
}
