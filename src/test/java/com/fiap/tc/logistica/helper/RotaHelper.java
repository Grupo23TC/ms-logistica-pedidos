package com.fiap.tc.logistica.helper;

import com.fiap.tc.logistica.model.rota.LocalizacaoRota;
import com.fiap.tc.logistica.model.rota.Rota;
import com.fiap.tc.logistica.model.rota.RotaResponse;

import java.util.Arrays;
import java.util.List;

public class RotaHelper {

    public static LocalizacaoRota gerarLocalizacaoRotaOrigem() {
        return new LocalizacaoRota(-23.4984522, -47.4309735);
    }

    public static LocalizacaoRota gerarLocalizacaoRotaDestino() {
        return new LocalizacaoRota(-23.4923629, -47.4292845);
    }

    public static LocalizacaoRota gerarLocalizacaoRotaOrigemInexistente() {
        return new LocalizacaoRota(-1000000.0, -100000000.0);
    }

    public static LocalizacaoRota gerarLocalizacaoRotaDestinoInexistente() {
        return new LocalizacaoRota(-11111110.0, -11110.0);
    }

    public static RotaResponse gerarRotaResponse() {
        return new RotaResponse(gerarListaRotas());
    }

    public static RotaResponse gerarRotaResponseVazia() {
        return new RotaResponse();
    }

    private static List<Rota> gerarListaRotas() {
        return Arrays.asList(new Rota(), new Rota());
    }
}
