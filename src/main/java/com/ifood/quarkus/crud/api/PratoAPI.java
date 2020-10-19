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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.SimplyTimed;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import com.ifood.quarkus.crud.dto.AdicionarPratoDTO;
import com.ifood.quarkus.crud.dto.AtualizarPratoDTO;
import com.ifood.quarkus.crud.dto.PratoDTO;
import com.ifood.quarkus.crud.dto.PratoMapper;
import com.ifood.quarkus.crud.entity.Prato;
import com.ifood.quarkus.crud.entity.Restaurante;

@Path("restaurantes/{idRestaurante}/pratos")
@Tag(name = "pratos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PratoAPI {

	@Inject
	PratoMapper pratoMapper;

	@GET
	@Counted(name = "Número de chamadas buscarPratos") // Conta quantas vezes o método foi chamado
	@SimplyTimed(name = "Tempo simples para executar buscarPratos")// Mostra o tempo simples do método
	@Timed(name = "Tempo total para executar buscarPratos")// Mostra o tempo total do método
	//@Gauge Pega informações de recursos, quantidade de memória, espaço em disco, temperatura do processador, número de elementos em uma fila, número de elementos em uma tabela
	public List<PratoDTO> buscarPratos(@PathParam("idRestaurante") Long idRestaurante) {
		Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
		if (restauranteOp.isEmpty()) {
			throw new NotFoundException("Restaurante não existe");
		}
		Stream<Prato> pratos = Prato.stream("restaurante", restauranteOp.get());
		return pratos.map(p -> pratoMapper.toDTO(p)).collect(Collectors.toList());
	}

	@POST
	@Transactional
	public Response adicionarPrato(@PathParam("idRestaurante") Long idRestaurante, @Valid AdicionarPratoDTO dto) {
		Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
		if (restauranteOp.isEmpty()) {
			throw new NotFoundException("Restaurante não existe");
		}
		// //Utilizando dto, recebi detached entity passed to persist:
		// Prato prato = new Prato();
		// prato.nome = dto.nome;
		// prato.descricao = dto.descricao;
		//
		// prato.preco = dto.preco;
		// prato.restaurante = restauranteOp.get();
		// prato.persist();

		Prato prato = pratoMapper.toPrato(dto);
		prato.restaurante = restauranteOp.get();
		prato.persist();
		return Response.status(Status.CREATED).build();
	}

	@PUT
	@Path("{id}")
	@Transactional
	public void atualizar(@PathParam("idRestaurante") Long idRestaurante, @PathParam("id") Long id,
			@Valid AtualizarPratoDTO dto) {
		Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
		if (restauranteOp.isEmpty()) {
			throw new NotFoundException("Restaurante não existe");
		}

		// No nosso caso, id do prato vai ser único, independente do restaurante...
		Optional<Prato> pratoOp = Prato.findByIdOptional(id);

		if (pratoOp.isEmpty()) {
			throw new NotFoundException("Prato não existe");
		}
		Prato prato = pratoOp.get();
	    pratoMapper.toPrato(dto, prato);
		prato.persist();
	}

	@DELETE
	@Path("{id}")
	@Transactional
	public void delete(@PathParam("idRestaurante") Long idRestaurante, @PathParam("id") Long id) {
		Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
		if (restauranteOp.isEmpty()) {
			throw new NotFoundException("Restaurante não existe");
		}

		Optional<Prato> pratoOp = Prato.findByIdOptional(id);

		pratoOp.ifPresentOrElse(Prato::delete, () -> {
			throw new NotFoundException();
		});
	}

}
