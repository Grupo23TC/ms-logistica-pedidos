package com.fiap.tc.logistica.service;

import com.fiap.tc.logistica.dto.request.AtualizarEntregadorRequest;
import com.fiap.tc.logistica.dto.request.CadastrarEntregadorRequest;
import com.fiap.tc.logistica.model.Entregador;

import java.util.List;

public interface EntregadorService {

    List<Entregador> listarEntregadoresDisponiveis();
    Entregador buscarEntregadorPorId(Long id);
    Entregador cadastraEntregador(CadastrarEntregadorRequest entregador);
    Entregador atualizarEntregador(Long id, AtualizarEntregadorRequest entregador);
    boolean removerEntregador(Long id);

}
