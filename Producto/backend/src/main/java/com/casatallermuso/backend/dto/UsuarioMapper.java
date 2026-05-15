package com.casatallermuso.backend.dto;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.casatallermuso.backend.entities.Usuario;

@Mapper(componentModel = "spring", config = UsuarioMapperConfig.class)
public interface UsuarioMapper {
    
    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);

    @InheritConfiguration(name = "ignorarCamposClienteTemplate")
    Usuario toEntity(UsuarioDTO.Vista dto);

    @InheritConfiguration(name = "ignorarCamposClienteTemplate")
    Usuario toEntity(UsuarioDTO.Registro dto);

    UsuarioDTO.Vista toVistaDTO(Usuario usuario);

    @Mapping(target = "clave", ignore = true)
    UsuarioDTO.Registro toRegistroDTO(Usuario usuario);

}
