package com.fiap.tc.logistica.service.rota;

import com.fiap.tc.logistica.exception.RotaNotFoundException;
import com.fiap.tc.logistica.helper.RotaHelper;
import com.fiap.tc.logistica.model.rota.RotaResponse;
import com.fiap.tc.logistica.service.feign.HereAPIFeignClient;
import com.fiap.tc.logistica.service.impl.RotaServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RotaServiceTest {

    @Mock
    private HereAPIFeignClient client;

    @InjectMocks
    private RotaServiceImpl rotaService;

    @Test
    void devePermitirCalcularRota() {
        var localizacaoOrigem = RotaHelper.gerarLocalizacaoRotaOrigem();
        var localizacaoDestino = RotaHelper.gerarLocalizacaoRotaDestino();
        var rotaResponse = RotaHelper.gerarRotaResponse();
        when(client.consultarRota(anyString(), anyString(), any(), any(), any()))
                .thenReturn(rotaResponse);

        var result = rotaService.calcularRota(localizacaoOrigem, localizacaoDestino);

        assertThat(result)
                .isNotNull()
                .isInstanceOf(RotaResponse.class);
        verify(client, times(1)).consultarRota(anyString(), anyString(), any(), any(), any());
    }

    @Test
    void deveGerarExcecao_QuandoCalcularRota_RotaNaoEncontrada() {
        var localizacaoOrigem = RotaHelper.gerarLocalizacaoRotaOrigemInexistente();
        var localizacaoDestino = RotaHelper.gerarLocalizacaoRotaDestinoInexistente();
        var rotaResponse = RotaHelper.gerarRotaResponseVazia();
        var message = "Não foi possível calcular rota, revise as coordenadas.";
        when(client.consultarRota(anyString(), anyString(), any(), any(), any()))
                .thenReturn(rotaResponse);

        assertThatThrownBy(() -> rotaService.calcularRota(localizacaoOrigem, localizacaoDestino))
                .isInstanceOf(RotaNotFoundException.class)
                .hasMessage(message);
        verify(client, times(1)).consultarRota(anyString(), anyString(), any(), any(), any());
    }
}
