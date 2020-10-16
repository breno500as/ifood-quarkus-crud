package com.ifood.quarkus.crud.api;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
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

import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import com.ifood.quarkus.crud.dto.AdicionarRestauranteDTO;
import com.ifood.quarkus.crud.dto.AtualizarRestauranteDTO;
import com.ifood.quarkus.crud.dto.RestauranteDTO;
import com.ifood.quarkus.crud.dto.RestauranteMapper;
import com.ifood.quarkus.crud.entity.Restaurante;
import com.ifood.quarkus.crud.exception.ConstraintViolationResponse;

@Path("/restaurantes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "restaurantes")
public class RestauranteAPI {

	@Inject
	private RestauranteMapper restauranteMapper;

	@GET
	public List<RestauranteDTO> listAll() {
		Stream<Restaurante> restaurantes = Restaurante.streamAll();
		return restaurantes.map(r -> restauranteMapper.toRestauranteDTO(r)).collect(Collectors.toList());
	}

	@POST
	@Transactional
	@APIResponse(responseCode = "201", description = "Caso restaurante seja cadastrado com sucesso")
    @APIResponse(responseCode = "400", content = @Content(schema = @Schema(allOf = ConstraintViolationResponse.class)))
	public void cria(@Valid AdicionarRestauranteDTO restauranteDTO) {
		Restaurante r = this.restauranteMapper.toRestaurante(restauranteDTO);
		r.persist();
	}

	@PUT
	@Path("{id}")
	@Transactional
	public void atualiza(@PathParam("id") Long id, @Valid AtualizarRestauranteDTO dto) {
		Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(id);
		if (restauranteOp.isEmpty()) {
			throw new NotFoundException();
		}
		Restaurante restaurante = restauranteOp.get();

		// if (!restaurante.proprietario.equals(sub)) {
		// throw new ForbiddenException();
		// }

		// MapStruct: aqui passo a referencia para ser atualizada
		restauranteMapper.toRestaurante(dto, restaurante);

		restaurante.persist();
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
