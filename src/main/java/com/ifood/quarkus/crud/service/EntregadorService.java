package com.ifood.quarkus.crud.service;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import com.ifood.quarkus.crud.entity.Entregador;

@ApplicationScoped
@Transactional
public class EntregadorService {
 
	 public void salvar() {
		 Entregador e = new Entregador();
		 e.nome = "teste";
		 e.persist();
	 }
	
}
