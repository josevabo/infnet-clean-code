package com.josevabo.model;

import java.math.BigDecimal;

public enum TipoAssinatura {
    ANUAL("Anual", BigDecimal.ZERO),
    SEMESTRAL("Semestral", BigDecimal.valueOf(0.03)),
    TRIMESTRAL("Trimestral", BigDecimal.valueOf(0.05));

    private String label;
    private BigDecimal taxa;

    TipoAssinatura(String label, BigDecimal taxa){
        this.label = label;
        this.taxa = taxa;
    }

    public String getLabel() {
        return label;
    }

    public BigDecimal getTaxa() {
        return taxa;
    }
}
