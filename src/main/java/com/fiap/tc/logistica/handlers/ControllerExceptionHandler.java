package com.fiap.tc.logistica.handlers;

import com.fiap.tc.logistica.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ControllerExceptionHandler {
  @ExceptionHandler(PedidoNotFoundException.class)
  public ResponseEntity<ErroCustomizado> handlePedidoNotFoundException(
      PedidoNotFoundException ex,
      HttpServletRequest request
  ) {
    HttpStatus status  = HttpStatus.NOT_FOUND;
    ErroCustomizado erroCustomizado = new ErroCustomizado(
        ex.getMessage(),
        Instant.now(),
        request.getRequestURI(),
        status.value()
    );

    return ResponseEntity.status(status).body(erroCustomizado);
  }

  @ExceptionHandler(EntregadorNotFoundException.class)
  public ResponseEntity<ErroCustomizado> handleEntregadorNotFoundException(
          EntregadorNotFoundException ex,
      HttpServletRequest request
  ) {
    HttpStatus status  = HttpStatus.NOT_FOUND;
    ErroCustomizado erroCustomizado = new ErroCustomizado(
        ex.getMessage(),
        Instant.now(),
        request.getRequestURI(),
        status.value()
    );

    return ResponseEntity.status(status).body(erroCustomizado);
  }

  @ExceptionHandler(EntregaNotFoundException.class)
  public ResponseEntity<ErroCustomizado> handleEntregaNotFoundException(
          EntregaNotFoundException ex,
          HttpServletRequest request
  ) {
    HttpStatus status  = HttpStatus.NOT_FOUND;
    ErroCustomizado erroCustomizado = new ErroCustomizado(
            ex.getMessage(),
            Instant.now(),
            request.getRequestURI(),
            status.value()
    );

    return ResponseEntity.status(status).body(erroCustomizado);
  }

  @ExceptionHandler(RotaNotFoundException.class)
  public ResponseEntity<ErroCustomizado> handleRotaNotFoundException(
          RotaNotFoundException ex,
      HttpServletRequest request
  ) {
    HttpStatus status  = HttpStatus.NOT_FOUND;
    ErroCustomizado erro = new ErroCustomizado(
        ex.getMessage(),
        Instant.now(),
        request.getRequestURI(),
        status.value()
    );

    return ResponseEntity.status(status).body(erro);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErroCustomizado> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex,
      HttpServletRequest request
  ) {
    HttpStatus status  = HttpStatus.UNPROCESSABLE_ENTITY;
    ValidacaoErro erro = new ValidacaoErro(
        "Dados inválidos",
        Instant.now(),
        request.getRequestURI(),
        status.value()
    );

    for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
      erro.addCampoErro(fieldError.getField(), fieldError.getDefaultMessage());
    }

    return ResponseEntity.status(status).body(erro);
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<ErroCustomizado> handleIllegalStateException(
          IllegalStateException ex,
          HttpServletRequest request
  ) {
    HttpStatus status  = HttpStatus.UNPROCESSABLE_ENTITY;
    ValidacaoErro erro = new ValidacaoErro(
            ex.getMessage(),
            Instant.now(),
            request.getRequestURI(),
            status.value()
    );

    return ResponseEntity.status(status).body(erro);
  }
}
