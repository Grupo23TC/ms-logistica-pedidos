package com.fiap.tc.logistica.service.feign;

import com.fiap.tc.logistica.config.feign.FeignConfiguration;
import com.fiap.tc.logistica.model.Cliente;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "clientes", primary = false, contextId = "clienteClient", url = "${application.client.clientes.api.host}",
        configuration = FeignConfiguration.class)
public interface ClienteFeignClient {

    @GetMapping("/{id}")
    Cliente getCliente(@PathVariable("id") Long id);

}
