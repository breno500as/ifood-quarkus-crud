package com.ifood.quarkus.crud.api;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.OAuthFlow;
import org.eclipse.microprofile.openapi.annotations.security.OAuthFlows;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirements;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

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
@RolesAllowed("proprietario")
@SecurityScheme(securitySchemeName = "ifood-oauth", type = SecuritySchemeType.OAUTH2, flows = @OAuthFlows(password = @OAuthFlow(tokenUrl = "http://localhost:8180/auth/realms/ifood/protocol/openid-connect/token")))
@SecurityRequirements(value = { @SecurityRequirement(name = "ifood-oauth", scopes = {}) })
public class RestauranteAPI {

	@Inject
	private RestauranteMapper restauranteMapper;

	@Inject
	@Channel("restaurantes")
	private Emitter<Restaurante> emitter;
	
	@Inject
	private JsonWebToken token; // Objeto que representa o conteúdo do token (header / payload)

	@Claim(value = "preferred_username")
	private String nomeProprietarioToken; // String que representa o claim preferred_username do token (nome do usuário logado do token)
	
	@Claim(standard = Claims.sub)
	private String sub;  // String que representa o claim subject do token (subject do usuário logado do token)
	
	@GET
	public List<RestauranteDTO> listAll() {
		Stream<Restaurante> restaurantes = Restaurante.streamAll();
		return restaurantes.map(r -> restauranteMapper.toRestauranteDTO(r)).collect(Collectors.toList());
	}

	@POST
	@Transactional
	@APIResponse(responseCode = "201", description = "Caso restaurante seja cadastrado com sucesso")
	@APIResponse(responseCode = "400", content = @Content(schema = @Schema(allOf = ConstraintViolationResponse.class)))
	public Response cria(@Valid AdicionarRestauranteDTO restauranteDTO) {
		Restaurante r = this.restauranteMapper.toRestaurante(restauranteDTO);
		r.proprietario = this.nomeProprietarioToken;
		r.persist();
		this.emitter.send(r);
		return Response.status(Status.CREATED).build();
	}

	@PUT
	@Path("{id}")
	@Transactional
	public Response atualiza(@PathParam("id") Long id, @Valid AtualizarRestauranteDTO dto) {
		Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(id);
		if (restauranteOp.isEmpty()) {
			throw new NotFoundException();
		}
		Restaurante restaurante = restauranteOp.get();

		 if (!restaurante.proprietario.equals(this.nomeProprietarioToken)) {
		 throw new ForbiddenException();
		 }

		// MapStruct: aqui passo a referencia para ser atualizada
		this.restauranteMapper.toRestaurante(dto, restaurante);

		restaurante.persist();
		
		return Response.ok().build();
	}

	@DELETE
	@Path("{id}")
	@Transactional
	public Response delete(@PathParam("id") Long id) {
		Optional<Restaurante> optional = Restaurante.findByIdOptional(id);

		optional.ifPresentOrElse(Restaurante::delete, () -> {
			throw new NotFoundException();
		});

		return Response.noContent().build();
	}

}
