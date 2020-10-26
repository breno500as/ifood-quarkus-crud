package com.ifood.quarkus.crud.api;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import com.ifood.quarkus.crud.entity.Entregador;

import io.quarkus.hibernate.orm.rest.data.panache.PanacheEntityResource;

@Tag(name = "entregadores")
public interface EntregadoresResource extends PanacheEntityResource<Entregador, Long> {

}
