package com.fiap.tc.logistica.service.impl;

import com.fiap.tc.logistica.model.Cliente;
import com.fiap.tc.logistica.service.ClienteService;
import com.fiap.tc.logistica.service.feign.ClienteFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteFeignClient clienteFeignClient;

    @Override
    public Cliente buscarClientePorId(Long clientId) {
        return clienteFeignClient.getCliente(clientId);
    }
}
