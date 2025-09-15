package com.breno.garage_service.controller;

import com.breno.garage_service.dto.WebhookEventDTO;
import com.breno.garage_service.dto.ExitResponse;
import com.breno.garage_service.service.ParkingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/webhook")
@Tag(
        name = "Webhook API",
        description = "Endpoint que recebe eventos ENTRY, PARKED e EXIT enviados pelo simulador da garagem"
)
public class WebhookController {

    private final ParkingService parkingService;

    @Operation(
            summary = "Processa evento do simulador",
            description = "Recebe eventos ENTRY, PARKED ou EXIT enviados pelo simulador de garagem e atualiza o sistema."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Evento aceito com sucesso"),
            @ApiResponse(responseCode = "200", description = "Saída processada com sucesso, valor calculado retornado",
                    content = @Content(schema = @Schema(implementation = ExitResponse.class))),
            @ApiResponse(responseCode = "400", description = "Evento inválido ou parâmetros incorretos"),
            @ApiResponse(responseCode = "500", description = "Erro interno ao processar evento")
    })
    @PostMapping
    public ResponseEntity<?> processWebhook(@Valid @RequestBody WebhookEventDTO event) {
        return switch (event.getEventType()) {
            case ENTRY -> {
                parkingService.registerEntry(event.getLicensePlate(), event.getEntryTime());
                yield ResponseEntity.accepted().build();
            }
            case PARKED -> {
                parkingService.registerParking(event.getLicensePlate(), event.getLat(), event.getLng(), LocalDateTime.now());
                yield ResponseEntity.accepted().build();
            }
            case EXIT -> {
                BigDecimal amount = parkingService.registerExit(event.getLicensePlate(), event.getExitTime());
                yield ResponseEntity.ok(new ExitResponse(amount));
            }
            default -> ResponseEntity.badRequest().body("Evento inválido");
        };
    }
}
