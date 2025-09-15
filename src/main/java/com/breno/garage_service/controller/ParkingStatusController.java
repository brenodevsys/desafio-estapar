package com.breno.garage_service.controller;

import com.breno.garage_service.dto.ParkingSessionDTO;
import com.breno.garage_service.service.ParkingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@Tag(
        name = "Parking Status API",
        description = "APIs para consultar status de placas, vagas e receita do estacionamento"
)
public class ParkingStatusController {

    private final ParkingService parkingService;

    @Operation(
            summary = "Consulta status da placa",
            description = "Retorna a última sessão de estacionamento registrada para uma placa específica."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sessão encontrada",
                    content = @Content(schema = @Schema(implementation = ParkingSessionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Placa inválida ou sem sessão aberta", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping("/plate-status")
    public ParkingSessionDTO plateStatus(
            @Parameter(description = "Número da placa do veículo", example = "ZUL0001")
            @RequestParam String plate
    ) {
        return parkingService.findLatestByPlate(plate);
    }

    @Operation(
            summary = "Consulta status da vaga",
            description = "Retorna a sessão de estacionamento associada a uma vaga pelo seu ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sessão encontrada",
                    content = @Content(schema = @Schema(implementation = ParkingSessionDTO.class))),
            @ApiResponse(responseCode = "400", description = "ID da vaga inválido ou não encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping("/spot-status")
    public ParkingSessionDTO spotStatus(
            @Parameter(description = "Identificador único da vaga", example = "101")
            @RequestParam Long spotId
    ) {
        return parkingService.findBySpot(spotId);
    }

    @Operation(
            summary = "Consulta receita por setor",
            description = "Calcula a receita total de um setor em um intervalo de datas."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Receita calculada com sucesso",
                    content = @Content(schema = @Schema(implementation = BigDecimal.class))),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos (setor ou datas)", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping("/revenue")
    public BigDecimal revenue(
            @Parameter(description = "Código do setor", example = "A")
            @RequestParam String sector,

            @Parameter(
                    description = "Data inicial no formato ISO-8601",
                    example = "2025-01-01T00:00:00",
                    schema = @Schema(type = "string", format = "date-time")
            )
            @RequestParam LocalDateTime start,

            @Parameter(
                    description = "Data final no formato ISO-8601",
                    example = "2025-01-31T23:59:59",
                    schema = @Schema(type = "string", format = "date-time")
            )
            @RequestParam LocalDateTime end
    ) {
        return parkingService.calculateRevenue(sector, start, end);
    }
}
