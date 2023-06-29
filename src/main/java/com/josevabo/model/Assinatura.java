package com.josevabo.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

public class Assinatura {
    private BigDecimal mensalidade;
    private final LocalDate begin;
    private Optional<LocalDate> end;
    private final Cliente cliente;
    private final TipoAssinatura tipoAssinatura;
    /**
     * Último dia válido para uso da assinatura, conforme pagamento
     */
    private LocalDate dataVigencia;

    public Assinatura(BigDecimal mensalidade, LocalDate begin, LocalDate end, Cliente cliente, TipoAssinatura tipoAssinatura, LocalDate dataVigencia) {
        this.mensalidade = mensalidade;
        this.begin = begin;
        this.end = Optional.of(end);
        this.cliente = cliente;
        this.tipoAssinatura = tipoAssinatura;
        this.dataVigencia = dataVigencia;
    }

    public Assinatura(BigDecimal mensalidade, LocalDate begin, Cliente cliente, TipoAssinatura tipoAssinatura, LocalDate dataVigencia) {
        this.mensalidade = mensalidade;
        this.begin = begin;
        this.end = Optional.empty();
        this.cliente = cliente;
        this.tipoAssinatura = tipoAssinatura;
        this.dataVigencia = dataVigencia;
    }

    public void encerrar(LocalDate dataEncerramento){
        this.end = Optional.of(dataEncerramento);
        this.dataVigencia = dataEncerramento;
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

    public TipoAssinatura getTipoAssinatura() {
        return tipoAssinatura;
    }

    public boolean estaVigente(){
        return !LocalDate.now().atStartOfDay().isAfter(dataVigencia.atStartOfDay());
    }

    public void estenderVigencia(int mesesAdicionais){
        this.dataVigencia = this.dataVigencia.plusMonths(mesesAdicionais);
    }

    public BigDecimal calcularTaxa(){
        LocalDate hoje = LocalDate.now();
        if (this.end.orElse(hoje.plusDays(1)).isAfter(hoje)){
            return this.tipoAssinatura.getTaxa().multiply(calcularValorAcumuladoAssinatura()).setScale(2, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal calcularValorAcumuladoAssinatura(){
        long mesesDeAssinatura = Period.between(this.begin, end.orElse(LocalDate.now())).toTotalMonths() + 1l;
        return this.mensalidade.multiply(BigDecimal.valueOf(mesesDeAssinatura));
    }

    public LocalDate getDataVigencia() {
        return dataVigencia;
    }

    @Override
    public String toString() {
        return "Assinatura{" +
                "mensalidade=" + mensalidade +
                ", begin=" + begin +
                ", end=" + end +
                ", cliente=" + cliente.getNome() +
                ", tipoAssinatura=" + tipoAssinatura +
                '}';
    }
}
