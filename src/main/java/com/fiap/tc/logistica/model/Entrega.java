package com.fiap.tc.logistica.model;

import com.fiap.tc.logistica.model.enums.StatusEntregaEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_entrega")
public class Entrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long entregaId;

    private Long pedidoId;

    @ManyToOne
    @JoinColumn(name = "entregador_id")
    private Entregador entregador;

    @Enumerated(EnumType.STRING)
    private StatusEntregaEnum status;

    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime inicioEstimado;

    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime fimEstimado;

}
