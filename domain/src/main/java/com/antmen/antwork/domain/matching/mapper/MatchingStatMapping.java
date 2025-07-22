package com.antmen.antwork.domain.matching.mapper;




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
