package com.casatallermuso.backend.dto.evento;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.casatallermuso.backend.entities.Evento;
import com.casatallermuso.backend.entities.HorarioEvento;

@Mapper(componentModel = "spring")
public interface EventoMapper {
    
    EventoMapper INSTANCE = Mappers.getMapper(EventoMapper.class);

    HorarioEvento toEntity(HorarioEventoDTO dto);
    HorarioEventoDTO toHorarioEventoDTO(HorarioEvento horarioEvento);

    Evento toEntity(EventoDTO.CardView dto);
    EventoDTO.CardView toEventoCardViewDTO(Evento evento);

    Evento toEntity(EventoDTO.ClientView dto);
    EventoDTO.ClientView toEventoClientViewDTO(Evento evento);

    Evento toEntity(EventoDTO.AdminView dto);
    EventoDTO.AdminView toEventoAdminViewDTO(Evento evento);

    Evento toEntity(EventoDTO.Post dto);
    EventoDTO.Post toEventoPost(Evento evento);

}
