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
import com.breno.garage_service.util.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParkingServiceTest {

    @Mock private SpotRepository spotRepository;
    @Mock private ParkingSessionRepository parkingSessionRepository;
    @Mock private PricingService pricingService;
    @Mock private ParkingSessionMapper parkingSessionMapper;

    @InjectMocks private ParkingService parkingService;

    private Sector sector;
    private Spot spot;
    private ParkingSession session;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        sector = TestUtils.getMockSector();
        spot = TestUtils.getMockSpot(sector);
        session = TestUtils.getMockSession(sector, spot);
    }

    @Test
    void shouldRegisterEntrySuccessfully() {
        Mockito.when(spotRepository.count()).thenReturn(10L);
        Mockito.when(spotRepository.countByStatus(SpotStatus.OCCUPIED)).thenReturn(5L);
        Mockito.when(pricingService.factorByOccupancy()).thenReturn(BigDecimal.ONE);

        parkingService.registerEntry("ABC1234", LocalDateTime.now());

        Mockito.verify(parkingSessionRepository, Mockito.times(1)).save(Mockito.any(ParkingSession.class));
    }

    @Test
    void shouldThrowWhenParkingWithoutSession() {
        Mockito.when(parkingSessionRepository.findFirstByPlateAndStatusOrderByEntryTimeDesc("ABC1234", SessionStatus.OPEN))
                .thenReturn(Optional.empty());

        assertThrows(BusinessException.class,
                () -> parkingService.registerParking("ABC1234", -23.56, -46.65, LocalDateTime.now()));
    }

    @Test
    void shouldFindLatestByPlateSuccessfully() {
        Mockito.when(parkingSessionRepository.findFirstByPlateOrderByEntryTimeDesc("ABC1234"))
                .thenReturn(Optional.of(session));

        ParkingSessionDTO dto = new ParkingSessionDTO();
        Mockito.when(parkingSessionMapper.toDto(session)).thenReturn(dto);

        ParkingSessionDTO result = parkingService.findLatestByPlate("ABC1234");

        assertNotNull(result);
        Mockito.verify(parkingSessionMapper, Mockito.times(1)).toDto(session);
    }
}
