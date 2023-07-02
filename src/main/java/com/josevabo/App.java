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
    private static final Logger LOG = LoggerFactory.getLogger(App.class);
    public static void main( String[] args )
    {
        // tarefas especificas da lista 2 executadas aqui
        exercicioEspecificoLista2();
        // os 12 exercicios da lista 1 estão aqui refatorados.
        // Todos os algoritmos de ordenar, agrupar e calcular foram extraídos para suas classes ou classe service respectiva
        exerciciosRefatorados();
    }

    private static void exercicioEspecificoLista2(){
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

        List<Pagamento> pagamentos = new ArrayList<>();
         // VERIFICAÇÃO SE ASSINATURA ESTÁ VÁLIDA NO MOMENTO DA COMPRA
        try{
            Pagamento pagamentoJoao = new Pagamento(produtosJoao, LocalDate.now(), assinaturaJoao);
            Pagamento pagamentoMaria = new Pagamento(produtosMaria, LocalDate.now().minusDays(1), assinaturaMaria);
            Pagamento pagamentoJose = new Pagamento(produtosJose, LocalDate.now().minusMonths(1), assinaturaJose);
            pagamentos = Arrays.asList(pagamentoMaria, pagamentoJoao, pagamentoJose);
            // Assinatura expirada impedirá criação de compra
            new Pagamento(produtosJoao, LocalDate.now(), assinaturaJoaoEncerrada);
        } catch (AssinaturaExpiradaException e) {
            LOG.error("Controle de assinatura expirada. Caso positivo, compra não é efetivada:");
            LOG.error(e.getMessage());
        }
        pagamentos.forEach(c -> LOG.info(c.toString()));


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

    public static void exerciciosRefatorados(){

        PagamentoService pagamentoService = new PagamentoService();

        // EXERCÍCIO 1
        System.out.println("\n\nEXERCÍCIO 1 - Pagamentos em datas distintas (hoje, q dia atrás e um mês atrás\n");
        Produto musica = new Produto("musica 1", Paths.get("downloads/teste.mp3"), BigDecimal.valueOf(2.99));
        Produto video = new Produto("video 1", Paths.get("downloads/teste.mp4"), BigDecimal.valueOf(4.99));
        Produto videogame = new Produto("videogame 1", Paths.get("downloads/teste.zip"), BigDecimal.valueOf(14.99));

        Cliente joao = new Cliente("Joao");
        Cliente maria = new Cliente("Maria");
        Cliente jose = new Cliente("José");

        Assinatura assinaturaJoao = new Assinatura(BigDecimal.valueOf(99.98), LocalDate.of(2023, 1, 1), joao, TipoAssinatura.ANUAL, LocalDate.now().plusMonths(1));
        Assinatura assinaturaMaria = new Assinatura(BigDecimal.valueOf(99.98), LocalDate.of(2022, 12, 1), maria, TipoAssinatura.ANUAL, LocalDate.now().plusMonths(1));
        Assinatura assinaturaJose = new Assinatura(BigDecimal.valueOf(99.98), LocalDate.of(2022, 10, 1), jose, TipoAssinatura.ANUAL, LocalDate.now().plusMonths(1));


        List<Produto> produtosJoao = Arrays.asList(musica, videogame, video);
        Pagamento pagamentoJoao = new Pagamento(produtosJoao, LocalDate.now(), assinaturaJoao);
        System.out.println("Data do pagamento hoje: " + pagamentoJoao.getDataCompra() );

        List<Produto> produtosMaria = Arrays.asList(video, videogame);
        Pagamento pagamentoMaria = new Pagamento(produtosMaria, LocalDate.now().minusDays(1), assinaturaMaria);
        System.out.println("Data do pagamento há um dia: " + pagamentoMaria.getDataCompra() );

        List<Produto> produtosJose = Arrays.asList(musica, video);
        Pagamento pagamentoJose = new Pagamento(produtosJose, LocalDate.now().minusMonths(1), assinaturaJose);
        System.out.println("Data do pagamento há um mês: " + pagamentoJose.getDataCompra());


        // EXERCÍCIO 2 - pagamentos ordenados por data
        System.out.println("\n\nEXERCÍCIO 2 - Pagamentos ordenados por data\n");

        List<Pagamento> pagamentos = Arrays.asList(pagamentoJoao, pagamentoMaria, pagamentoJose);
        pagamentoService.ordenarPorData(pagamentos).forEach(System.out::println);

        // EXERCÍCIO 3 -
        // Calcule e Imprima a soma dos valores de um pagamento com optional e recebendo um Double diretamente.
        System.out.println("\n\nEXERCÍCIO 3 - Calcule e Imprima a soma dos valores de um pagamento com optional e recebendo um Double diretamente\n");
        Double totalEmDouble = pagamentoService.calcularTotalPagamento(pagamentoJoao);
        System.out.println("total do pagamento é " + totalEmDouble);

        // EXERCÍCIO 4
        //  Calcule o Valor de todos os pagamentos da Lista de pagamentos.
        System.out.println("\n\nEXERCÍCIO 4 - Calcule o Valor de todos os pagamentos da Lista de pagamentos.\n");
        BigDecimal valorTotal = pagamentoService.calcularTotalPagamentos(pagamentos);
        System.out.println("Valor total dos pagamentos é: " + valorTotal);



        // EXERCÍCIO 5
        // Imprima a quantidade de cada Produto vendido.
        System.out.println("\n\nEXERCÍCIO 5 - Imprima a quantidade de cada Produto vendido.\n");
        pagamentoService.obterQuantidadePorProduto(pagamentos)
                .forEach((k,v) ->
                        System.out.printf("Produto: \"%s\"  Quantidade vendida: %d%n",k,  v)
                );

        // EXERCÍCIO 6
        // Crie um Mapa de <Cliente, List<Produto> , onde Cliente pode ser o nome do cliente.
        System.out.println("\n\nEXERCÍCIO 6 - Crie um Mapa de <Cliente, List<Produto> , onde Cliente pode ser o nome do cliente.\n");
        Map<Cliente, List<Produto>> produtosPorCliente = pagamentoService.obterProdutosPorCliente(pagamentos);
        produtosPorCliente.forEach((k,v) -> System.out.println("Cliente " + k.getNome() + "; lista de produtos : " + v.stream().map(Produto::getNome).collect(Collectors.joining(", "))));

        // EXERCÍCIO 7
        // 7 - Qual cliente gastou mais?
        System.out.println("\n\nEXERCÍCIO 7 - Qual cliente gastou mais?\n");
        Optional<Cliente> clienteQueMaisGastou = pagamentoService.extrairClienteComMaiorGasto(pagamentos);
        clienteQueMaisGastou.ifPresent(c-> System.out.println("Cliente que mais gastou foi  "+ c.getNome()));


        // EXERCÍCIO 8
        // 8 - Quanto foi faturado em um determinado mês?
        System.out.println("\n\nEXERCÍCIO 8 - Quanto foi faturado em um determinado mês?\n");
        Map<Month, BigDecimal> vendasPorMes = pagamentoService.calcularValoresPorMes(pagamentos);
        vendasPorMes.forEach((mes,vendas)-> System.out.printf("Total de %s em vendas no mes de %s%n", vendas, mes));

        // EXERCÍCIO 9
        // 9 - Crie 3 assinaturas com assinaturas de 99.98 reais, sendo 2 deles com assinaturas encerradas.
        System.out.println("\n\nEXERCÍCIO 9 - Crie 3 assinaturas com assinaturas de 99.98 reais, sendo 2 deles com assinaturas encerradas\n");
        assinaturaJoao = new Assinatura(BigDecimal.valueOf(99.98), LocalDate.of(2023, 1, 1), joao , TipoAssinatura.ANUAL, LocalDate.now().plusMonths(1));
        assinaturaMaria = new Assinatura(BigDecimal.valueOf(99.98), LocalDate.of(2022, 12, 1), maria, TipoAssinatura.ANUAL, LocalDate.now().plusMonths(1));
        assinaturaJose = new Assinatura(BigDecimal.valueOf(99.98), LocalDate.of(2022, 10, 1), jose, TipoAssinatura.ANUAL, LocalDate.now().plusMonths(1));
        assinaturaMaria.encerrar(LocalDate.now().minusMonths(2));
        assinaturaJose.encerrar(LocalDate.now().minusMonths(4));
        List<Assinatura> assinaturas = Arrays.asList(assinaturaJoao, assinaturaMaria, assinaturaJose);
        assinaturas.forEach(System.out::println);


        // EXERCÍCIO 10
        // 10 - Imprima o tempo em meses de alguma assinatura ainda ativa.
        System.out.println("\n\nEXERCÍCIO 10 - Imprima o tempo em meses de alguma assinatura ainda ativa.\n");
        assinaturaJoao.estaVigente();
        System.out.println("Início " + assinaturaJoao.getBegin());
        System.out.println("Meses ativos de assinatura de joão: " + assinaturaJoao.getMesesAssinatura());


        // EXERCÍCIO 11
        // 11 - Imprima o tempo de meses entre o start e end de todas assinaturas. Não utilize IFs para assinaturas sem end Time.
        System.out.println("\n\nEXERCÍCIO 11 - Imprima o tempo de meses entre o start e end de todas assinaturas. Não utilize IFs para assinaturas sem end Time.\n");
        assinaturas.forEach(a -> {
            System.out.println(String.format("Assinatura de %s. Inicio em %s. Fim em %s. Duração de %d meses", a.getCliente().getNome(), a.getBegin(), a.getEnd(), a.getMesesAssinatura()));
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

        assinaturas.forEach(
                a -> System.out.printf("Assinatura do cliente %s com valor total de %s%n", a.getCliente().getNome(), a.calcularValorAcumuladoAssinatura())
        );

    }

}
