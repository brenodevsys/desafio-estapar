package com.breno.garage_service.service;

import com.breno.garage_service.dto.GarageConfigDTO;
import com.breno.garage_service.entity.Sector;
import com.breno.garage_service.entity.Spot;
import com.breno.garage_service.enumerate.SpotStatus;
import com.breno.garage_service.repository.SectorRepository;
import com.breno.garage_service.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GarageSyncService {

    private static final int MAX_RETRIES = 8;
    private static final long RETRY_DELAY_MS = 1500;

    private final RestClient restClient;
    private final SectorRepository sectorRepository;
    private final SpotRepository spotRepository;

    @Value("${app.simulator-base-url}")
    private String simulatorBaseUrl;

    public void syncGarageAndSpots() {
        Optional.ofNullable(fetchConfigWithRetry())
                .ifPresentOrElse(this::persistConfig,
                        () -> log.error("Não foi possível carregar configuração da garagem após {} tentativas.", MAX_RETRIES));
    }

    private void persistConfig(GarageConfigDTO config) {
        spotRepository.deleteAll();
        sectorRepository.deleteAll();

        config.getGarage().stream()
                .map(dto -> Sector.builder()
                        .code(dto.getSector())
                        .basePrice(dto.getBasePrice())
                        .maxCapacity(dto.getMaxCapacity())
                        .build())
                .forEach(sectorRepository::save);

        config.getSpots().stream()
                .map(dto -> Spot.builder()
                        .id(dto.getId())
                        .sector(sectorRepository.findByCode(dto.getSector())
                                .orElseThrow(() -> new IllegalStateException("Setor não encontrado: " + dto.getSector())))
                        .lat(dto.getLat())
                        .lng(dto.getLng())
                        .status(SpotStatus.AVAILABLE)
                        .build())
                .forEach(spotRepository::save);

        log.info("Sync concluído: {} setores, {} vagas", sectorRepository.count(), spotRepository.count());
    }

    private GarageConfigDTO fetchConfigWithRetry() {
        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                log.info("Buscando configuração do simulador em {}/garage (tentativa {}/{})",
                        simulatorBaseUrl, attempt, MAX_RETRIES);

                GarageConfigDTO config = restClient.get()
                        .uri(simulatorBaseUrl + "/garage")
                        .retrieve()
                        .body(GarageConfigDTO.class);

                if (config != null) return config;
                log.warn("Resposta vazia do simulador (tentativa {}/{})", attempt, MAX_RETRIES);

            } catch (ResourceAccessException ex) {
                log.warn("Simulador não respondeu (tentativa {}/{}). Erro: {}",
                        attempt, MAX_RETRIES, ex.getClass().getSimpleName());
            } catch (Exception ex) {
                log.error("Falha ao buscar /garage: {}", ex.getMessage(), ex);
            }

            try {
                Thread.sleep(RETRY_DELAY_MS);
            } catch (InterruptedException ignored) {}
        }
        return null;
    }
}
