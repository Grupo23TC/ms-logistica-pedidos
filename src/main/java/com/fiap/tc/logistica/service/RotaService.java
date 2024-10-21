package com.fiap.tc.logistica.service;

import com.fiap.tc.logistica.model.rota.Localizacao;
import com.fiap.tc.logistica.model.rota.RotaResponse;

public interface RotaService {

    RotaResponse calcularRota(Localizacao origem, Localizacao destino);

}
