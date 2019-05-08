# StarWars Planet API

Esta API é responsável por armazenar informações de planetas dos filmes StarWars. Possui um cadastro de planetas que é integrado à API: https://swapi.co/ 

## Iniciando

Estas instruções vão fazer você ter uma cópia do projeto pronta para executar localmente.

### Pré-requisitos

O que você precisa ter instalado para instalar e executar o projeto

```
Java 1.8
Maven
docker e docker-compose
```

### Instalando

Passo a passo para executar a aplicação localmente

Criar a estrutura do docker e inicialização do banco de dados

```
mkdir -p /storage/mysql_init
mkdir -p /storage/docker/mysql-datadir
```

Copiar script que o docker usará para criar a tabela

```
cp ./run-it/starwars_create_table.sql /storage/mysql_init
```

Iniciar banco de dados

```
cd ./run-it
docker-compose up -d
```

Executar a aplicação

```
mvn spring-boot:run
Ou 
mvn package
java -jar ./target/star-wars-game-1.0.0.jar
```

## Executando os testes

Para executar os testes somente é necessário

```
mvn clean test
```
