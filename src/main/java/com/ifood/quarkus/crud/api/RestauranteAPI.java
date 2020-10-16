package com.ifood.quarkus.crud.api;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ifood.quarkus.crud.entity.Restaurante;

@Path("/restaurantes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestauranteAPI {

	@GET
	public List<Restaurante> listAll() {
		return Restaurante.listAll();
	}

	@POST
	@Transactional
	public void cria(Restaurante restauranteDTO) {
		restauranteDTO.persist();
	}

	@PUT
	@Path("{id}")
	@Transactional
	public void atualiza(@PathParam("id") Long id, Restaurante restauranteDTO) {
		Optional<Restaurante> optional = Restaurante.findByIdOptional(id);

		if (optional.isPresent()) {
			Restaurante r = optional.get();
			r.nome = restauranteDTO.nome;
			r.persist();
		} else {
			throw new NotFoundException();
		}
	}

	@DELETE
	@Path("{id}")
	@Transactional
	public void delete(@PathParam("id") Long id) {
		Optional<Restaurante> optional = Restaurante.findByIdOptional(id);

		optional.ifPresentOrElse(Restaurante::delete, () -> {
			throw new NotFoundException();
		});

	}

}
