package com.antmen.antwork.common.infra.repository.account;

import com.antmen.antwork.common.domain.entity.account.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

public interface UserRepositoryCustom {
    Page<User> searchCustomersWithPaging(String name, String sortBy, Pageable pageable
    );
}
