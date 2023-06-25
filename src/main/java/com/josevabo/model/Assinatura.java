package com.josevabo.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public class Assinatura {
    private BigDecimal mensalidade;
    private final LocalDate begin;
    private Optional<LocalDate> end;
    private final Cliente cliente;

    public Assinatura(BigDecimal mensalidade, LocalDate begin, Optional<LocalDate> end, Cliente cliente) {
        this.mensalidade = mensalidade;
        this.begin = begin;
        this.end = end;
        this.cliente = cliente;
    }

    public Assinatura(BigDecimal mensalidade, LocalDate begin, Cliente cliente) {
        this.mensalidade = mensalidade;
        this.begin = begin;
        this.cliente = cliente;
        this.end = Optional.empty();
    }

    public void encerrar(LocalDate dataEncerramento){
        this.end = Optional.of(dataEncerramento);
    }

    public BigDecimal getMensalidade() {
        return mensalidade;
    }

    public LocalDate getBegin() {
        return begin;
    }

    public Optional<LocalDate> getEnd() {
        return end;
    }

    public Cliente getCliente() {
        return cliente;
    }

    @Override
    public String toString() {
        return "Assinatura{" +
                "mensalidade=" + mensalidade +
                ", begin=" + begin +
                ", end=" + end +
                ", cliente=" + cliente.getNome() +
                '}';
    }
}
