package com.breno.garage_service.repository;

import com.breno.garage_service.entity.Sector;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SectorRepository extends JpaRepository<Sector, Long> {
    Optional<Sector> findByCode(String code);

}