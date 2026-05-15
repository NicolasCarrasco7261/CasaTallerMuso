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
    Usuario toEntity(UsuarioDTO.ObtenerCliente dto);

    @InheritConfiguration(name = "ignorarCamposClienteTemplate")
    Usuario toEntity(UsuarioDTO.CrearCliente dto);

    UsuarioDTO.ObtenerCliente toObtenerClienteDTO(Usuario usuario);

    @Mapping(target = "claveSinCifrar", ignore = true)
    UsuarioDTO.CrearCliente toCrearClienteDTO(Usuario usuario);

}
