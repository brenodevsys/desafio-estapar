package com.breno.garage_service.dto;

import com.breno.garage_service.enumerate.EventType;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Evento recebido pelo Webhook do simulador da garagem")
public class WebhookEventDTO {

    @NotBlank
    @JsonProperty("license_plate")
    @Schema(description = "Placa do veículo", example = "ZUL0001")
    private String licensePlate;

    @JsonProperty("entry_time")
    @Schema(type = "string", format = "date-time", description = "Data/hora de entrada do veículo", example = "2025-01-01T12:00:00")
    private LocalDateTime entryTime;

    @JsonProperty("exit_time")
    @Schema(type = "string", format = "date-time", description = "Data/hora de saída do veículo", example = "2025-01-01T14:30:00")
    private LocalDateTime exitTime;

    @Schema(description = "Latitude da vaga (usado em eventos PARKED)", example = "-23.561684")
    private Double lat;

    @Schema(description = "Longitude da vaga (usado em eventos PARKED)", example = "-46.655981")
    private Double lng;

    @NotNull
    @JsonProperty("event_type")
    @Schema(description = "Tipo do evento enviado pelo simulador", example = "ENTRY")
    private EventType eventType;
}
