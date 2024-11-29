package com.fiap.tc.logistica.service.impl;

import com.fiap.tc.logistica.dto.mapper.EntregadorMapper;
import com.fiap.tc.logistica.dto.request.entregador.AtualizarEntregadorRequest;
import com.fiap.tc.logistica.dto.request.entregador.CadastrarEntregadorRequest;
import com.fiap.tc.logistica.exception.EntregadorNotFoundException;
import com.fiap.tc.logistica.model.Entregador;
import com.fiap.tc.logistica.repository.EntregadorRepository;
import com.fiap.tc.logistica.service.EntregadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@SuppressWarnings("squid:S6813")
public class EntregadorServiceImpl implements EntregadorService {

    @Autowired
    private EntregadorRepository entregadorRepository;

    @Override
    public List<Entregador> listarEntregadoresDisponiveis() {
        return entregadorRepository.buscarEntregadoresDisponiveis();
    }

    @Override
    public Entregador buscarEntregadorPorId(Long id) {
        return entregadorRepository.findById(id)
                .orElseThrow(() -> new EntregadorNotFoundException("Entregador de id: " + id + " não encontrado."));
    }

    @Override
    public Entregador cadastraEntregador(CadastrarEntregadorRequest request) {
        Entregador entregador = EntregadorMapper.toEntregador(request);
        entregador.setEstaDisponivel(true);
        return entregadorRepository.save(entregador);
    }

    @Override
    public Entregador atualizarStatusEntregador(Long id, AtualizarEntregadorRequest request) {
        Entregador entregador = buscarEntregadorPorId(id);

        entregador.setEstaDisponivel(request.estaDisponivel());

        return entregadorRepository.save(entregador);
    }

    @Override
    public boolean removerEntregador(Long id) {
        buscarEntregadorPorId(id);
        try {
            entregadorRepository.deleteById(id);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
