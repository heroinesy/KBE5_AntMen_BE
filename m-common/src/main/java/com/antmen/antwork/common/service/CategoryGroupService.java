package com.antmen.antwork.common.service;

import com.antmen.antwork.common.domain.entity.CategoryGroup;
import com.antmen.antwork.common.infra.repository.CategoryGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryGroupService {
    private final CategoryGroupRepository categoryGroupRepository;

    @Transactional
    public void saveCategoryGroups(Long reservationId, List<Long> categoryIdList) {
        for (Long categoryId : categoryIdList) {
            CategoryGroup cg = CategoryGroup.builder()
                    .reservationId(reservationId)
                    .categoryId(categoryId)
                    .build();
            categoryGroupRepository.save(cg);
        }
    }

    @Transactional(readOnly = true)
    public List<CategoryGroup> getCategoryGroupsByReservationId(Long reservationId) {
        return categoryGroupRepository.findByReservationId(reservationId);
    }
} 