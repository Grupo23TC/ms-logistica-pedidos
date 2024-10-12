package com.fiap.tc.logistica.model.rota;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sessao {

    private String id;
    private String type;
    private List<Acao> actions;
    private SaidaChegada departure;
    private SaidaChegada arrival;
    private String polyline;
    private String language;
    private Transporte transport;

}
