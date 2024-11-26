package com.fiap.tc.logistica.dto.mapper;


import com.fiap.tc.logistica.dto.request.entregador.CadastrarEntregadorRequest;
import com.fiap.tc.logistica.model.Entregador;

public class EntregadorMapper {

    public static Entregador toEntregador(CadastrarEntregadorRequest request) {
        Entregador entregador = new Entregador();
        entregador.setCpf(request.cpf());
        entregador.setNome(request.nome());
        entregador.setEmail(request.email());
        entregador.setTelefone(request.telefone());

        return entregador;
    }
}
