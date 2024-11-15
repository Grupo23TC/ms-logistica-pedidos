package com.fiap.tc.logistica.exception;

import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidacaoErro extends ErroCustomizado {
  private final List<CampoErro> erros = new ArrayList<>();

  public ValidacaoErro(String erro, Instant horario, String rota, Integer status) {
    super(erro, horario, rota, status);
  }

  public void addCampoErro(String campo, String messagem) {
    erros.removeIf(erro -> erro.campo().equals(campo));
    erros.add(new CampoErro(campo, messagem));
  }
}
