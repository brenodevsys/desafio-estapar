package com.breno.garage_service.service;

import com.breno.garage_service.entity.ParkingSession;
import com.breno.garage_service.entity.Sector;
import com.breno.garage_service.entity.Spot;
import com.breno.garage_service.enumerate.SpotStatus;
import com.breno.garage_service.repository.SpotRepository;
import com.breno.garage_service.util.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PricingServiceTest {

    @Mock
    private SpotRepository spotRepository;
    @InjectMocks
    private PricingService pricingService;

    private ParkingSession session;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        Sector sector = TestUtils.getMockSector();
        Spot spot = TestUtils.getMockSpot(sector);
        session = TestUtils.getMockSession(sector, spot);
    }

    @Test
    void shouldApplySurchargeHighWhenOccupancyAbove75() {
        Mockito.when(spotRepository.count()).thenReturn(100L);
        Mockito.when(spotRepository.countByStatus(SpotStatus.OCCUPIED)).thenReturn(90L);

        BigDecimal factor = pricingService.factorByOccupancy();
        assertTrue(factor.compareTo(BigDecimal.ONE) > 0);
    }

    @Test
    void shouldCalculateTotalChargeCorrectly() {
        BigDecimal total = pricingService.calculateTotalCharge(session, 130); // 2h10m
        assertNotNull(total);
        assertTrue(total.compareTo(BigDecimal.ZERO) > 0);
    }
}
