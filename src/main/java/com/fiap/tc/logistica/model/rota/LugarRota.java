package com.fiap.tc.logistica.model.rota;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LugarRota {

    private String type;
    private LocalizacaoRota location;
    private LocalizacaoRota originalLocation;
}
