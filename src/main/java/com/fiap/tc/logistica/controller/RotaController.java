package com.fiap.tc.logistica.controller;

import com.fiap.tc.logistica.model.rota.LocalizacaoRota;
import com.fiap.tc.logistica.service.RotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rotas")
public class RotaController {

    @Autowired
    private RotaService rotaService;

    @GetMapping
    public ResponseEntity<?> getRota(@RequestParam double latOrig, @RequestParam double lngOrig,
                                     @RequestParam double latDest, @RequestParam double lngDest) {
        LocalizacaoRota origem = new LocalizacaoRota(latOrig, lngOrig);
        LocalizacaoRota destino = new LocalizacaoRota(latDest, lngDest);

        return ResponseEntity.ok(rotaService.calcularRota(origem, destino));
    }
}
