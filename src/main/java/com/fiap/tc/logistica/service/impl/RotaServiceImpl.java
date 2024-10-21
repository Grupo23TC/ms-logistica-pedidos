package com.fiap.tc.logistica.service.impl;

import com.fiap.tc.logistica.model.rota.Localizacao;
import com.fiap.tc.logistica.model.rota.RotaResponse;
import com.fiap.tc.logistica.service.RotaService;
import com.fiap.tc.logistica.service.feign.HereAPIFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
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
    public RotaResponse calcularRota(Localizacao origem, Localizacao destino) {

        return hereAPIFeignClient.consultarRota(origem.toString(),
                destino.toString(),
                returns,
                transportMode,
                apiKey);
    }
}
