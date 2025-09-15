package com.breno.garage_service.service;

import com.breno.garage_service.dto.ParkingSessionDTO;
import com.breno.garage_service.entity.ParkingSession;
import com.breno.garage_service.entity.Sector;
import com.breno.garage_service.entity.Spot;
import com.breno.garage_service.enumerate.SessionStatus;
import com.breno.garage_service.enumerate.SpotStatus;
import com.breno.garage_service.exception.BusinessException;
import com.breno.garage_service.mapper.ParkingSessionMapper;
import com.breno.garage_service.repository.ParkingSessionRepository;
import com.breno.garage_service.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParkingService {

    private final SpotRepository spotRepository;
    private final ParkingSessionRepository parkingSessionRepository;
    private final PricingService pricingService;
    private final ParkingSessionMapper parkingSessionMapper;

    @Transactional
    public void registerEntry(String plate, LocalDateTime entryTime) {
        Objects.requireNonNull(plate, "Plate cannot be null");
        Objects.requireNonNull(entryTime, "Entry time cannot be null");

        long totalSpots = spotRepository.count();
        long occupiedSpots = spotRepository.countByStatus(SpotStatus.OCCUPIED);

        log.info("Nova entrada solicitada para placa={} às {}. Ocupadas {}/{} vagas",
                plate, entryTime, occupiedSpots, totalSpots);

        if (occupiedSpots >= totalSpots) {
            log.warn("Entrada negada para placa={} - estacionamento lotado", plate);
            throw new BusinessException("Estacionamento cheio. Entrada negada.");
        }

        parkingSessionRepository.findFirstByPlateAndStatusOrderByEntryTimeDesc(plate, SessionStatus.OPEN)
                .ifPresent(session -> {
                    log.warn("Placa {} já possui sessão aberta (ID={})", plate, session.getId());
                    throw new BusinessException("Placa já tem sessão aberta.");
                });

        ParkingSession session = ParkingSession.builder()
                .plate(plate)
                .entryTime(entryTime)
                .priceFactor(pricingService.factorByOccupancy())
                .status(SessionStatus.OPEN)
                .build();

        parkingSessionRepository.save(session);
        log.info("Sessão criada com sucesso para placa={}, sessãoId={}", plate, session.getId());
    }

    @Transactional
    public void registerParking(String plate, double lat, double lng, LocalDateTime parkedTime) {
        Objects.requireNonNull(plate, "Plate cannot be null");
        Objects.requireNonNull(parkedTime, "Parked time cannot be null");

        log.info("Tentando estacionar placa={} na coordenada lat={}, lng={} às {}", plate, lat, lng, parkedTime);

        ParkingSession session = parkingSessionRepository.findFirstByPlateAndStatusOrderByEntryTimeDesc(plate, SessionStatus.OPEN)
                .orElseThrow(() -> {
                    log.error("Sessão aberta não encontrada para placa={}", plate);
                    return new BusinessException("Sessão não encontrada.");
                });

        Spot spot = spotRepository.findByLatAndLng(lat, lng)
                .orElseThrow(() -> {
                    log.error("Vaga não encontrada para lat={}, lng={}", lat, lng);
                    return new BusinessException("Vaga não encontrada.");
                });

        if (Objects.equals(spot.getStatus(), SpotStatus.OCCUPIED) &&
                !Objects.equals(plate, spot.getCurrentPlate())) {
            log.warn("Tentativa de estacionar na vaga já ocupada. Placa={}, SpotId={}, PlacaAtual={}",
                    plate, spot.getId(), spot.getCurrentPlate());
            throw new BusinessException("Vaga já ocupada.");
        }

        spot.setStatus(SpotStatus.OCCUPIED);
        spot.setCurrentPlate(plate);
        spotRepository.save(spot);

        Sector sector = Objects.requireNonNull(spot.getSector(), "Sector cannot be null");
        BigDecimal effectivePrice = sector.getBasePrice().multiply(session.getPriceFactor());

        session.setSpot(spot);
        session.setSector(sector);
        session.setEffectivePriceHour(effectivePrice);
        session.setParkedTime(parkedTime);

        parkingSessionRepository.save(session);
        log.info("Placa={} estacionada com sucesso na vaga={}, setor={}, preçoEfetivo={}",
                plate, spot.getId(), sector.getCode(), effectivePrice);
    }

    @Transactional
    public BigDecimal registerExit(String plate, LocalDateTime exitTime) {
        Objects.requireNonNull(plate, "Plate cannot be null");
        Objects.requireNonNull(exitTime, "Exit time cannot be null");

        log.info("Saída solicitada para placa={} às {}", plate, exitTime);

        ParkingSession session = parkingSessionRepository
                .findFirstByPlateAndStatusOrderByEntryTimeDesc(plate, SessionStatus.OPEN)
                .orElseThrow(() -> {
                    log.error("Nenhuma sessão aberta encontrada para saída da placa={}", plate);
                    return new BusinessException("Sessão aberta não encontrada.");
                });

        freeSpotIfOccupied(session);

        long totalMinutes = Duration.between(session.getEntryTime(), exitTime).toMinutes();
        BigDecimal totalAmount = pricingService.calculateTotalCharge(session, totalMinutes);

        session.setExitTime(exitTime);
        session.setChargedAmount(totalAmount);
        session.setStatus(SessionStatus.CLOSED);

        parkingSessionRepository.save(session);
        log.info("Saída concluída para placa={}, minutos={}, valorCobrado={}", plate, totalMinutes, totalAmount);

        return totalAmount;
    }

    @Transactional(readOnly = true)
    public ParkingSessionDTO findLatestByPlate(String plate) {
        Objects.requireNonNull(plate, "Plate cannot be null");
        log.debug("Buscando última sessão da placa={}", plate);

        return parkingSessionRepository.findFirstByPlateOrderByEntryTimeDesc(plate)
                .map(parkingSessionMapper::toDto)
                .orElseThrow(() -> {
                    log.warn("Nenhuma sessão encontrada para a placa={}", plate);
                    return new BusinessException("Nenhuma sessão encontrada para a placa: " + plate);
                });
    }

    @Transactional(readOnly = true)
    public ParkingSessionDTO findBySpot(Long spotId) {
        Objects.requireNonNull(spotId, "Spot ID cannot be null");
        log.debug("Buscando sessão atual para vaga={}", spotId);

        return spotRepository.findById(spotId)
                .flatMap(spot -> parkingSessionRepository.findFirstByPlateOrderByEntryTimeDesc(spot.getCurrentPlate()))
                .map(parkingSessionMapper::toDto)
                .orElseThrow(() -> {
                    log.warn("Nenhuma sessão encontrada para a vaga={}", spotId);
                    return new BusinessException("Nenhuma sessão encontrada para a vaga: " + spotId);
                });
    }

    @Transactional(readOnly = true)
    public BigDecimal calculateRevenue(String sectorCode, LocalDateTime start, LocalDateTime end) {
        Objects.requireNonNull(sectorCode, "Sector code cannot be null");
        Objects.requireNonNull(start, "Start date cannot be null");
        Objects.requireNonNull(end, "End date cannot be null");

        log.info("Calculando receita do setor={} no período {} até {}", sectorCode, start, end);

        return parkingSessionRepository.findBySectorCodeAndExitTimeBetween(sectorCode, start, end)
                .stream()
                .map(ParkingSession::getChargedAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void freeSpotIfOccupied(ParkingSession session) {
        if (Objects.nonNull(session.getSpot())) {
            Spot spot = session.getSpot();
            spot.setStatus(SpotStatus.AVAILABLE);
            spot.setCurrentPlate(null);
            spotRepository.save(spot);
            log.debug("Liberada vaga={} da placa={}", spot.getId(), session.getPlate());
        }
    }
}
