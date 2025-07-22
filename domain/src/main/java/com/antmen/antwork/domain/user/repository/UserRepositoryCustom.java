package com.antmen.antwork.domain.user.repository;





public interface UserRepositoryCustom {
    Page<User> searchCustomersWithPaging(String name, String sortBy, Pageable pageable
    );
}
