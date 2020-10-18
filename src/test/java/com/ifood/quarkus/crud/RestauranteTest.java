package com.ifood.quarkus.crud;

import javax.ws.rs.core.Response.Status;

import org.approvaltests.Approvals;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.database.rider.cdi.api.DBRider;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.ifood.quarkus.crud.dto.AtualizarRestauranteDTO;
import com.ifood.quarkus.crud.entity.Restaurante;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.specification.RequestSpecification;

@QuarkusTest
@DBRider
@DBUnit(caseSensitiveTableNames = false,caseInsensitiveStrategy = Orthography.LOWERCASE)
@QuarkusTestResource(DatabaseLifecycle.class)
public class RestauranteTest {
	
	private String token;

	@Test
	@DataSet(value = "restaurantes1.yml")
	public void getRestaurantes() {
		String resultado = given().when().get("/restaurantes").then().statusCode(200).extract().asString();
		Approvals.verifyJson(resultado);
	}
	
	@BeforeEach
	public void getToken() throws Exception {
		this.token = TokenUtils.generateTokenString("/JWTProprietarioClaims.json", null);
	}

	private RequestSpecification given() {
		return RestAssured.given().contentType(ContentType.JSON).header(new Header("Authorization", "Bearer " + token));
	}

	// @Test
	// @DataSet("restaurantes1.yml")
	public void testAlterarRestaurante() {
		AtualizarRestauranteDTO dto = new AtualizarRestauranteDTO();
		dto.nome = "novoNome";
		Long parameterValue = 123L;
		
		given().with().pathParam("id", parameterValue).body(dto).when().put("/restaurantes/{id}").then()
				.statusCode(Status.NO_CONTENT.getStatusCode()).extract().asString();

		Restaurante findById = Restaurante.findById(parameterValue);

		// poderia testar todos os outros atribudos
		Assert.assertEquals(dto.nome, findById.nome);

	}
}
