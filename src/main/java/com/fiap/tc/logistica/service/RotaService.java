package com.fiap.tc.logistica.service;

import com.fiap.tc.logistica.model.rota.LocalizacaoRota;
import com.fiap.tc.logistica.model.rota.RotaResponse;

public interface RotaService {

    RotaResponse calcularRota(LocalizacaoRota origem, LocalizacaoRota destino);

}
