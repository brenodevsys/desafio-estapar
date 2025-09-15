package com.breno.garage_service.enumerate;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Tipos possíveis de evento do simulador")
public enum EventType { ENTRY, PARKED, EXIT }
