package com.josevabo.model;

import java.math.BigDecimal;
import java.nio.file.Path;

public class Produto {
    private final String nome;
    private final Path file;
    private BigDecimal preco;

    public Produto(String nome, Path file, BigDecimal preco) {
        this.nome = nome;
        this.file = file;
        this.preco = preco;
    }

    public String getNome() {
        return nome;
    }

    public Path getFile() {
        return file;
    }

    public BigDecimal getPreco() {
        return preco;
    }
}
