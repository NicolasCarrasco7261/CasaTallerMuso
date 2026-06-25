package com.casatallermuso.backend.dto.curso;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.casatallermuso.backend.entities.Curso;
import com.casatallermuso.backend.entities.HorarioCurso;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CursoMapper {
    
    CursoMapper INSTANCE = Mappers.getMapper(CursoMapper.class);

    @Mapping(target = "curso", ignore = true)
    HorarioCurso toEntity(HorarioCursoDTO dto);
    HorarioCursoDTO toHorarioCursoDTO(HorarioCurso horarioCurso);

    Curso toEntity(CursoDTO.CardView dto);
    CursoDTO.CardView toCursoCardViewDTO(Curso curso);

    Curso toEntity(CursoDTO.ClientView dto);
    CursoDTO.ClientView toCursoClientViewDTO(Curso curso);

    Curso toEntity(CursoDTO.AdminView dto);
    CursoDTO.AdminView toCursoAdminViewDTO(Curso curso);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creadoEn", ignore = true)
    Curso toEntity(CursoDTO.Post dto);
    CursoDTO.Post toCursoPost(Curso curso);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creadoEn", ignore = true)
    void updateCursoFromDTO(CursoDTO.Put dto, @MappingTarget Curso curso);
}
