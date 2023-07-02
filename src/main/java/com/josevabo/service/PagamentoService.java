package com.josevabo.service;

import com.josevabo.model.Cliente;
import com.josevabo.model.Pagamento;
import com.josevabo.model.Produto;

import java.math.BigDecimal;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class PagamentoService {

    public List<Pagamento> ordenarPorData(List<Pagamento> pagamentos) {
        return pagamentos.stream()
                .sorted(Comparator.comparing(Pagamento::getDataCompra))
                .collect(Collectors.toList());
    }

    public Double calcularTotalPagamento(Pagamento pagamento){
        return pagamento.getProdutos().stream()
                .mapToDouble(p -> p.getPreco().doubleValue())
                .sum();
    }
    public BigDecimal calcularTotalPagamentos(List<Pagamento> pagamentos){
        return pagamentos.stream()
                .map(Pagamento::getProdutos)
                .flatMap(Collection::stream)
                .map(Produto::getPreco)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Map<String, Long> obterQuantidadePorProduto(List<Pagamento> pagamentos){
        return pagamentos.stream()
                .map(Pagamento::getProdutos)
                .flatMap(List::stream)
                .collect(Collectors.groupingBy(Produto::getNome, Collectors.counting()));
    }

    public Map<Cliente, List<Produto>> obterProdutosPorCliente(List<Pagamento> pagamentos){
        return pagamentos.stream()
                .collect(Collectors.groupingBy(p -> p.getAssinatura().getCliente(), Collectors.mapping(p -> p.getProdutos(), Collectors.toList())))
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .flatMap(List::stream).collect(Collectors.toList())
                ));
    }

    public Optional<Cliente> extrairClienteComMaiorGasto(List<Pagamento> pagamentos){
        Map<Cliente, List<Produto>> produtosPorCliente = obterProdutosPorCliente(pagamentos);
        return produtosPorCliente.entrySet().stream()
                .sorted(Comparator.comparing(entry ->
                        entry.getValue().stream()
                                .map(Produto::getPreco)
                                .reduce(BigDecimal.ZERO, BigDecimal::subtract))
                ).findFirst()
                .map(Map.Entry::getKey);
    }

    public Map<Month, BigDecimal> calcularValoresPorMes(List<Pagamento> pagamentos){
        return pagamentos.stream().collect(Collectors.groupingBy(p -> p.getDataCompra().getMonth()))
                .entrySet().stream()
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> entry.getValue().stream()
                                        .map(Pagamento::getProdutos)
                                        .flatMap(List::stream).map(Produto::getPreco)
                                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        ));
    }
}
