package com.fiap.tc.logistica.model.rota;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaidaChegadaRota {


    private ZonedDateTime time;
    private LugarRota place;

}
