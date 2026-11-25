package com.atlasbk.Atlas.Extract;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExtractRepository extends JpaRepository<Extract, Long> {
    boolean existsById(Long id);

    List<Extract> findDistinctByOrgnAccount_Id(Long id);
}
