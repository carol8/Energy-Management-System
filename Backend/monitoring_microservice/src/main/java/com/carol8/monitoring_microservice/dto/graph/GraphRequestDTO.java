package com.carol8.monitoring_microservice.dto.graph;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GraphRequestDTO {
    private UUID userUuid;
    private UUID deviceUuid;
    private String date;
}
