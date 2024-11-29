package com.fiap.tc.logistica.controller.entregador.integration;

import com.fiap.tc.logistica.helper.EntregadorHelper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class EntregadorControllerIT {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Nested
    class CadastrarEntregador {

        @Test
        void devePermitirCadastrarEntregador() {
            var request = EntregadorHelper.gerarCadastrarEntregadorRequest();

            given()
                    .contentType("application/json")
                    .body(request)
            .when()
                    .post("/entregadores")
            .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body(matchesJsonSchemaInClasspath("schemas/entregadores/entregador.schema.json"));
        }
    }

    @Nested
    class BuscarEntregador {

        @Test
        void devePermitirBuscarEntregadorPorId() throws Exception {
            var id = 1L;
            given()

                    .when()
                    .get("/entregadores/{id}", id)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body(matchesJsonSchemaInClasspath("schemas/entregadores/entregador.schema.json"));
        }

        @Test
        void deveGerarExcecao_QuandoBuscarEntregadorPorId_IdNaoEncontrado() {
            var id = 1000000L;
            given()

                    .when()
                    .get("/entregadores/{id}", id)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body(matchesJsonSchemaInClasspath("schemas/exception/erroCustomizado.schema.json"));
        }

        @Test
        void devePermitirListarEntregadoresDisponiveis() throws Exception {
            given()

                    .when()
                    .get("/entregadores/disponiveis")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body(matchesJsonSchemaInClasspath("schemas/entregadores/entregadores-list.schema.json"));
        }
    }

    @Nested
    class AtualizarEntregador {

        @Test
        void devePermitirAtualizarStatusEntregador() throws Exception {
            var id = 1L;
            var request = EntregadorHelper.gerarAtualizarEntregadorRequest();
            given()
                    .contentType("application/json")
                    .body(request)
                    .when()
                    .put("/entregadores/{id}", id)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body(matchesJsonSchemaInClasspath("schemas/entregadores/entregador.schema.json"));
        }

        @Test
        void deveGerarExcecao_QuandoAtualizarEntregador_IdNaoEncontrado () throws Exception {
            var id = 1000000L;
            var request = EntregadorHelper.gerarAtualizarEntregadorRequest();
            given()
                    .contentType("application/json")
                    .body(request)
                    .when()
                    .put("/entregadores/{id}", id)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body(matchesJsonSchemaInClasspath("schemas/exception/erroCustomizado.schema.json"));
        }
    }

    @Nested
    class DeletarEntregador {

        @Test
        void devePermitirDeletarEntregador() throws Exception {
            var id = 1L;

            given()

                    .when()
                    .delete("/entregadores/{id}", id)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body(matchesJsonSchemaInClasspath("schemas/entregadores/entregador-deletado.schema.json"));
        }

        @Test
        void deveGerarExcecao_QuandoRemoverEntregador_IdNaoEncontrado() throws Exception {
            var id = 1000000L;
            given()
                    .when()
                    .delete("/entregadores/{id}", id)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body(matchesJsonSchemaInClasspath("schemas/exception/erroCustomizado.schema.json"));
        }
    }
}
