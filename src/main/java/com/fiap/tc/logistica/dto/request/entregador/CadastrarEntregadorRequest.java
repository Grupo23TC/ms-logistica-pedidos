package com.fiap.tc.logistica.dto.request.entregador;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.br.CPF;

public record CadastrarEntregadorRequest(
    @Schema(example = "Alberto", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "O campo nome é obrigatório")
    String nome,
    @Schema(example = "429.652.910-24", requiredMode = Schema.RequiredMode.REQUIRED)
    @CPF(message = "O CPF deve ser válido")
    String cpf,
    @Schema(example = "alberto@gmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @Email(message = "O e-mail deve ser válido")
    String email,
    @Schema(example = "(99) 99999-9999", requiredMode = Schema.RequiredMode.REQUIRED)
    @Pattern(regexp = "^\\(\\d{2}\\) \\d{5}-\\d{4}$", message = "O telefone deve estar no formato (99) 99999-9999")
    String telefone
){
}
