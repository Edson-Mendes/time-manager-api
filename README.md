<h1 align="center">Time Manager API</h1>

![Badge Em Desenvolvimento](https://img.shields.io/static/v1?label=Status&message=Em+Desenvolvimento&color=yellow&style=for-the-badge)
![Badge Java](https://img.shields.io/static/v1?label=Java&message=17&color=red&style=for-the-badge&logo=java)
![Badge Spring](https://img.shields.io/static/v1?label=Spring&message=v2.6.8&color=brightgreen&style=for-the-badge&logo=spring)

## :book: Resumo do projeto

Time Manager API é uma aplicação para auxiliar no gerenciamento de tempo gasto em atividades (ex. esse projeto).

## :hammer: Funcionalidades

- `Atividades`
    - `cadastrar`: Cadastro de atividade através de um POST para **/activities** com as informações de nome e descrição da
      atividade no corpo da requisição.
    - `Buscar`: Busca paginada de atividades através de um GET para **/activities**.
    - `Atualizar`: Atualizar atividade através de um PUT para **/activities/{ID}**, onde *{ID}* é o identificador da atividade.
    - `Deletar`: Deletar atividade através de um DELETE para **/activities/{ID}**, onde *{ID}* é o identificador da atividade.
- `Intervals`
  - `cadastrar`: Cadastro de intervalo de execução de uma atividade através de POST para **/activities/{activityID}/intervals**, onde *{activityID}* é o identificador da ativiadade.
  - `Buscar`: Busca paginada dos intervals de determinada atividade através de um GET para **/activities/{activityID}/intervals**, onde *{activityID}* é o identificador da ativiadade..
  - `Deletar`: Deletar interval através de um DELETE para **/activities/{activityID}/intervals/{activityID}**, onde *{activityID}* é o identificador da atividade e *{intervalID}* é o identificador do intervalo.

## :toolbox: Tecnologias

- `Intellij`
- `Java 17`
- `Maven`
- `Spring Boot, Spring MVC, Spring Data JPA`
- `Docker`
- `PostgreSQL`
- `Lombok`
- `Bean Validation`
- `Postman`
- `JUnit 5`
- `Mockito`
- `TestContainer`
