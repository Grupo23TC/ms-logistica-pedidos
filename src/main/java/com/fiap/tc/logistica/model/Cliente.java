package com.fiap.tc.logistica.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {

    private Long clienteId;
    private String nome;
    private String cpf;

}
