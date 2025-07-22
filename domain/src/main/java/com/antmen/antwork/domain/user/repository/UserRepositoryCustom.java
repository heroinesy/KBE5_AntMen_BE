package com.antmen.antwork.domain.user.repository;

import com.antmen.antwork.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {
    Page<User> searchCustomersWithPaging(String name, String sortBy, Pageable pageable
    );
}