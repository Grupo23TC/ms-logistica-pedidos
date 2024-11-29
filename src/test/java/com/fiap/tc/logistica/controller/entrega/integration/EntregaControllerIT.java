package com.fiap.tc.logistica.controller.entrega.integration;

import com.fiap.tc.logistica.helper.EntregaHelper;
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
public class EntregaControllerIT {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Nested
    class BuscarEntrega {

        @Test
        void devePermitirBuscarEntregaPorId() {

            var id = 1L;

            given()
                    .when()
                    .get("/entregas/{id}", id)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body(matchesJsonSchemaInClasspath("schemas/entregas/entrega.schema.json"));
        }

        @Test
        void deveGerarExcecao_QuandoBuscarEntregaPorId_IdNaoEncontrado() {
            var id = 10000L;
            given()
                    .when()
                    .get("/entregas/{id}", id)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body(matchesJsonSchemaInClasspath("schemas/exception/erroCustomizado.schema.json"));
        }
    }

    @Nested
    class CalcularRotaECriarEntrega {

        @Test
        void devePermitirCalcularRotaECriarEntrega() {
            var request = EntregaHelper.gerarCalcularEntregaRequest();
            given()
                    .contentType("application/json")
                    .body(request)
                    .when()
                    .post("/entregas/calcularECriar")
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body(matchesJsonSchemaInClasspath("schemas/rotas/rota.schema.json"));
        }

        @Test
        void deveGerarExcecao_QuandoCalcularRotaECriarEntrega_JaExisteEntregaPedido() {
            var request = EntregaHelper.gerarCalcularEntregaRequestEntregaJaExistente();
            given()
                    .contentType("application/json")
                    .body(request)
                    .when()
                    .post("/entregas/calcularECriar")
                    .then()
                    .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                    .body(matchesJsonSchemaInClasspath("schemas/exception/erroCustomizado.schema.json"));
        }

        @Test
        void deveGerarExcecao_QuandoCalcularRotaECriarEntrega_PedidoNaoEncontrado() {
            var request = EntregaHelper.gerarCalcularEntregaRequestPedidoNaoExistente();
            given()
                    .contentType("application/json")
                    .body(request)
                    .when()
                    .post("/entregas/calcularECriar")
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body(matchesJsonSchemaInClasspath("schemas/exception/erroCustomizado.schema.json"));
        }

        @Test
        void deveGerarExcecao_QuandoCalcularRotaECriarEntrega_RotaNaoEncontrada() {
            var request = EntregaHelper.gerarCalcularEntregaRequestRotaNaoExistente();
            given()
                    .contentType("application/json")
                    .body(request)
                    .when()
                    .post("/entregas/calcularECriar")
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body(matchesJsonSchemaInClasspath("schemas/exception/erroCustomizado.schema.json"));
        }
    }

    @Nested
    class SolicitarEntrega {

        @Test
        void devePermitirSolicitarEntrega() {
            var id = 1L;
            given()
                    .when()
                    .put("/entregas/solicitar/{id}", id)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body(matchesJsonSchemaInClasspath("schemas/entregas/entrega.schema.json"));
        }

        @Test
        void deveGerarExcecao_QuandoSolicitarEntrega_IdNaoEncontrado() {
            var id = 100000L;
            given()
                    .when()
                    .put("/entregas/solicitar/{id}", id)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body(matchesJsonSchemaInClasspath("schemas/exception/erroCustomizado.schema.json"));
        }

        @Test
        void deveGerarExcecao_QuandoSolicitarEntrega_StatusNaoPendente() {
            var id = 2L;
            given()
                    .when()
                    .put("/entregas/solicitar/{id}", id)
                    .then()
                    .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                    .body(matchesJsonSchemaInClasspath("schemas/exception/erroCustomizado.schema.json"));
        }

    }

    @Nested
    class AtribuirEntregadorParaEntrega {

