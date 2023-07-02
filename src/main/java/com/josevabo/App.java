package com.josevabo;

import com.josevabo.exception.AssinaturaExpiradaException;
import com.josevabo.model.*;
import com.josevabo.service.PagamentoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class App 
{
    public static void main( String[] args )
    {
        exercicioDesignPattern();
    }

    private static void exercicioDesignPattern(){
        // Criação dos objetos de teste
        Produto musica = new Produto("musica 1", Paths.get("downloads/teste.mp3"), BigDecimal.valueOf(2.99));
        Produto video = new Produto("video 1", Paths.get("downloads/teste.mp4"), BigDecimal.valueOf(4.99));
        Produto videogame = new Produto("videogame 1", Paths.get("downloads/teste.zip"), BigDecimal.valueOf(14.99));

        Cliente joao = new Cliente("Joao");
        Cliente maria = new Cliente("Maria");
        Cliente jose = new Cliente("José");

        //ANTES: construtor complexo
//        Assinatura assinaturaJoao = new Assinatura(BigDecimal.valueOf(99.98), LocalDate.of(2023, 1, 1), joao, TipoAssinatura.TRIMESTRAL, LocalDate.now().plusMonths(3));
//        Assinatura assinaturaMaria = new Assinatura(BigDecimal.valueOf(99.98), LocalDate.of(2022, 12, 1), maria, TipoAssinatura.SEMESTRAL, LocalDate.now().plusMonths(2));
//        Assinatura assinaturaJose = new Assinatura(BigDecimal.valueOf(99.98), LocalDate.of(2022, 10, 1), jose, TipoAssinatura.ANUAL, LocalDate.now().plusMonths(1));
//        Assinatura assinaturaJoaoEncerrada = new Assinatura(BigDecimal.valueOf(99.98), LocalDate.of(2022, 10, 1), jose, TipoAssinatura.ANUAL, LocalDate.now().plusMonths(1));
//        assinaturaJoaoEncerrada.encerrar(LocalDate.now().minusMonths(2));

        //DEPOIS: builder torna a instanciação da Assinatura mais intuitiva
        Assinatura assinaturaJoao =  new Assinatura.Builder(99.98,joao, LocalDate.of(2023, 1, 1))
                .assinaturaTrimestral()
                .comDataVigencia(LocalDate.now().plusMonths(3))
                .build();

        Assinatura assinaturaMaria = new Assinatura.Builder(99.98,maria,LocalDate.of(2022, 12, 1))
                .assinaturaTrimestral()
                .comDataVigencia(LocalDate.now().plusMonths(2))
                .build();
        Assinatura assinaturaJose = new Assinatura.Builder(99.98, jose, LocalDate.of(2022, 10, 1))
                .assinaturaAnual()
                .comDataVigencia(LocalDate.now().plusMonths(1))
                .build();
        Assinatura assinaturaJoaoEncerrada = new Assinatura.Builder(99.98, joao, LocalDate.of(2022, 10, 1))
                .assinaturaAnual()
                .encerradaEm(LocalDate.now().minusMonths(2))
                .build();
    }

}
