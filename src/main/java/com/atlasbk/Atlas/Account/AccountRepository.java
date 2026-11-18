package com.atlasbk.Atlas.Account;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Override
    boolean existsById(Long aLong);

    boolean existsByAccNum(Integer accNum);
}
