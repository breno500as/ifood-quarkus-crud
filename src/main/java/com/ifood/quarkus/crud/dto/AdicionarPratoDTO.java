package com.ifood.quarkus.crud.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;


public class AdicionarPratoDTO {
	

	@NotNull
	public String nome;

	@NotNull
	public String descricao;

	public BigDecimal preco;

}
