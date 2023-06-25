package com.josevabo;

import com.josevabo.model.Assinatura;
import com.josevabo.model.Cliente;
import com.josevabo.model.Pagamento;
import com.josevabo.model.Produto;

import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

public class App 
{
    public static void main( String[] args )
    {
        exercicio1();
    }

    private static void exercicio1(){
        // EXERCÍCIO 1
        System.out.println("\n\nEXERCÍCIO 1 - Pagamentos em datas distintas (hoje, q dia atrás e um mês atrás\n");
        Produto musica = new Produto("musica 1", Paths.get("downloads/teste.mp3"), BigDecimal.valueOf(2.99));
        Produto video = new Produto("video 1", Paths.get("downloads/teste.mp4"), BigDecimal.valueOf(4.99));
        Produto videogame = new Produto("videogame 1", Paths.get("downloads/teste.zip"), BigDecimal.valueOf(14.99));

        Cliente joao = new Cliente("Joao");
        Cliente maria = new Cliente("Maria");
        Cliente jose = new Cliente("José");

        List<Produto> produtosJoao = Arrays.asList(musica, videogame, video);
        Pagamento pagamentoJoao = new Pagamento(produtosJoao, LocalDate.now(), joao);
        System.out.println("Data do pagamento hoje: " + pagamentoJoao.getDataCompra() );

        List<Produto> produtosMaria = Arrays.asList(video, videogame);
        Pagamento pagamentoMaria = new Pagamento(produtosMaria, LocalDate.now().minusDays(1), maria);
        System.out.println("Data do pagamento há um dia: " + pagamentoMaria.getDataCompra() );

        List<Produto> produtosJose = Arrays.asList(musica, video);
        Pagamento pagamentoJose = new Pagamento(produtosJose, LocalDate.now().minusMonths(1), jose);
        System.out.println("Data do pagamento há um mês: " + pagamentoJose.getDataCompra());


        // EXERCÍCIO 2 - pagamentos ordenados por data
        System.out.println("\n\nEXERCÍCIO 2 - Pagamentos ordenados por data\n");

        List<Pagamento> pagamentos = Arrays.asList(pagamentoJoao, pagamentoMaria, pagamentoJose);
        pagamentos.sort(Comparator.comparing(Pagamento::getDataCompra));
        pagamentos.stream().map(Pagamento::getDataCompra).forEach(System.out::println);

        // EXERCÍCIO 3 -
        // Calcule e Imprima a soma dos valores de um pagamento com optional e recebendo um Double diretamente.
        System.out.println("\n\nEXERCÍCIO 3 - Calcule e Imprima a soma dos valores de um pagamento com optional e recebendo um Double diretamente\n");
        Double totalEmDouble = pagamentoJoao.getProdutos().stream().mapToDouble(p -> p.getPreco().doubleValue()).sum();
        System.out.println("total do pagamento é " + totalEmDouble);

        // EXERCÍCIO 4
        //  Calcule o Valor de todos os pagamentos da Lista de pagamentos.
        System.out.println("\n\nEXERCÍCIO 4 - Calcule o Valor de todos os pagamentos da Lista de pagamentos.\n");
        BigDecimal valorTotal = pagamentos.stream().map(Pagamento::getProdutos).flatMap(Collection::stream).map(Produto::getPreco).reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("Valor total dos pagamentos é: " + valorTotal);



        // EXERCÍCIO 5
        // Imprima a quantidade de cada Produto vendido.
        System.out.println("\n\nEXERCÍCIO 5 - Imprima a quantidade de cada Produto vendido.\n");
        pagamentos.stream()
                .map(Pagamento::getProdutos)
                .flatMap(List::stream)
                .collect(Collectors.groupingBy(Produto::getNome, Collectors.counting()))
                .forEach((k,v) ->
                        System.out.printf("Produto: \"%s\"  Quantidade vendida: %d%n",k,  v)
                );

        // EXERCÍCIO 6
        // Crie um Mapa de <Cliente, List<Produto> , onde Cliente pode ser o nome do cliente.
        System.out.println("\n\nEXERCÍCIO 6 - Crie um Mapa de <Cliente, List<Produto> , onde Cliente pode ser o nome do cliente.\n");
        Map<Cliente, List<Produto>> produtosPorCliente = pagamentos.stream()
                .collect(Collectors.groupingBy(Pagamento::getCliente, Collectors.mapping(p -> p.getProdutos(), Collectors.toList())))
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .flatMap(List::stream).collect(Collectors.toList())
                ));
        produtosPorCliente.forEach((k,v) -> System.out.println("Cliente " + k.getNome() + "; lista de produtos : " + v.stream().map(Produto::getNome).collect(Collectors.joining(", "))));

        // EXERCÍCIO 7
        // 7 - Qual cliente gastou mais?
        System.out.println("\n\nEXERCÍCIO 7 - Qual cliente gastou mais?\n");
        Optional<Cliente> clienteQueMaisGastou = produtosPorCliente.entrySet().stream()
                .sorted(Comparator.comparing(entry ->
                        entry.getValue().stream()
                                .map(Produto::getPreco)
                                .reduce(BigDecimal.ZERO, BigDecimal::subtract))
                ).findFirst().map(Map.Entry::getKey);
        clienteQueMaisGastou.ifPresent(c-> System.out.println("Cliente que mais gastou foi  "+ c.getNome()));


        // EXERCÍCIO 8
        // 8 - Quanto foi faturado em um determinado mês?
        System.out.println("\n\nEXERCÍCIO 8 - Quanto foi faturado em um determinado mês?\n");
        Map<Month, BigDecimal> vendasPorMes = pagamentos.stream().collect(Collectors.groupingBy(p -> p.getDataCompra().getMonth()))
                .entrySet().stream()
                .collect(
                        Collectors.toMap(
                            entry -> entry.getKey(),
                            entry -> entry.getValue().stream()
                                        .map(Pagamento::getProdutos)
                                        .flatMap(List::stream).map(Produto::getPreco)
                                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                            ));

        vendasPorMes.forEach((mes,vendas)-> System.out.printf("Total de %s em vendas no mes de %s%n", vendas, mes));

        // EXERCÍCIO 9
        // 9 - Crie 3 assinaturas com assinaturas de 99.98 reais, sendo 2 deles com assinaturas encerradas.
        System.out.println("\n\nEXERCÍCIO 9 - Crie 3 assinaturas com assinaturas de 99.98 reais, sendo 2 deles com assinaturas encerradas\n");
        Assinatura assinaturaJoao = new Assinatura(BigDecimal.valueOf(99.98), LocalDate.of(2023, 1, 1), joao);
        Assinatura assinaturaMaria = new Assinatura(BigDecimal.valueOf(99.98), LocalDate.of(2022, 12, 1), maria);
        Assinatura assinaturaJose = new Assinatura(BigDecimal.valueOf(99.98), LocalDate.of(2022, 10, 1), jose);
        assinaturaMaria.encerrar(LocalDate.now().minusMonths(2));
        assinaturaJose.encerrar(LocalDate.now().minusMonths(4));
        List<Assinatura> assinaturas = Arrays.asList(assinaturaJoao, assinaturaMaria, assinaturaJose);
        assinaturas.forEach(System.out::println);


        // EXERCÍCIO 10
        // 10 - Imprima o tempo em meses de alguma assinatura ainda ativa.
        System.out.println("\n\nEXERCÍCIO 10 - Imprima o tempo em meses de alguma assinatura ainda ativa.\n");

        LocalDate inicio = assinaturaJoao.getBegin();
        Period tempoDeAssinatura = Period.between(inicio, LocalDate.now());
        System.out.println("Início " + inicio);
        System.out.println("Meses ativos de assinatura de joão: " + tempoDeAssinatura.getMonths());


        // EXERCÍCIO 11
        // 11 - Imprima o tempo de meses entre o start e end de todas assinaturas. Não utilize IFs para assinaturas sem end Time.
        System.out.println("\n\nEXERCÍCIO 11 - Imprima o tempo de meses entre o start e end de todas assinaturas. Não utilize IFs para assinaturas sem end Time.\n");
        assinaturas.forEach(a -> {
            String cliente = a.getCliente().getNome();
            LocalDate inicioCliente = a.getBegin();
            LocalDate fimCliente = a.getEnd().orElse(LocalDate.now());
            int meses = Period.between(a.getBegin(), fimCliente).getMonths();
            System.out.println(String.format("Assinatura de %s. Inicio em %s. Fim em %s. Duração de %d meses", cliente, inicioCliente, fimCliente, meses));
        });

        // EXERCÍCIO 12
        // 12 - Calcule o valor pago em cada assinatura até o momento.
        System.out.println("\n\nEXERCÍCIO 12 - Calcule o valor pago em cada assinatura até o momento.\n");
        Map<Assinatura, BigDecimal> valoresAssinaturas = assinaturas.stream().collect(Collectors.toMap(
                assinatura -> assinatura,
                assinatura -> {
                    int tempoAssinatura = Period.between(assinatura.getBegin(), assinatura.getEnd().orElse(LocalDate.now())).getMonths();
                    return assinatura.getMensalidade().multiply(BigDecimal.valueOf(tempoAssinatura));
                }
        ));

        valoresAssinaturas.forEach(
                (ass, valor) -> System.out.printf("Assinatura do cliente %s com valor total de %s%n", ass.getCliente().getNome(), valor)
        );

    }

}
