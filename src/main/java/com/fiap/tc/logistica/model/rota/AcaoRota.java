package com.fiap.tc.logistica.model.rota;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcaoRota {

    private String action;
    private Integer duration;
    private Integer length;
    private String instruction;
    private Integer offset;
    private String direction;
    private String severity;
}
