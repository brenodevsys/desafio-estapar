package com.breno.garage_service.repository;


import com.breno.garage_service.entity.Spot;
import com.breno.garage_service.enumerate.SpotStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpotRepository extends JpaRepository<Spot, Long> {
    Optional<Spot> findByLatAndLng(double lat, double lng);

    long countByStatus(SpotStatus status);

}
