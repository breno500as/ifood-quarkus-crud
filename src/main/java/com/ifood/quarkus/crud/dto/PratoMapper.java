package com.ifood.quarkus.crud.dto;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.ifood.quarkus.crud.entity.Prato;


@Mapper(componentModel = "cdi")
public interface PratoMapper {
	
	PratoDTO toDTO(Prato p);

    Prato toPrato(AdicionarPratoDTO dto);

    void toPrato(AtualizarPratoDTO dto, @MappingTarget Prato prato);

}
