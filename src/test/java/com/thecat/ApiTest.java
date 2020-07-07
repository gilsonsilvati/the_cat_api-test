package com.thecat;

import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class ApiTest {

    private static String URL_BASE = "https://api.thecatapi.com/v1/";

    private String path;
    private String vote_id;

    @Test
    public void deve_cadastrar() {
        path = new StringBuilder(URL_BASE).append("user/passwordlesssignup").toString();
        String corpo = "{\"email\" : \"leka.silva@gmail.com\", \"appDescription\" : \"Teste The Cat API -> Gilson Silva\"}";

        // GIVEN -> DADO QUE / WHEN -> QUANDO ESTIVER COM / THEN -> ENTÃO
        Response response = given()
                .contentType(ContentType.JSON)
                .body(corpo)
            .when()
                .post(path);

        response
            .then()
                .statusCode(200);

        assertThat(response.getStatusCode(), is(equalTo(200)));
    }

    @Test
    public void deve_votar() {
        path = new StringBuilder(URL_BASE).append("votes/").toString();

        // GIVEN -> DADO QUE / WHEN -> QUANDO ESTIVER COM / THEN -> ENTÃO
        Response response = given()
                .contentType(ContentType.JSON)
                .body("{\"image_id\" : \"MjA0NzcwNA\", \"value\" : true, \"sub_id\" : \"demo-b6f1ef\"}")
            .when()
                .post(path);

        response
            .then()
                .statusCode(200);

        System.out.println(">>> Response : " + response.body().asString());
        vote_id = response.jsonPath().getString("id");
        System.out.println(">>> ID : " + vote_id);
    }

    @Test
    public void deve_deletar_voto() {
        deve_votar();
        deve_deletar();
    }

    private void deve_deletar() {
        path = new StringBuilder(URL_BASE).append("votes/{vote_id}").toString();

        // GIVEN -> DADO QUE / WHEN -> QUANDO ESTIVER COM / THEN -> ENTÃO
        Response response = given()
                .contentType(ContentType.JSON)
                .header(new Header("x-api-key", "d56b216d-3a9a-4d4c-821e-8dabeb5ee0ca"))
                .pathParam("vote_id", vote_id)
            .when()
                .delete(path);

        System.out.println(">>> Response : " + response.body().asString());

        response
            .then()
                .statusCode(200)
                .body("message", equalTo("SUCCESS"));
    }

    @Test
    public void deve_favoritar_desfavoritar_imagem() {
        deve_favoritar();
        deve_desfavoritar();
    }

    private void deve_favoritar() {
        path = new StringBuilder(URL_BASE).append("favourites").toString();

        // GIVEN -> DADO QUE / WHEN -> QUANDO ESTIVER COM / THEN -> ENTÃO
        Response response = given()
                .contentType(ContentType.JSON)
                .header(new Header("x-api-key", "d56b216d-3a9a-4d4c-821e-8dabeb5ee0ca"))
                .body("{\"image_id\" : \"2rl\"}")
            .when()
                .post(path);

        System.out.println(">>> Response favorita : " + response.body().asString());
        vote_id = response.jsonPath().getString("id");

        response
            .then()
                .statusCode(200)
                .body("message", equalTo("SUCCESS"));
    }

    private void deve_desfavoritar() {
        path = new StringBuilder(URL_BASE).append("favourites").append("/{favourite_id}").toString();

        // GIVEN -> DADO QUE / WHEN -> QUANDO ESTIVER COM / THEN -> ENTÃO
        Response response = given()
                .contentType(ContentType.JSON)
                .header(new Header("x-api-key", "d56b216d-3a9a-4d4c-821e-8dabeb5ee0ca"))
                .pathParam("favourite_id", vote_id)
            .when()
                .delete(path);

        System.out.println(">>> Response desfavorita : " + response.body().asString());
        System.out.println("ID : " + vote_id);

        response
            .then()
                .statusCode(200)
                .body("message", equalTo("SUCCESS"));
    }

}
