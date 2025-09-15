package com.breno.garage_service.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.breno.garage_service.service.GarageSyncService;

@Configuration
public class StartupSync {

    private final GarageSyncService garageSyncService;

    public StartupSync(GarageSyncService syncService) {
        this.garageSyncService = syncService;
    }

    @Bean
    public ApplicationRunner runOnStart() {
        return args -> garageSyncService.syncGarageAndSpots();
    }
}