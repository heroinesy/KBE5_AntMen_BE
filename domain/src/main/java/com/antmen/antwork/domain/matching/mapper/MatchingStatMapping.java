package com.antmen.antwork.domain.matching.mapper;

import com.antmen.antwork.domain.matching.dto.MatchingStatDto;
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