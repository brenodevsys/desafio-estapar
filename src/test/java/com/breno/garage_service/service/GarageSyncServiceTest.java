package com.breno.garage_service.service;

import com.breno.garage_service.dto.GarageConfigDTO;
import com.breno.garage_service.entity.Sector;
import com.breno.garage_service.entity.Spot;
import com.breno.garage_service.repository.SectorRepository;
import com.breno.garage_service.repository.SpotRepository;
import com.breno.garage_service.util.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestClient;

import java.util.Optional;

class GarageSyncServiceTest {

    @Mock private RestClient restClient;
    @Mock private SectorRepository sectorRepository;
    @Mock private SpotRepository spotRepository;

    @InjectMocks private GarageSyncService garageSyncService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldPersistConfigSuccessfully() {

        GarageConfigDTO config = TestUtils.getMockGarageConfig();
        Sector mockSector = TestUtils.getMockSector();

        RestClient.RequestHeadersUriSpec uriSpec = Mockito.mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.ResponseSpec responseSpec = Mockito.mock(RestClient.ResponseSpec.class);

        Mockito.when(restClient.get()).thenReturn(uriSpec);
        Mockito.when(uriSpec.uri(Mockito.anyString())).thenReturn(uriSpec);
        Mockito.when(uriSpec.retrieve()).thenReturn(responseSpec);
        Mockito.when(responseSpec.body(GarageConfigDTO.class)).thenReturn(config);

        Mockito.when(sectorRepository.findByCode("A")).thenReturn(Optional.of(mockSector));

        garageSyncService.syncGarageAndSpots();

        Mockito.verify(spotRepository, Mockito.atLeastOnce()).deleteAll();
        Mockito.verify(sectorRepository, Mockito.atLeastOnce()).deleteAll();
        Mockito.verify(sectorRepository, Mockito.atLeastOnce()).save(Mockito.any(Sector.class));
        Mockito.verify(spotRepository, Mockito.atLeastOnce()).save(Mockito.any(Spot.class));
    }

}
