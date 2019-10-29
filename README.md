RaspberryAwards
===============

# Golden Raspberry Awards API

Esta API fornece uma lista de indicados e vencedores do Pior Filme do Golden Raspberry Awards e o produtor com maior intervalo entre dois prêmios, e o que obteve dois prêmios mais rápido.

A aplicação obtém a lista de indicados de um arquivo .csv e salva em um banco de dados em memória.

Para resolver esse caso utilizou-se Apache Camel seguindo os seguintes passos:

1. Configuração do caminho do arquivo de leitura CSV no _application.properties_
2. Usa transação para assegurar que se um erro ocorrer será feito rolled back
3. Uso do Camel Bindy para transformar dados do CSV em POJO
4. Uso do padrão Splitter para transmitir a mensagem
5. Uso de um bean para converter e tratar os dados
6. Uso do padrão de integração Aggregator para agregar os POJOs em uma List
9. Uso de um processador jpa via Camel para persistir a lista em batch no banco de dados

### Splitter EIP:
![Splitter:](http://www.enterpriseintegrationpatterns.com/img/Sequencer.gif)

### Aggregator EIP:
![Aggregator:](http://www.enterpriseintegrationpatterns.com/img/Aggregator.gif)


_MovieListCSVRoute.java_
```
    from("file:"+folderLocation+"?noop=true&recursive=false&fileName="+movieListFileName)
	        .transacted()
	        .unmarshal(bindyCsvDataFormat)
	        .split(body())
	        .streaming()
	        .bean(mapper, "convertAndTransform")
	        .aggregate(constant(true), new ArrayListAggregationStrategy())
	        .completionTimeout(10000)
	        .to("jpa:com.texo.raspberry.model.Nominee?entityType=java.util.List")
	        .end();
```


## Pré-requisitos
* Java 1.8
* Apache Maven 3.0+

## Dependências
* Spring Boot: 2.1.5.RELEASE
* Apache Camel: 3.0.0-RC2
* Banco de Dados HSQL


## Executando a aplicação
```
mvn spring-boot:run
```



## Executando testes
```
mvn test
```


São cobertos os seguintes casos de testes:
- Teste do controller para o validação dos dados do método /intervals
- Teste unitário do service para o cálculo dos intervalos
- Teste do controller para os métodos POST, GET
- Teste do controller para obter um indicado por ID
- Teste do controller para obter os intervalos entre os prêmios dos produtores
- Teste do arquivo de leitura CSV e da definição do caminho do arquivo


## obtendo a lista de indicados
```GET http://localhost:8181/nominees```

```
[
    {
        "id": 1,
        "year": 1980,
        "title": "Can't Stop the Music",
        "studios": "Associated Film Distribution",
        "producers": "Allan Carr",
        "winner": true
    },
    {
        "id": 2,
        "year": 1980,
        "title": "Cruising",
        "studios": "Lorimar Productions, United Artists",
        "producers": "Jerry Weintraub",
        "winner": false
    },
    .
    .
    .
```

## obtendo os intervalos entre os  prêmios dos produtores
```GET http://localhost:8181/nominees/intervals```

```
{
    "min": {
        "producer": "Joel Silver",
        "interval": 1,
        "previousWin": 1990,
        "followingWin": 1991
    },
    "max": {
        "producer": "Matthew Vaughn",
        "interval": 35,
        "previousWin": 1980,
        "followingWin": 2015
    }
}
```



## configurações

outras configurações editadas no arquivo _application.properties_:

### caminho padrão do arquivo CSV
```
source.location=src/main/resources
source.file=movielist.csv
```

### porta padrão da aplicação
```
server.port=8181
```
