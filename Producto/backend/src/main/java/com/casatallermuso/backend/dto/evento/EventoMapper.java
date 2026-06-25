package com.casatallermuso.backend.dto.evento;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.casatallermuso.backend.entities.Evento;
import com.casatallermuso.backend.entities.HorarioEvento;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface EventoMapper {
    
    EventoMapper INSTANCE = Mappers.getMapper(EventoMapper.class);

    @Mapping(target = "evento", ignore = true)
    HorarioEvento toEntity(HorarioEventoDTO dto);
    HorarioEventoDTO toHorarioEventoDTO(HorarioEvento horarioEvento);

    Evento toEntity(EventoDTO.CardView dto);
    EventoDTO.CardView toEventoCardViewDTO(Evento evento);

    Evento toEntity(EventoDTO.ClientView dto);
    EventoDTO.ClientView toEventoClientViewDTO(Evento evento);

    Evento toEntity(EventoDTO.AdminView dto);
    EventoDTO.AdminView toEventoAdminViewDTO(Evento evento);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creadoEn", ignore = true)
    Evento toEntity(EventoDTO.Post dto);
    EventoDTO.Post toEventoPost(Evento evento);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creadoEn", ignore = true)
    void updateEventoFromDTO(EventoDTO.Put dto, @MappingTarget Evento evento);
}
