package com.fiap.tc.logistica.model.rota;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rota {

    private String id;
    private List<Sessao> sections;
}
