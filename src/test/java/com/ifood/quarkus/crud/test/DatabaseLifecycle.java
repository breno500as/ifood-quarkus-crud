package com.ifood.quarkus.crud.test;

import java.util.HashMap;
import java.util.Map;

import org.testcontainers.containers.PostgreSQLContainer;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class DatabaseLifecycle implements QuarkusTestResourceLifecycleManager {
	
	private static PostgreSQLContainer<?> POSTGRESQL = new PostgreSQLContainer("postgres:11");
	
	/**
	 * Baixa um container postgres e retorna um mapa de propriedades para customizar o container do postgres.
	 */
	@Override
	public Map<String, String> start() {
		POSTGRESQL.start();
		Map<String, String> propriedades = new HashMap<>();
		propriedades.put("quarkus.datasource.jdbc.url", POSTGRESQL.getJdbcUrl());
		propriedades.put("quarkus.datasource.username", POSTGRESQL.getUsername());
		propriedades.put("quarkus.datasource.password", POSTGRESQL.getPassword());
		 
		 
		return propriedades;
	}
	
	/**
	 * Executa o método quando se está parando os testes.
	 */

	@Override
	public void stop() {
		if (POSTGRESQL != null && POSTGRESQL.isRunning()) {
			POSTGRESQL.stop();
		}
	}

}
