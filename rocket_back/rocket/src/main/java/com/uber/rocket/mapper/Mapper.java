package com.uber.rocket.mapper;

public interface Mapper<Entity, DTO> {

    DTO mapToDto(Entity entity);
}
