package com.breno.garage_service.util;


import com.breno.garage_service.dto.GarageConfigDTO;
import com.breno.garage_service.dto.SectorConfigDTO;
import com.breno.garage_service.dto.SpotConfigDTO;
import com.breno.garage_service.entity.ParkingSession;
import com.breno.garage_service.entity.Sector;
import com.breno.garage_service.entity.Spot;
import com.breno.garage_service.enumerate.SessionStatus;
import com.breno.garage_service.enumerate.SpotStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class TestUtils {

    public static Sector getMockSector() {
        return Sector.builder()
                .id(1L)
                .code("A")
                .basePrice(BigDecimal.TEN)
                .maxCapacity(10)
                .build();
    }

    public static Spot getMockSpot(Sector sector) {
        return Spot.builder()
                .id(100L)
                .sector(sector)
                .lat(-23.56)
                .lng(-46.65)
                .status(SpotStatus.AVAILABLE)
                .build();
    }

    public static ParkingSession getMockSession(Sector sector, Spot spot) {
        return ParkingSession.builder()
                .id(200L)
                .plate("ABC1234")
                .sector(sector)
                .spot(spot)
                .entryTime(LocalDateTime.now().minusHours(1))
                .status(SessionStatus.OPEN)
                .priceFactor(BigDecimal.ONE)
                .effectivePriceHour(BigDecimal.TEN)
                .build();
    }

    public static GarageConfigDTO getMockGarageConfig() {
        SectorConfigDTO sectorConfig = SectorConfigDTO.builder()
                .sector("A")
                .basePrice(BigDecimal.TEN)
                .maxCapacity(10)
                .build();

        SpotConfigDTO spotConfig = SpotConfigDTO.builder()
                .id(100L)
                .sector("A")
                .lat(-23.56)
                .lng(-46.65)
                .build();

        return GarageConfigDTO.builder()
                .garage(List.of(sectorConfig))
                .spots(List.of(spotConfig))
                .build();
    }
}
