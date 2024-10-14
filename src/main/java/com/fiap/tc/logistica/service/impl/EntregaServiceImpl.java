package com.fiap.tc.logistica.service.impl;

import com.fiap.tc.logistica.dto.request.AtualizarEntregadorRequest;
import com.fiap.tc.logistica.dto.request.AtualizarStatusPedidoRequest;
import com.fiap.tc.logistica.dto.request.CalcularEntregaRequest;
import com.fiap.tc.logistica.exception.ClienteNotFoundException;
import com.fiap.tc.logistica.exception.EntregaNotFoundException;
import com.fiap.tc.logistica.exception.PedidoNotFoundException;
import com.fiap.tc.logistica.exception.RotaNotFoundException;
import com.fiap.tc.logistica.model.Cliente;
import com.fiap.tc.logistica.model.Entrega;
import com.fiap.tc.logistica.model.Entregador;
import com.fiap.tc.logistica.model.Pedido;
import com.fiap.tc.logistica.model.enums.StatusEntregaEnum;
import com.fiap.tc.logistica.model.enums.StatusPedidoEnum;
import com.fiap.tc.logistica.model.rota.Localizacao;
import com.fiap.tc.logistica.model.rota.RotaResponse;
import com.fiap.tc.logistica.repository.EntregaRepository;
import com.fiap.tc.logistica.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class EntregaServiceImpl implements EntregaService {

    @Autowired
    private EntregaRepository entregaRepository;

    @Autowired
    private RotaService rotaService;

    @Autowired
    private EntregadorService entregadorService;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private ClienteService clienteService;

    @Override
    public RotaResponse calcularEntrega(CalcularEntregaRequest request) {

        List<Entrega> entregaPorPedido = entregaRepository.findByPedidoId(request.pedidoId());

        if (!entregaPorPedido.isEmpty()) {
            throw new IllegalStateException("Já existe uma entrega com esse pedido");
        }

//        Cliente cliente = clienteService.buscarClientePorId(request.clientId());
//        if (cliente == null) {
//            throw new ClienteNotFoundException("Cliente de id: " + request.clientId() + " não encontrado.");
//        }

        Pedido pedido = pedidoService.buscarPedidoPorId(request.pedidoId());
        if (pedido == null) {
            throw new PedidoNotFoundException("Pedido de id: " + request.pedidoId() + " não encontrado.");
        }

//        if (!cliente.getClienteId().equals(pedido.getUsuarioId())) {
//            throw new IllegalArgumentException("O Cliente solicitando a entrega não é o cliente que solicitou o pedido.");
//        }

        Localizacao origem = new Localizacao(request.latOrig(), request.lngOrig());
        Localizacao destino = new Localizacao(request.latDest(), request.lngDest());

        RotaResponse rotaResponse = rotaService.calcularRota(origem, destino);

        if (rotaResponse == null || rotaResponse.getRoutes() == null || rotaResponse.getRoutes().isEmpty()) {
            throw new RotaNotFoundException("Não foi possível calcular rota, revise as coordenadas.");
        }

        ZonedDateTime inicioEstimado = rotaResponse.getRoutes().getFirst().getSections().getFirst().getDeparture().getTime();
        ZonedDateTime fimEstimado = rotaResponse.getRoutes().getFirst().getSections().getFirst().getArrival().getTime();

        Entrega entrega = new Entrega();
        entrega.setStatus(StatusEntregaEnum.PENDENTE);
        entrega.setPedidoId(request.pedidoId());
        entrega.setInicioEstimado(inicioEstimado);
        entrega.setFimEstimado(fimEstimado);

        entregaRepository.save(entrega);

        return rotaResponse;
    }

    @Override
    public Entrega solicitarEntrega(Long entregaId) {

        Entrega entrega = buscarEntregaPorId(entregaId);

        if (!entrega.getStatus().equals(StatusEntregaEnum.PENDENTE)) {
            throw new IllegalStateException("A entrega precisa estar com status PENDENTE para ser solicitada.");
        }

        entrega.setStatus(StatusEntregaEnum.SOLICITADA);

        //TODO Postar mensagem no Kafka

        return entregaRepository.save(entrega);
    }

    @Override
    public void notificarEntregadores() {
        //TODO Consumir mensagem no Kafka
        List<Entregador> entregadoresDisponiveis = entregadorService.listarEntregadoresDisponiveis();
        entregadoresDisponiveis.forEach(entregador -> {
            System.out.println("Olá " + entregador.getNome() +
                    " a entrega id: " + 1L + " foi solicitada, por favor solicitar a atribuição para realiza-la.");
        });
    }

    @Override
    public Entrega atribuirEntregadorAEntrega(Long entregaId, Long entregadorId) {

        Entrega entrega = buscarEntregaPorId(entregaId);

        if (!entrega.getStatus().equals(StatusEntregaEnum.SOLICITADA)) {
            throw new IllegalStateException("A entrega precisa estar com status SOLICITADA para ser atribuída.");
        }

        Entregador entregador = entregadorService.buscarEntregadorPorId(entregadorId);

        if (!entregador.getEstaDisponivel()) {
            throw new IllegalStateException("O Entregador: " + entregador.getNome() + " não está disponivel para realizar a entrega.");
        }

        entrega.setEntregador(entregador);
        entrega.setStatus(StatusEntregaEnum.ENVIADA);

        AtualizarEntregadorRequest atualizarEntregadorRequest = new AtualizarEntregadorRequest(false);
        entregadorService.atualizarStatusEntregador(entregadorId, atualizarEntregadorRequest);

        AtualizarStatusPedidoRequest atualizarStatusPedidoRequest = new AtualizarStatusPedidoRequest(StatusPedidoEnum.ENVIADO);
        pedidoService.atualizarStatusPedido(entrega.getPedidoId(), atualizarStatusPedidoRequest);

        return entregaRepository.save(entrega);
    }

    @Override
    public Entrega finalizarEntrega(Long entregaId) {

        Entrega entrega = buscarEntregaPorId(entregaId);

        if (!entrega.getStatus().equals(StatusEntregaEnum.ENVIADA)) {
            throw new IllegalStateException("A entrega precisa estar com status ENVIADA para ser finalizada.");
        }

        //TODO Validar se entregador se encontra no local de destino?

        AtualizarEntregadorRequest atualizarEntregadorRequest = new AtualizarEntregadorRequest(true);
        entregadorService.atualizarStatusEntregador(entrega.getEntregador().getEntregadorId(), atualizarEntregadorRequest);

        entrega.setStatus(StatusEntregaEnum.ENTREGUE);

        return entregaRepository.save(entrega);
    }

    @Override
    public Entrega buscarEntregaPorId(Long entregaId) {
        return entregaRepository.findById(entregaId)
                .orElseThrow(() -> new EntregaNotFoundException("Entrega de id: " + entregaId + " não encontrada."));
    }
}
