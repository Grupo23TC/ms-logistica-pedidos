package com.fiap.tc.logistica.dto.request.entregador;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.br.CPF;

public record CadastrarEntregadorRequest(
    @NotBlank(message = "O campo nome é obrigatório")
    String nome,
    @CPF(message = "O CPF deve ser válido")
    String cpf,
    @Email(message = "O e-mail deve ser válido")
    String email,
    @Pattern(regexp = "^\\(\\d{2}\\) \\d{5}-\\d{4}$", message = "O telefone deve estar no formato (99) 99999-9999")
    String telefone
){
}
