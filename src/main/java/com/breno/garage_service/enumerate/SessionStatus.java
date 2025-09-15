package com.breno.garage_service.enumerate;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Tipos possíveis de SESSOES do simulador")
public enum SessionStatus {
    OPEN,     // sessão em andamento
    CLOSED,   // sessão finalizada
    ENTRY,    // carro entrou
    PARKED,   // carro estacionado
    EXIT      // carro saiu
}

