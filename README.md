# Projeto Infnet Clean Code

## Lista de Exercícios 2
1. Foi criado o Enum TipoAssinatura, contendo o atributo taxa de cada tipo.
2. O atributo dataVigencia e método estaVigente() foram criados na classe Assinatura para controlar até quando a assinatura está vigente (paga)
3. Ao instanciar uma compra, o construtor sempre checa se a assinatura está vigente, retornando erro específico em caso negativo

### Sonar
Foram executadas algumas rodadas de avaliação do sonar, a primeira passou, mas listou alguns code smells:
![Sonar inicial com code smells](/images/sonar_1.png)

Os code smells foram resolvidos, inclusive sendo necessário adicionar um logger ao projeto e arquivo de configuração (main/resources/log4j2.xml):
![Sonar inicial com code smells](/images/sonar_2.png)
