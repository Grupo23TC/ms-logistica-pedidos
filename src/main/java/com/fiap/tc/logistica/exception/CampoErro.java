package com.fiap.tc.logistica.exception;

public record CampoErro(
    String campo,
    String mensagem
) {
}
