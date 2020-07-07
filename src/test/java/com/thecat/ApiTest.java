package com.thecat;

import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class ApiTest extends MassaDeDados {

    @BeforeClass
    public static void setUpClass() {
        baseURI = "https://api.thecatapi.com/v1/";
    }

    @Test
    public void deve_cadastrar() {
        // GIVEN -> DADO QUE / WHEN -> QUANDO ESTIVER COM / THEN -> ENTÃO
        Response response = given()
                .contentType(ContentType.JSON)
                .body(corpoCadastro)
            .when()
                .post(pathCadastro);

        validacao(response);
    }

    @Test
    public void deve_votar() {
        // GIVEN -> DADO QUE / WHEN -> QUANDO ESTIVER COM / THEN -> ENTÃO
        Response response = given()
                .contentType(ContentType.JSON)
                .body(corpoVotacao)
            .when()
                .post("votes/");

        id = response.jsonPath().getString("id");

        validacao(response);
    }

    @Test
    public void deve_deletar_voto() {
        deve_votar();
//        deve_deletar();
    }

    private void deve_deletar() {
        // GIVEN -> DADO QUE / WHEN -> QUANDO ESTIVER COM / THEN -> ENTÃO
        Response response = given()
                .contentType(ContentType.JSON)
                .header(new Header("x-api-key", "d56b216d-3a9a-4d4c-821e-8dabeb5ee0ca"))
                .pathParam("vote_id", id)
            .when()
                .delete("votes/{vote_id}");

        validacao(response);
    }

    @Test
    public void deve_favoritar_desfavoritar_imagem() {
        deve_favoritar();
        deve_desfavoritar();
    }

    private void deve_favoritar() {
        // GIVEN -> DADO QUE / WHEN -> QUANDO ESTIVER COM / THEN -> ENTÃO
        Response response = given()
                .contentType(ContentType.JSON)
                .header(new Header("x-api-key", "d56b216d-3a9a-4d4c-821e-8dabeb5ee0ca"))
                .body(corpoFavorita)
            .when()
                .post("favourites");

        id = response.jsonPath().getString("id");

        validacao(response);
    }

    private void deve_desfavoritar() {
        // GIVEN -> DADO QUE / WHEN -> QUANDO ESTIVER COM / THEN -> ENTÃO
        Response response = given()
                .contentType(ContentType.JSON)
                .header(new Header("x-api-key", "d56b216d-3a9a-4d4c-821e-8dabeb5ee0ca"))
                .pathParam("favourite_id", id)
            .when()
                .delete(corpoDesfavorita);

        validacao(response);
    }

    private void validacao(Response response) {
        response.then().statusCode(200).body("message", is(equalTo("SUCCESS")));

        System.out.println(">>> Retorno da API : " + response.body().asString());
        System.out.println("ID : " + id);
        System.out.println("-----------------------------------------------------------------------");
    }

}
