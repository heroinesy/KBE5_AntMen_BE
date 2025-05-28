package com.antmen.antwork.common.service.mapper;

import com.antmen.antwork.common.domain.entity.CategoryOption;
import com.antmen.antwork.common.domain.entity.Reservation;
import com.antmen.antwork.common.domain.entity.ReservationOption;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReservationOptionMapper {

    public ReservationOption toEntity(Reservation reservation, CategoryOption categoryOption) {
        return ReservationOption.builder()
                .reservation(reservation)
                .categoryOption(categoryOption)
                .build();
    }

    public Long toDto(ReservationOption entity) {
        return entity.getCategoryOption().getCoId();
    }

    public List<Long> toDtoList(List<ReservationOption> entities) {
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}



