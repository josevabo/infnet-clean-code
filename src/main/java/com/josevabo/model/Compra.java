package com.josevabo.model;

import com.josevabo.exception.AssinaturaExpiradaException;

import java.time.LocalDate;
import java.util.List;

public class Compra {
    private List<Produto> produtos;
    private final LocalDate dataCompra;
    private final Assinatura assinatura;

    public Compra(List<Produto> produtos, LocalDate dataCompra, Assinatura assinatura) throws AssinaturaExpiradaException {
        if(!assinatura.estaVigente())
            throw new AssinaturaExpiradaException("Não é possível realizar compra sem assinatura vigente");

        this.assinatura = assinatura;
        this.produtos = produtos;
        this.dataCompra = dataCompra;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public LocalDate getDataCompra() {
        return dataCompra;
    }

    public Assinatura getAssinatura() {
        return assinatura;
    }

    public void adicionar(Produto produto) {
        this.produtos.add(produto);
    }
}
