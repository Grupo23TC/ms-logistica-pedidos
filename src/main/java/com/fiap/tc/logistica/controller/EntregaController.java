package com.fiap.tc.logistica.controller;

import com.fiap.tc.logistica.dto.request.CalcularEntregaRequest;
import com.fiap.tc.logistica.model.Entrega;
import com.fiap.tc.logistica.model.rota.RotaResponse;
import com.fiap.tc.logistica.service.EntregaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/entregas")
public class EntregaController {

    @Autowired
    private EntregaService entregaService;

    @PostMapping("/calcularECriar")
    public ResponseEntity<RotaResponse> calcularECriarEntrega(@RequestBody CalcularEntregaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(entregaService.calcularECriarEntrega(request));
    }

    @PutMapping("/solicitar/{id}")
    public ResponseEntity<Entrega> solicitarEntrega(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(entregaService.solicitarEntrega(id));
    }

    @PutMapping("/atribuirEntregador/{entregaId}/{entregadorId}")
    public ResponseEntity<Entrega> atribuirEntregador(@PathVariable Long entregaId, @PathVariable Long entregadorId) {
        return ResponseEntity.status(HttpStatus.OK).body(entregaService.atribuirEntregadorAEntrega(entregaId, entregadorId));
    }

    @PutMapping("/finalizar/{id}")
    public ResponseEntity<Entrega> finalizarEntrega(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(entregaService.finalizarEntrega(id));
    }

    @PutMapping("/cancelar/{id}")
    public ResponseEntity<Entrega> cancelarEntrega(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(entregaService.cancelarEntrega(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Entrega> getEntrega(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(entregaService.buscarEntregaPorId(id));
    }
}
