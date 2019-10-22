RaspberryAwards
===============

# Golden Raspberry Awards API

Esta API fornece uma lista de indicados e vencedores do Pior Filme do Golden Raspberry Awards e o produtor com maior intervalo entre dois pr�mios, e o que obteve dois pr�mios mais r�pido.

A aplica��o obt�m a lista de indicados de um arquivo .csv e salva em um banco de dados em mem�ria.

Para resolver esse caso utilizou-se Apache Camel seguindo os seguintes passos:

1. Configura��o do caminho do arquivo de leitura CSV no _application.properties_
2. Usa transa��o para assegurar que se um erro ocorrer ser� feito rolled back
3. Uso do Camel Bindy para transformar dados do CSV em POJO
4. Uso do padr�o Splitter para transmitir a mensagem
5. Uso de um bean para converter e tratar os dados
6. Uso do padr�o de integra��o Aggregator para agregar os POJOs em uma List
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


## Pr�-requisitos
* Java 1.8
* Apache Maven 3.0+

## Depend�ncias
* Spring Boot: 2.1.5.RELEASE
* Apache Camel: 3.0.0-RC2
* Banco de Dados HSQL


## Instala��o
```
mvn clean install
```

## Executando testes de integra��o
```
mvn test
```

S�o cobertos os seguintes casos de testes de integra��o:
- Teste do controler para os m�todos POST, GET
- Teste do controler para obter um indicado por ID
- Teste do controller para obter os intervalos entre os pr�mios dos produtores
- Teste do arquivo de leitura CSV e da defini��o do caminho do arquivo


## Executando a aplica��o
```
mvn spring-boot:run
```


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

## obtendo os intervalos entre os  pr�mios dos produtores
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
        "interval": 13,
        "previousWin": 2002,
        "followingWin": 2015
    }
}
```



## configura��es

outras configura��es editadas no arquivo _application.properties_:

### caminho padr�o do arquivo CSV
```
source.location=src/main/resources
source.file=movielist.csv
```

### porta padr�o da aplica��o
```
server.port=8181
```
