package com.fiap.tc.logistica.helper;

import com.fiap.tc.logistica.model.rota.*;

import java.time.ZonedDateTime;
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

    public static RotaResponse gerarRotaResponseComplete() {
        Rota rota = new Rota();
        rota.setId("73d932a4-db07-4d48-97fe-ac0f34a7873f");

        // Define departure and arrival
        SessaoRota section = new SessaoRota();
        section.setId("7c898197-12c6-4940-b28b-9bf1068dd29a");
        section.setType("vehicle");

        // Add actions (steps for the route)
        section.setActions(Arrays.asList(
                new AcaoRota("depart", 25, 117, "Head toward Rua Doutor Gabriel Rezende Passos on Rua Pedro Sunica Neto. Go for 117 m.", 0),
                new AcaoRota("turn", 98, 611, "Turn left onto Rua Sizina Azevedo Schrepel. Go for 611 m.", 3, "left", "quite"),
                new AcaoRota("turn", 5, 20, "Turn right toward Rua Belmira Loureiro de Almeida. Go for 20 m.", 10, "right", "quite"),
                new AcaoRota("turn", 25, 121, "Turn right onto Rua Belmira Loureiro de Almeida. Go for 121 m.", 11, "right", "quite"),
                new AcaoRota("turn", 26, 199, "Turn left onto Rua Santos Severo Scapol. Go for 199 m.", 14, "left", "quite"),
                new AcaoRota("arrive", 0, 0, "Arrive at Rua Santos Severo Scapol. Your destination is on the left.", 18)
        ));

        // Set departure and arrival places
        section.setDeparture(createPlace(-23.49836, -47.4309, -23.4984522, -47.4309735, "2024-11-28T10:21:03-03:00"));
        section.setArrival(createPlace(-23.4925866, -47.4291928, -23.4923629, -47.4292846, "2024-11-28T10:24:02-03:00"));

        section.setPolyline("BGvnn6sBnv-u6CsJ4XgFwRgK0Z46B3X46B7V0wC3hB4zC_iBgFjDsYzK0KnGwHsJrYwlB7L8LjI8G0Z0tBgKkS4DsJiMujB");
        section.setLanguage("en-us");

        // Set transport mode
        TransporteRota transport = new TransporteRota();
        transport.setMode("car");
        section.setTransport(transport);

        // Add section to the route
        rota.setSections(Arrays.asList(section));

        return new RotaResponse(Arrays.asList(rota));
    }

    private static SaidaChegadaRota createPlace(double lat, double lng, double origLat, double origLng, String time) {
        SaidaChegadaRota saidaChegadaRota = new SaidaChegadaRota();
        LugarRota place = new LugarRota();
        place.setType("place");
        place.setLocation(new LocalizacaoRota(lat, lng));
        place.setOriginalLocation(new LocalizacaoRota(origLat, origLng));
        saidaChegadaRota.setPlace(place);
        saidaChegadaRota.setTime(ZonedDateTime.parse(time));
        return saidaChegadaRota;
    }
}