        @Test
        void devePermitirAtribuirEntregadorParaEntrega() {
            var id = 2L;
            given()
                    .when()
                    .put("/entregas/atribuirEntregador/{entregaId}/{entregadorId}", id, id)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body(matchesJsonSchemaInClasspath("schemas/entregas/entrega.schema.json"));
        }

        @Test
        void deveGerarExcecao_QuandoAtribuirEntregadorParaEntrega_IdNaoEncontrado() {
            var id = 10000L;
            given()
                    .when()
                    .put("/entregas/atribuirEntregador/{entregaId}/{entregadorId}", id, id)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body(matchesJsonSchemaInClasspath("schemas/exception/erroCustomizado.schema.json"));
        }

        @Test
        void deveGerarExcecao_QuandoAtribuirEntregadorParaEntrega_StatusNaoSolicitada() {
            var id = 5L;
            given()
                    .when()
                    .put("/entregas/atribuirEntregador/{entregaId}/{entregadorId}", id, id)
                    .then()
                    .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                    .body(matchesJsonSchemaInClasspath("schemas/exception/erroCustomizado.schema.json"));
        }

        @Test
        void deveGerarExcecao_QuandoAtribuirEntregadorParaEntrega_EntregadorNaoEncontrado() {
            var id = 6L;
            var entregadorId = 100000l;
            given()
                    .when()
                    .put("/entregas/atribuirEntregador/{entregaId}/{entregadorId}", id, entregadorId)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body(matchesJsonSchemaInClasspath("schemas/exception/erroCustomizado.schema.json"));
        }

        @Test
        void deveGerarExcecao_QuandoAtribuirEntregadorParaEntrega_EntregadorNaoDisponivel() {
            var id = 6L;
            var entregadorId = 3l;
            given()
                    .when()
                    .put("/entregas/atribuirEntregador/{entregaId}/{entregadorId}", id, entregadorId)
                    .then()
                    .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                    .body(matchesJsonSchemaInClasspath("schemas/exception/erroCustomizado.schema.json"));
        }

    }

    @Nested
    class FinalizarEntrega {

        @Test
        void devePermitirFinalizarEntrega() {
            var id = 3L;
            given()
                    .when()
                    .put("/entregas/finalizar/{id}", id)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body(matchesJsonSchemaInClasspath("schemas/entregas/entrega.schema.json"));
        }

        @Test
        void deveGerarExcecao_QuandoFinalizarEntrega_IdNaoEncontrado() {
            var id = 100000l;
            given()
                    .when()
                    .put("/entregas/finalizar/{id}", id)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body(matchesJsonSchemaInClasspath("schemas/exception/erroCustomizado.schema.json"));
        }

        @Test
        void deveGerarExcecao_QuandoFinalizarEntrega_StatusNaoEnviada() {
            var id = 5L;
            given()
                    .when()
                    .put("/entregas/finalizar/{id}", id)
                    .then()
                    .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                    .body(matchesJsonSchemaInClasspath("schemas/exception/erroCustomizado.schema.json"));
        }

    }

    @Nested
    class CancelarEntrega {

        @Test
        void devePermitirCancelarEntrega() {
            var id = 7L;
            given()
                    .when()
                    .put("/entregas/cancelar/{id}", id)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body(matchesJsonSchemaInClasspath("schemas/entregas/entrega.schema.json"));
        }

        @Test
        void deveGerarExcecao_QuandoCancelarEntrega_IdNaoEncontrado() {
            var id = 10000000L;
            given()
                    .when()
                    .put("/entregas/cancelar/{id}", id)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body(matchesJsonSchemaInClasspath("schemas/exception/erroCustomizado.schema.json"));
        }

        @Test
        void deveGerarExcecao_QuandoCancelarEntrega_StatusEntregue() {
            var id = 4L;
            given()
                    .when()
                    .put("/entregas/cancelar/{id}", id)
                    .then()
                    .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                    .body(matchesJsonSchemaInClasspath("schemas/exception/erroCustomizado.schema.json"));
        }
    }
}
