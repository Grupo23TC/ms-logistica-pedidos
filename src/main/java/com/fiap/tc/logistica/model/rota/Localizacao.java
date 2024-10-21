package com.fiap.tc.logistica.model.rota;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Localizacao {

    private Double lat;
    private Double lng;

    @Override
    public String toString() {
        return lat + "," + lng;
    }
}
