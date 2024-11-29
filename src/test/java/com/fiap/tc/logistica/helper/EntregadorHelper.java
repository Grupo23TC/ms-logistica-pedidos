package com.fiap.tc.logistica.helper;

import com.fiap.tc.logistica.dto.request.entregador.AtualizarEntregadorRequest;
import com.fiap.tc.logistica.dto.request.entregador.CadastrarEntregadorRequest;
import com.fiap.tc.logistica.model.Entregador;

public class EntregadorHelper {

    public static Entregador gerarEntregador() {
        Entregador entregador = new Entregador();
        entregador.setNome("Entregador Teste");
        entregador.setEmail("entregador@gmail.com");
        entregador.setCpf("570.706.520-20");
        entregador.setTelefone("(11) 99999-9999");
        entregador.setEstaDisponivel(true);

        return entregador;
    }

    public static AtualizarEntregadorRequest gerarAtualizarEntregadorRequest() {
        return new AtualizarEntregadorRequest(true);
    }

    public static CadastrarEntregadorRequest gerarCadastrarEntregadorRequest() {
        return new CadastrarEntregadorRequest("Entregador",
                "570.706.520-20",
                "entregador@gmail.com",
                "(11) 99999-9999");
    }
}
