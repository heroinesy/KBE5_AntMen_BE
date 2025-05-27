package com.antmen.antwork.common.infra.repository;

import com.antmen.antwork.common.domain.entity.CategoryGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryGroupRepository extends JpaRepository<CategoryGroup, Long> {
    List<CategoryGroup> findByReservationId(Long reservationId);
} 