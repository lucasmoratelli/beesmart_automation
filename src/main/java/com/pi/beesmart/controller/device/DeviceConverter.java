package com.pi.beesmart.controller.device;

import com.pi.beesmart.model.device.DeviceEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
@Component
public class DeviceConverter {
    public List<DeviceDTO> toDTO(List<DeviceEntity> entities) {

        return entities //
                .stream() //
                .map(entity -> new DeviceDTO(entity.id, entity.gpio, entity.name, entity.type, entity.value)) //
                .collect(Collectors.toList());
    }

    public DeviceDTO toDTO(DeviceEntity entity) {
        return new DeviceDTO(entity.id, entity.gpio, entity.name, entity.type, entity.value);
    }

    public DeviceEntity toEntity(DeviceDTO dto) {
        return new DeviceEntity(dto.id, dto.gpio, dto.name, dto.type, dto.value);
    }
}
