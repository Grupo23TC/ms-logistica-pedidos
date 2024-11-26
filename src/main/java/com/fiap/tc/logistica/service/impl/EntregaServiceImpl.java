package com.fiap.tc.logistica.service.impl;

import com.fiap.tc.logistica.dto.request.entregador.AtualizarEntregadorRequest;
import com.fiap.tc.logistica.dto.request.pedido.AtualizarRastreioRequest;
import com.fiap.tc.logistica.dto.request.pedido.AtualizarStatusPedidoRequest;
import com.fiap.tc.logistica.dto.request.entrega.CalcularEntregaRequest;
import com.fiap.tc.logistica.exception.EntregaNotFoundException;
import com.fiap.tc.logistica.model.Entrega;
import com.fiap.tc.logistica.model.Entregador;
import com.fiap.tc.logistica.model.Pedido;
import com.fiap.tc.logistica.model.enums.StatusEntregaEnum;
import com.fiap.tc.logistica.model.enums.StatusPedidoEnum;
import com.fiap.tc.logistica.model.rota.LocalizacaoRota;
import com.fiap.tc.logistica.model.rota.RotaResponse;
import com.fiap.tc.logistica.repository.EntregaRepository;
import com.fiap.tc.logistica.service.*;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.annotation.KafkaListener;
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

//    @Autowired
//    private NotificacaoEntregadorProducer producer;
//
//    @Autowired
//    private NotificacaoEntregadorConsumer consumer;

    @Override
    public RotaResponse calcularECriarEntrega(CalcularEntregaRequest request) {

        List<Entrega> entregaPorPedido = entregaRepository.findByPedidoIdAndStatusNot(request.pedidoId(), StatusEntregaEnum.CANCELADA);

        if (!entregaPorPedido.isEmpty()) {
            throw new IllegalStateException("Já existe uma entrega com esse pedido");
        }

//        Cliente cliente = clienteService.buscarClientePorId(request.clientId());

        Pedido pedido = pedidoService.buscarPedidoPorId(request.pedidoId());

//        if (!cliente.getClienteId().equals(pedido.getUsuarioId())) {
//            throw new IllegalArgumentException("O Cliente solicitando a entrega não é o cliente que solicitou o pedido.");
//        }

        LocalizacaoRota origem = new LocalizacaoRota(request.latOrig(), request.lngOrig());
        LocalizacaoRota destino = new LocalizacaoRota(request.latDest(), request.lngDest());

        RotaResponse rotaResponse = rotaService.calcularRota(origem, destino);

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

//        producer.sendMessage("teste");

        return entregaRepository.save(entrega);
    }

    @Override
//    @KafkaListener(topics = "${spring.kafka.topic}", groupId = "${spring.kafka.group}")
    public void notificarEntregadores(String message) {

        System.out.println("Received message: " + message);

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

        AtualizarRastreioRequest rastreioRequest = new AtualizarRastreioRequest(entregaId);
        pedidoService.atualizarRastreioPedido(entrega.getPedidoId(), rastreioRequest);

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
    public Entrega cancelarEntrega(Long entregaId) {

        Entrega entrega = buscarEntregaPorId(entregaId);

        if (entrega.getStatus().equals(StatusEntregaEnum.ENTREGUE)) {
            throw new IllegalStateException("Você não pode cancelar uma entrega já finalizada.");
        }

        if (entrega.getEntregador() != null && !entrega.getEntregador().getEstaDisponivel()) {
            AtualizarEntregadorRequest atualizarEntregadorRequest = new AtualizarEntregadorRequest(true);
            entregadorService.atualizarStatusEntregador(entrega.getEntregador().getEntregadorId(), atualizarEntregadorRequest);
        }

        if (StatusEntregaEnum.ENVIADA.equals(entrega.getStatus())) {
            AtualizarStatusPedidoRequest atualizarStatusPedidoRequest = new AtualizarStatusPedidoRequest(StatusPedidoEnum.CRIADO);
            pedidoService.atualizarStatusPedido(entrega.getPedidoId(), atualizarStatusPedidoRequest);

            AtualizarRastreioRequest rastreioRequest = new AtualizarRastreioRequest(null);
            pedidoService.atualizarRastreioPedido(entrega.getPedidoId(), rastreioRequest);
        }

        entrega.setStatus(StatusEntregaEnum.CANCELADA);

        return entregaRepository.save(entrega);
    }

    @Override
    public Entrega buscarEntregaPorId(Long entregaId) {
        return entregaRepository.findById(entregaId)
                .orElseThrow(() -> new EntregaNotFoundException("Entrega de id: " + entregaId + " não encontrada."));
    }
}
