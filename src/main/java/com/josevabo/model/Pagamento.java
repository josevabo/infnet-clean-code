package com.josevabo.model;

import java.time.LocalDate;
import java.util.List;

public class Pagamento {
    private List<Produto> produtos;
    private final LocalDate dataCompra;
    private final Cliente cliente;

    public Pagamento(List<Produto> produtos, LocalDate dataCompra, Cliente cliente) {
        this.produtos = produtos;
        this.dataCompra = dataCompra;
        this.cliente = cliente;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public LocalDate getDataCompra() {
        return dataCompra;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void adicionar(Produto produto) {
        this.produtos.add(produto);
    }
}
