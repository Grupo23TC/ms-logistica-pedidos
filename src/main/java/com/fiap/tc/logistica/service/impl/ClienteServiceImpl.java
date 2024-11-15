package com.fiap.tc.logistica.service.impl;

import com.fiap.tc.logistica.exception.ClienteNotFoundException;
import com.fiap.tc.logistica.model.Cliente;
import com.fiap.tc.logistica.service.ClienteService;
import com.fiap.tc.logistica.service.feign.ClienteFeignClient;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteFeignClient clienteFeignClient;

    @Override
    public Cliente buscarClientePorId(Long clientId) {
        try {
            return clienteFeignClient.getCliente(clientId);
        } catch (FeignException e) {
            if (e.status() == 404) {
                throw new ClienteNotFoundException("Cliente de id: " + clientId + " não encontrado.");
            } else {
                throw new RuntimeException(e.getMessage());
            }
        }
    }
}
