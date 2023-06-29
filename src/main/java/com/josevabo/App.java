package com.josevabo;

import com.josevabo.exception.AssinaturaExpiradaException;
import com.josevabo.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class App 
{
    private static final Logger LOG = LoggerFactory.getLogger(App.class);
    public static void main( String[] args )
    {
        exercicio1();
    }

    private static void exercicio1(){

        // Criação dos objetos de teste
        Produto musica = new Produto("musica 1", Paths.get("downloads/teste.mp3"), BigDecimal.valueOf(2.99));
        Produto video = new Produto("video 1", Paths.get("downloads/teste.mp4"), BigDecimal.valueOf(4.99));
        Produto videogame = new Produto("videogame 1", Paths.get("downloads/teste.zip"), BigDecimal.valueOf(14.99));

        Cliente joao = new Cliente("Joao");
        Cliente maria = new Cliente("Maria");
        Cliente jose = new Cliente("José");

        Assinatura assinaturaJoao = new Assinatura(BigDecimal.valueOf(99.98), LocalDate.of(2023, 1, 1), joao, TipoAssinatura.TRIMESTRAL, LocalDate.now().plusMonths(3));
        Assinatura assinaturaMaria = new Assinatura(BigDecimal.valueOf(99.98), LocalDate.of(2022, 12, 1), maria, TipoAssinatura.SEMESTRAL, LocalDate.now().plusMonths(2));
        Assinatura assinaturaJose = new Assinatura(BigDecimal.valueOf(99.98), LocalDate.of(2022, 10, 1), jose, TipoAssinatura.ANUAL, LocalDate.now().plusMonths(1));
        Assinatura assinaturaJoaoEncerrada = new Assinatura(BigDecimal.valueOf(99.98), LocalDate.of(2022, 10, 1), jose, TipoAssinatura.ANUAL, LocalDate.now().plusMonths(1));
        assinaturaJoaoEncerrada.encerrar(LocalDate.now().minusMonths(2));
        List<Assinatura> assinaturas = Arrays.asList(assinaturaJoao, assinaturaMaria, assinaturaJose, assinaturaJoaoEncerrada);
        assinaturas.forEach(a -> LOG.info(a.toString()));

        List<Produto> produtosJoao = Arrays.asList(musica, videogame, video);
        List<Produto> produtosMaria = Arrays.asList(video, videogame);
        List<Produto> produtosJose = Arrays.asList(musica, video);

        List<Compra> compras = new ArrayList<>();
         // VALIDAÇÃO SE ASSINATURA ESTÁ VÁLIDA NO MOMENTO DA COMPRA
        try{
            Compra compraJoao = new Compra(produtosJoao, LocalDate.now(), assinaturaJoao);
            Compra compraMaria = new Compra(produtosMaria, LocalDate.now().minusDays(1), assinaturaMaria);
            Compra compraJose = new Compra(produtosJose, LocalDate.now().minusMonths(1), assinaturaJose);
            compras = Arrays.asList(compraMaria, compraJoao, compraJose);
            // Assinatura expirada impedirá criação de compra
            new Compra(produtosJoao, LocalDate.now(), assinaturaJoaoEncerrada);
        } catch (AssinaturaExpiradaException e) {
            LOG.error("Controle de assinatura expirada. Caso positivo, compra não é efetivada:");
            LOG.error(e.getMessage());
        }
        compras.forEach(c -> LOG.info(c.toString()));


        // TAXAS APLICADAS ÀS ASSINATURAS
        LOG.info("Taxas diferentes para tipos de assinaturas diferentes:");
        Assinatura assinaturaTrimestral = new Assinatura(BigDecimal.valueOf(99.99), LocalDate.parse("2021-01-01"), joao, TipoAssinatura.TRIMESTRAL, LocalDate.now().plusMonths(2));
        Assinatura assinaturaSemestral = new Assinatura(BigDecimal.valueOf(80.99), LocalDate.parse("2021-11-01"), joao, TipoAssinatura.SEMESTRAL, LocalDate.now().plusMonths(3));
        Assinatura assinaturaTrimestralEncerrada = new Assinatura(BigDecimal.valueOf(99.99), LocalDate.parse("2022-01-01"), LocalDate.of(2023,01,01), joao, TipoAssinatura.ANUAL, LocalDate.now().minusMonths(1));
        ArrayList<Assinatura> assinaturas2 = new ArrayList<>();
        assinaturas2.add(assinaturaTrimestral);
        assinaturas2.add(assinaturaSemestral);
        assinaturas2.add(assinaturaTrimestralEncerrada);
        assinaturas2.forEach(a -> {
            TipoAssinatura tipo = a.getTipoAssinatura();
            int taxa = a.getTipoAssinatura().getTaxa().multiply(BigDecimal.valueOf(100)).intValue();
            String message = String.format("Assinatura do tipo %s tem taxa de %d%% %n",
                    tipo, taxa);
            LOG.info(message);
            LOG.info(String.valueOf(a.calcularTaxa()));
        });

        LOG.info("Estendendo vigência de assinatura para tornar vigente:");
        Assinatura assinatura = new Assinatura(BigDecimal.valueOf(99), LocalDate.of(2022, 1, 1), joao, TipoAssinatura.TRIMESTRAL, LocalDate.now().minusMonths(1));
        Consumer<Assinatura> imprimirDadosAssinatura = ass -> {
                String vigente = ass.estaVigente() ? "SIM": "NÃO";
                LocalDate dataVigencia = ass.getDataVigencia();
            String message = String.format("Assinatura com vigência até %s está vigente? %s %n",
                    dataVigencia, vigente);
            LOG.info(message);
        };
        imprimirDadosAssinatura.accept(assinatura);
        assinatura.estenderVigencia(2);
        imprimirDadosAssinatura.accept(assinatura);

    }

}
