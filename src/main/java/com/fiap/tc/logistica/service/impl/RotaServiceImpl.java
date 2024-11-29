package com.fiap.tc.logistica.service.impl;

import com.fiap.tc.logistica.exception.RotaNotFoundException;
import com.fiap.tc.logistica.model.rota.LocalizacaoRota;
import com.fiap.tc.logistica.model.rota.RotaResponse;
import com.fiap.tc.logistica.service.RotaService;
import com.fiap.tc.logistica.service.feign.HereAPIFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("squid:S6813")
public class RotaServiceImpl implements RotaService {

    @Autowired
    private HereAPIFeignClient hereAPIFeignClient;

    @Value("${application.client.here.api.key}")
    private String apiKey;

    @Value("${application.client.here.api.return}")
    private String returns;

    @Value("${application.client.here.api.transportMode}")
    private String transportMode;


    @Override
    public RotaResponse calcularRota(LocalizacaoRota origem, LocalizacaoRota destino) {

        RotaResponse rotaResponse = hereAPIFeignClient.consultarRota(origem.toString(),
                destino.toString(),
                returns,
                transportMode,
                apiKey);

        if (rotaResponse == null || rotaResponse.getRoutes() == null || rotaResponse.getRoutes().isEmpty()) {
            throw new RotaNotFoundException("Não foi possível calcular rota, revise as coordenadas.");
        }

        return rotaResponse;
    }
}
