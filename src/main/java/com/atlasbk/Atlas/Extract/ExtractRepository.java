package com.atlasbk.Atlas.Extract;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExtractRepository extends JpaRepository<Extract, Long> {
    boolean existsById(Long id);
}
