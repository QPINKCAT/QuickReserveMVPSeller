package com.pinkcat.quick_reserve_seller.common.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface ActiveRepository<T, ID> extends JpaRepository<T, ID> {
    Optional<T> findByPkAndActive(ID pk, Boolean active);
    Page<T> findAllByActive(Pageable pageable, Boolean active);
    List<T> findAllByActive(Boolean active);
}