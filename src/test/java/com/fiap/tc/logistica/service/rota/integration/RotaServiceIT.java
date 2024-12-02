package com.fiap.tc.logistica.service.rota.integration;

import com.fiap.tc.logistica.exception.RotaNotFoundException;
import com.fiap.tc.logistica.helper.RotaHelper;
import com.fiap.tc.logistica.model.rota.RotaResponse;
import com.fiap.tc.logistica.service.impl.RotaServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class RotaServiceIT {

    @Autowired
    private RotaServiceImpl rotaService;

    @Test
    void devePermitirCalcularRota() {
        var localizacaoOrigem = RotaHelper.gerarLocalizacaoRotaOrigem();
        var localizacaoDestino = RotaHelper.gerarLocalizacaoRotaDestino();


        var result = rotaService.calcularRota(localizacaoOrigem, localizacaoDestino);

        assertThat(result)
                .isNotNull()
                .isInstanceOf(RotaResponse.class);
        assertThat(result.getRoutes())
                .isNotEmpty();
    }

    @Test
    void deveGerarExcecao_QuandoCalcularRota_RotaNaoEncontrada() {
        var localizacaoOrigem = RotaHelper.gerarLocalizacaoRotaOrigemInexistente();
        var localizacaoDestino = RotaHelper.gerarLocalizacaoRotaDestinoInexistente();
        var message = "Não foi possível calcular rota, revise as coordenadas.";

        assertThatThrownBy(() -> rotaService.calcularRota(localizacaoOrigem, localizacaoDestino))
                .isInstanceOf(RotaNotFoundException.class)
                .hasMessage(message);

    }
}
