package com.fiap.tc.logistica.service;

import com.fiap.tc.logistica.dto.request.entregador.AtualizarEntregadorRequest;
import com.fiap.tc.logistica.dto.request.entregador.CadastrarEntregadorRequest;
import com.fiap.tc.logistica.model.Entregador;

import java.util.List;

public interface EntregadorService {

    List<Entregador> listarEntregadoresDisponiveis();
    Entregador buscarEntregadorPorId(Long id);
    Entregador cadastraEntregador(CadastrarEntregadorRequest entregador);
    Entregador atualizarStatusEntregador(Long id, AtualizarEntregadorRequest entregador);
    boolean removerEntregador(Long id);

}
