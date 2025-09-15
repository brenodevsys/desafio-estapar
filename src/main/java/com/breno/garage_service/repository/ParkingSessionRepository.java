package com.breno.garage_service.repository;


import com.breno.garage_service.entity.ParkingSession;
import com.breno.garage_service.enumerate.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ParkingSessionRepository extends JpaRepository<ParkingSession, Long> {

    Optional<ParkingSession> findFirstByPlateAndStatusOrderByEntryTimeDesc(String plate, SessionStatus status);

    Optional<ParkingSession> findFirstByPlateOrderByEntryTimeDesc(String plate);

    List<ParkingSession> findBySectorCodeAndExitTimeBetween(
            String sectorCode,
            LocalDateTime start,
            LocalDateTime end
    );

}
