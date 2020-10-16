package com.ifood.quarkus.crud.api;


import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ifood.quarkus.crud.entity.Restaurante;

@Path("/restaurantes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestauranteAPI {
	
	@GET
	public List<Restaurante> listAll(){
		return Restaurante.listAll();
	}

}
