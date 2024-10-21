package com.fiap.tc.logistica.controller;

import com.fiap.tc.logistica.model.rota.Localizacao;
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
        Localizacao origem = new Localizacao(latOrig, lngOrig);
        Localizacao destino = new Localizacao(latDest, lngDest);

        return ResponseEntity.ok(rotaService.calcularRota(origem, destino));
    }
}
