package com.fiap.tc.logistica.controller;

import com.fiap.tc.logistica.dto.request.entregador.AtualizarEntregadorRequest;
import com.fiap.tc.logistica.dto.request.entregador.CadastrarEntregadorRequest;
import com.fiap.tc.logistica.model.Entregador;
import com.fiap.tc.logistica.service.EntregadorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/entregadores")
@SuppressWarnings("squid:S6813")
public class EntregadorController {

    @Autowired
    private EntregadorService entregadorService;

    @GetMapping("/disponiveis")
    public ResponseEntity<List<Entregador>> disponiveis() {
        return ResponseEntity.status(HttpStatus.OK).body(entregadorService.listarEntregadoresDisponiveis());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Entregador> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(entregadorService.buscarEntregadorPorId(id));
    }

    @PostMapping
    public ResponseEntity<Entregador> cadastrar(@RequestBody @Valid CadastrarEntregadorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(entregadorService.cadastraEntregador(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Entregador> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizarEntregadorRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(entregadorService.atualizarStatusEntregador(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> remover(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(entregadorService.removerEntregador(id));
    }
}
