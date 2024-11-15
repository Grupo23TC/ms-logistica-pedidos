package com.fiap.tc.logistica.model.rota;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessaoRota {

    private String id;
    private String type;
    private List<AcaoRota> actions;
    private SaidaChegadaRota departure;
    private SaidaChegadaRota arrival;
    private String polyline;
    private String language;
    private TransporteRota transport;

}
