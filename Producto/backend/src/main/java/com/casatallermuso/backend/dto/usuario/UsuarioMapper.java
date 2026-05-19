package com.casatallermuso.backend.dto.usuario;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.casatallermuso.backend.entities.DetalleUsuario;
import com.casatallermuso.backend.entities.RolUsuario;
import com.casatallermuso.backend.entities.UbicacionUsuario;
import com.casatallermuso.backend.entities.Usuario;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UsuarioMapper {
    
    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);

    RolUsuario toEntity(RolDTO dto);
    RolDTO toRolDTO(RolUsuario rolUsuario);

    DetalleUsuario toEntity(DetalleDTO dto);
    DetalleDTO toDetalleDTO(DetalleUsuario detalleUsuario);

    UbicacionUsuario toEntity(UbicacionDTO dto);
    UbicacionDTO toUbicacionDTO(UbicacionUsuario ubicacionUsuario);

    Usuario toEntity(UsuarioDTO.Perfil perfil);
    UsuarioDTO.Perfil toPerfilDTO(Usuario usuario);

    Usuario toEntity(UsuarioDTO.PerfilCorreo perfilCorreo);
    UsuarioDTO.PerfilCorreo toPerfilCorreoDTO(Usuario usuario);

    Usuario toEntity(UsuarioDTO.Cuenta cuenta);
    UsuarioDTO.Cuenta toCuentaDTO(Usuario usuario);

    Usuario toEntity(UsuarioDTO.PerfilId perfilId);
    UsuarioDTO.PerfilId toPerfilIdDTO(Usuario usuario);

    Usuario toEntity(UsuarioDTO.CuentaId cuentaId);
    UsuarioDTO.CuentaId toCuentaIdDTO(Usuario usuario);

    void updateUsuarioFromDTO(UsuarioDTO.Update dto, @MappingTarget Usuario usuario);

}
