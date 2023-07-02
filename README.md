# Projeto Infnet Clean Code

## Lista de Exercícios 3 - Design Pattern Builder
Foi escolhida a classe Assinatura como alvo de uma pequena refatoração. A necessidade de vários atributos para construção de um objeto tornava complexa a instanciação por construtor.

Segue exemplo antes da aplicação do pattern Builder:
```java
Assinatura assinaturaJoao = new Assinatura(BigDecimal.valueOf(99.98), LocalDate.of(2023, 1, 1), joao, TipoAssinatura.TRIMESTRAL, LocalDate.now().plusMonths(3));
```

Com a aplicação do pattern builder a classe é possível instanciar de forma intuitiva objetos que tenham características diferentes, como tipo de assinatura, data encerramento e vigência.

Exemplo de assinatura com encerramento antes e depois do pattern:
```java
//ANTES
Assinatura assinaturaJoaoEncerrada = new Assinatura(
        BigDecimal.valueOf(99.98), 
        LocalDate.of(2022, 10, 1), 
        LocalDate.of(2023, 12, 1), 
        jose, 
        TipoAssinatura.ANUAL, 
        LocalDate.now().plusMonths(1));

//DEPOIS
Assinatura assinaturaJoaoEncerrada = new Assinatura.Builder(
        99.98, 
        joao, 
        LocalDate.of(2022, 10, 1))
    .assinaturaAnual()
    .encerradaEm(LocalDate.now().minusMonths(2))
    .build();
```

Após a criação da classe interna Builder, é possível iniciar um objeto com atributos mínimos e acrescentar atributos de forma mais legível com métodos dedicados.