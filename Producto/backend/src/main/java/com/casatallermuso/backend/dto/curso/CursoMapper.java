package com.casatallermuso.backend.dto.curso;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.casatallermuso.backend.entities.Curso;
import com.casatallermuso.backend.entities.HorarioCurso;

@Mapper(componentModel = "spring")
public interface CursoMapper {
    
    CursoMapper INSTANCE = Mappers.getMapper(CursoMapper.class);

    HorarioCurso toEntity(HorarioCursoDTO dto);
    HorarioCursoDTO toHorarioCursoDTO(HorarioCurso horarioCurso);

    Curso toEntity(CursoDTO.Card dto);
    CursoDTO.Card toCursoCardDTO(Curso curso);

    Curso toEntity(CursoDTO.View dto);
    CursoDTO.View toCursoViewDTO(Curso curso);

}
