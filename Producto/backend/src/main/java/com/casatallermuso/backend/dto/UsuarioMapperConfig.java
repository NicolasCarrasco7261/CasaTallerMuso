package com.casatallermuso.backend.dto;

import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import com.casatallermuso.backend.entities.Usuario;

@MapperConfig
public interface UsuarioMapperConfig {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "claveHash", ignore = true)
    @Mapping(target = "activo", ignore = true)
    @Mapping(target = "rol", ignore = true)
    @Mapping(target = "detalleUsuario.id", ignore = true)
    @Named("ignorarCamposCliente")
    void ignorarCamposClienteTemplate(UsuarioDTO.BaseDTO dto, @MappingTarget Usuario entity);

}
