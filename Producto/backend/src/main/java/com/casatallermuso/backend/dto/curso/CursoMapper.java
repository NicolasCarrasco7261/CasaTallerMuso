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

    Curso toEntity(CursoDTO.CardView dto);
    CursoDTO.CardView toCursoCardViewDTO(Curso curso);

    Curso toEntity(CursoDTO.ClientView dto);
    CursoDTO.ClientView toCursoClientViewDTO(Curso curso);

    Curso toEntity(CursoDTO.AdminView dto);
    CursoDTO.AdminView toCursoAdminViewDTO(Curso curso);

    Curso toEntity(CursoDTO.Post dto);
    CursoDTO.Post toCursoPost(Curso curso);

}
