package com.pi.beesmart.controller;

import com.pi.beesmart.model.OutputEntity;

import java.util.List;
import java.util.stream.Collectors;

public class OutputConverter {
    public List<OutputDTO> toDTO(List<OutputEntity> entities) {

        return entities //
                .stream() //
                .map(entity -> new OutputDTO(entity.gpio, entity.name, entity.status, entity.type, entity.temperature)) //
                .collect(Collectors.toList());
    }

    public OutputDTO toDTO(OutputEntity entity) {
        return new OutputDTO(entity.gpio, entity.name, entity.status, entity.type, entity.temperature);
    }

    public OutputEntity toEntity(OutputDTO dto) {
        return new OutputEntity(dto.gpio, dto.name, dto.status, dto.type, dto.temperature);
    }
}
