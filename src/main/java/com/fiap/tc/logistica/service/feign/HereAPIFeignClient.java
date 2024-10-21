package com.fiap.tc.logistica.service.feign;

import com.fiap.tc.logistica.config.feign.FeignConfiguration;
import com.fiap.tc.logistica.model.rota.RotaResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "routes", primary = false, contextId = "routesClient", url = "${application.client.here.api.host}",
        configuration = FeignConfiguration.class)
public interface HereAPIFeignClient {

    @GetMapping(value = "/v8/routes", consumes = MediaType.APPLICATION_JSON_VALUE)
    RotaResponse consultarRota(@RequestParam String origin,
                               @RequestParam String destination,
                               @RequestParam("return") String returns,
                               @RequestParam String transportMode,
                               @RequestParam String apikey);
}
