package com.ifood.quarkus.crud.test;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import com.github.database.rider.cdi.api.DBRider;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.ifood.quarkus.crud.service.EntregadorService;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@DBRider
@DBUnit(caseSensitiveTableNames = false,caseInsensitiveStrategy = Orthography.LOWERCASE)
@QuarkusTestResource(DatabaseLifecycle.class)
public class EntregadorTest {

	@Inject
	private EntregadorService entregadorService;
	
	@Test
	@TestTransaction
	public void salvaEntregador() {
		this.entregadorService.salvar();
	}
}
