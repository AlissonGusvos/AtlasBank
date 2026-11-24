package com.atlasbk.Atlas.Account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUserId_Id(Long userId);
    Optional<Account> findAccountByAccNum(Integer accNum);

    @Override
    boolean existsById(Long aLong);
    boolean existsByAccNum(Integer accNum);
}
