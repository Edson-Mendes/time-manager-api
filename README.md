<h1 align="center">Time Manager API</h1>

![Badge Em Desenvolvimento](https://img.shields.io/static/v1?label=Status&message=Em+Desenvolvimento&color=yellow&style=for-the-badge)
![Badge Java](https://img.shields.io/static/v1?label=Java&message=17&color=red&style=for-the-badge&logo=java)
![Badge Spring](https://img.shields.io/static/v1?label=Spring&message=v2.6.8&color=brightgreen&style=for-the-badge&logo=spring)

## :book: Resumo do projeto

Time Manager API é uma aplicação para auxiliar no gerenciamento de tempo gasto em atividades (ex. esse projeto).
Com essa ferramenta o usuário terá total controle de seu tempo aplicado em atividades/projetos.

## :hammer: Funcionalidades

### :lock: API de gerenciamento de Sign Up

- `Sign Up de usuário - POST /signup`: Cadastro de usuário enviando as informações **name**, **email**, **password** e 
**confirm** em um JSON no corpo da requisição. Não é necessário estar autenticado.

  O password é salvo criptografado no banco de dados usando BCryp.

    Segue abaixo um exemplo do corpo da requisição<br>
    ```json
    {
      "name": "Lorem Ipsum",
      "email": "lorem@email.com",
      "password": "1234567890",
      "confirm": "1234567890"
    }
    ```
    Em caso de sucesso a resposta tem status 201 com um JSON no corpo da resposta contendo informações do usuário registrado.
    Segue abaixo um exemplo do corpo da resposta.<br>
    ```json
    {
      "id": 100,
      "name": "Lorem Ipsum",
      "email": "lorem@email.com"
    }
    ```
  
  
### :lock: API de gerenciamento de Sign In

- `Sign In de usuário - POST /signin`: Sign in de usuário enviando as informações **email** e **password** 
em um JSON no corpo da requisição. Não é necessário estar autenticado.

    Segue abaixo um exemplo do corpo da requisição<br>
    ```json
    {
      "email": "lorem@email.com",
      "password": "1234567890",
    }
    ```
    Em caso de sucesso a resposta tem status 200 com um JSON no corpo da resposta contendo informações do token de autorização do usuário.
    Segue abaixo um exemplo do corpo da resposta.<br>
    ```json
    {
      "type": "Bearer",
      "token": "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJUaW1lIE1hbmFnZXIgQVBJIiwic3ViIjoiMSIsImlhdCI6MTY5NjM1OTA1MiwiZXhwIjoxNjk2MzYyNjUyfQ.YkDs4Zu4WveFHnjEghXorkTaKPA6hB4_7rlBcUJFN-8"
    }
    ```

### API de gerenciamento de Atividades

- `Cadastrar atividade - POST /activities`: Cadastro de atividade enviando as informações de **name** e **description** da
      atividade no corpo da requisição. É necessário estar autenticado.
    
    Segue abaixo um exemplo do corpo da requisição:<br>
    ```json
    {
      "name": "Desenvolver Time Manager API",
      "description": "Implementar o backend da aplicação Time Manager utilizando Java e Spring."  
    }
    ```

    Em caso de sucesso a resposta tem status 200 com um JSON no corpo da resposta contendo informações do atividade cadastrada.
    Segue abaixo um exemplo do corpo da resposta.<br>
    ```json
    {
      "id": 10000
      "name": "Desenvolver Time Manager API",
      "description": "Implementar o backend da aplicação Time Manager utilizando Java e Spring.",
      "createdAt": "2023-10-03T18:55:50.788Z",
      "status": "ACTIVE"
    }
    ```
    
- `Buscar atividades - GET /activities`: Busca paginada de atividades, o número da página (page), o tamanho da página (size) e o modo de ordenação (sort) podem ser alterados de acordo com a necessidade do cliente. É necessário estar autenticado. É necessário estar autenticado.

    Em caso de sucesso a resposta tem status 200 com um JSON no corpo da resposta contendo as informações das atividades.
    Segue abaixo um exemplo do corpo da resposta para requisição GET /activities?page=0&size=5&sort=createdAt,DESC<br>    
  ```json
  {
    "content": [
      {
        "id": 2,
        "name": "Estudar Java",
        "description": "Estudar essa belíssima linguagem verbosa <3.",
        "createdAt": "2023-10-03 16:14:17",
        "status": "ACTIVE"
      },
      {
        "id": 1,
        "name": "Desenvolver Time Manager API",
        "description": "Implementar o backend da aplicação Time Manager utilizando Java e Spring.",
        "createdAt": "2023-10-03 16:13:03",
        "status": "ACTIVE"
      }
    ],
    "pageable": {
      "sort": {
        "sorted": true,
        "unsorted": false,
        "empty": false
      },
      "pageNumber": 0,
      "pageSize": 5,
      "offset": 0,
      "paged": true,
      "unpaged": false
    },
    "totalPages": 1,
    "totalElements": 2,
    "last": true,
    "size": 5,
    "number": 0,
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    },
    "numberOfElements": 2,
    "first": true,
    "empty": false
  }
  ```
  
- `Atualizar atividades - PUT /activities/ID`: Atualizar atividade por **ID**, onde **ID** é o identificador da atividade e
  enviar as novas informações da atividade no corpo da requisição. É necessário estar autenticado.

  Segue abaixo um exemplo do corpo da requisição:<br>
  ```json
    {
      "name": "novo nome da atividade",
      "description": "nova descrição da atividade"  
    }
  ```

  Em caso de sucesso a resposta tem status 204.
  
- `Atualizar status da atividade - PATCH /activities/ID`: Atualizar status da atividade (ACTIVE ou CONCLUDED) por **ID**, onde **{ID}** é o identificador da atividade e
  enviar as novas informações da atividade no corpo da requisição. É necessário estar autenticado.

  Segue abaixo um exemplo do corpo da requisição:<br>
  ```json
    {
      "status" : "concluded"  
    }
  ```

  Em caso de sucesso a resposta tem status 204.
  
- `Deletar atividade - DELETE /activities/ID`: Deletar atividade por **ID**, onde **{ID}** é o identificador da atividade. É necessário estar autenticado.

  Em caso de sucesso a resposta tem status 204.
  
### API de gerenciamento de Intervalos

- `Cadastrar intervalo - POST /activities/ACTIVITY_ID/intervals`: Cadastro de intervalo de execução de uma atividade por ACTIVITY_ID, onde **ACTIVITY_ID** é o identificador da ativiadade e enviar as informações
  de **startedAT** e **elapsedTime** no corpo da requisição. É necessário estar autenticado.

  Segue abaixo um exemplo do corpo da requisição:<br>
  ```json
  {
    "startedAt": "2023-10-03T20:00:00",
    "elapsedTime": "00:45:00"
  }
  ```

  Em caso de sucesso a resposta tem status 201 e um JSON no corpo da resposta contendo as informações do intervalo cadastrado.
  Segue abaixo um exemplo do corpo da resposta.<br>

  ```json
  {
    "id": 5000000,
    "startedAt": "2023-10-03T20:00:00",
    "elapsedTime": "00:45:00"
  }
  ```
- `Buscar intervalos - GET /activities/ACTIVITY_ID/intervals`: Busca paginada de intervalos de uma atividade, onde **ACTIVITY_ID** é o identificador da ativiadade.

  O número da página (page), o tamanho da página (size) e o modo de ordenação (sort) podem ser alterados de acordo com a necessidade do cliente. É necessário estar autenticado.

  Em caso de sucesso a resposta tem status 200 e um JSON no corpo da resposta contendo as informações dos intervalos da atividade.
  Segue abaixo um exemplo do corpo da resposta:<br>
  ```json
  {
    "content": [
      {
        "id": 2,
        "startedAt": "2023-10-04T10:00:00",
        "elapsedTime": "00:52:00"
      },
      {
        "id": 1,
        "startedAt": "2023-10-03T20:00:00",
        "elapsedTime": "00:45:00"
      }
    ],
    "pageable": {
      "sort": {
        "sorted": true,
        "unsorted": false,
        "empty": false
      },
      "pageNumber": 0,
      "pageSize": 10,
      "offset": 0,
      "paged": true,
      "unpaged": false
    },
    "totalPages": 1,
    "totalElements": 2,
    "last": true,
    "size": 10,
    "number": 0,
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    },
    "numberOfElements": 2,
    "first": true,
    "empty": false
  }
  ```

- `Deletar intervalo - DELETE /activities/ACTIVITY_ID/intervals/INTERVAL_ID`: Deletar intervalo de uma atividade por **ACTIVITY_ID** e **INTERVAL_ID**, onde **ACTIVITY_ID** é o identificador da atividade e **INTERVAL_ID** é o identificador do intervalo.

  Em caso de sucesso a resposta tem status 204.

## Diagramas

### Diagrama entidade relacionamento

```mermaid
---
    title: Database Schema
---
    erDiagram
        USER {
            bigserial id PK
            varchar name
            varchar email
            varchar password
            boolean enabled
            timestamp created_at
        }
        ROLE {
            serial id PK
            varchar name
        }
        USER_ROLES {
            bigint user_id FK
            int roles_id FK
        }
        ACTIVITY {
            bigserial id PK
            varchar name
            varchar description
            varchar status
            timestamp created_at
            bigint user_id FK
        }
        INTERVAL {
            bigserial id PK
            timestamp started_at
            time elapsed_time
            bigint activity_id FK
        }
        
        USER }o--o{ USER_ROLES : has
        ROLE }o--o{ USER_ROLES : allows
        ACTIVITY ||--o{ INTERVAL : contains
        USER ||--o{ ACTIVITY : makes
```

### Diagrama de classes
```mermaid
---
    title: Business layer
---
classDiagram
    class UserService {
        <<interface>>
        findByUsername(String username) UserDetails
        findUserDetailsById(long id) UserDetails
        save(SignupRequest signupRequest) UserResponse
    }
    class IntervalService {
        <<interface>>
        create(long activityId, IntervalRequest requestBody) IntervalResponse
        find(long activityId, Pageable pageable) Page~IntervalResponse~
        delete(long activityId, long intervalId) void
    }
    class ActivityService {
        <<interface>>
        find(Pageable pageable) Page~ActivityResponse~
        findById(long id) Activity
        create(ActivityRequest activityRequest) ActivityResponse
        update(long id, ActivityRequest activityRequest) void
        deleteActivityById(long id) void
        updateStatusById(long id, UpdateStatusRequest updateStatusRequest) void
    }
    class SigninService {
        <<interface>>
        signin(LoginRequest loginRequest) TokenResponse
    }
    class TokenService {
        <<interface>>
    }

    class ModelMapper {
        <<interface>>
    }
    class AuthenticationManager {
        <<interface>>
    }

    class UserServiceImpl {
        -UserRepository userRepository
        -PasswordEncoder passwordEncoder
        -ModelMapper mapper
        +findByUsername(String username) UserDetails
        +findUserDetailsById(long id) UserDetails
        +save(SignupRequest signupRequest) UserResponse
        -getRole() Role
    }
    class SigninServiceImpl {
        -AuthenticationManager authManager
        -TokenService tokenService
        +signin(LoginRequest loginRequest) TokenResponse
    }
    class ActivityServiceImpl {
        -ActivityRepository activityRepository
        +find(Pageable pageable) Page~ActivityResponse~
        +findById(long id) Activity
        +create(ActivityRequest activityRequest) ActivityResponse
        +update(long id, ActivityRequest activityRequest) void
        +deleteActivityById(long id) void
        +updateStatusById(long id, UpdateStatusRequest updateStatusRequest) void
        -findByIdAndNotDeleted(long id) Activity
        -getCurrentUser() User
    }
    class IntervalServiceImpl {
        -ActivityService activityService
        -IntervalRepository intervalRepository
        +create(long activityId, IntervalRequest requestBody) IntervalResponse
        +find(long activityId, Pageable pageable) Page~IntervalResponse~
        +delete(long activityId, long intervalId) void
        -findById(long id) Interval
    }

    class ActivityRepository {
        <<interface>>
    }
    class UserRepository {
        <<interface>>
    }
    class IntervalRepository {
        <<interface>>
    }

    UserServiceImpl ..|> UserService
    UserServiceImpl --> UserRepository
    UserServiceImpl --> ModelMapper
    SigninServiceImpl ..|> SigninService
    SigninServiceImpl --> AuthenticationManager
    SigninServiceImpl --> TokenService
    ActivityServiceImpl ..|> ActivityService
    ActivityServiceImpl --> ActivityRepository
    IntervalServiceImpl ..|> IntervalService
    IntervalServiceImpl --> ActivityService
    IntervalServiceImpl --> IntervalRepository
```

## :toolbox: Tecnologias e ferramentas

<a href="https://www.jetbrains.com/idea/" target="_blank"><img src="https://img.shields.io/badge/intellij-000000.svg?&style=for-the-badge&logo=intellijidea&logoColor=white" target="_blank"></a>

<a href="https://pt.wikipedia.org/wiki/Java_(linguagem_de_programa%C3%A7%C3%A3o)" target="_blank"><img src="https://img.shields.io/badge/java%2017-D32323.svg?&style=for-the-badge&logo=java&logoColor=white" target="_blank"></a>

<a href="https://spring.io/projects/spring-boot" target="_blank"><img src="https://img.shields.io/badge/Springboot-6db33f.svg?&style=for-the-badge&logo=springboot&logoColor=white" target="_blank"></a>
<a href="https://spring.io/projects/spring-data-jpa" target="_blank"><img src="https://img.shields.io/badge/Spring%20Data%20JPA-6db33f.svg?&style=for-the-badge&logo=spring&logoColor=white" target="_blank"></a>
<a href="https://spring.io/projects/spring-security" target="_blank"><img src="https://img.shields.io/badge/Spring%20Security-6db33f.svg?&style=for-the-badge&logo=spring&logoColor=white" target="_blank"></a>

<a href="https://maven.apache.org/" target="_blank"><img src="https://img.shields.io/badge/Apache%20Maven-b8062e.svg?&style=for-the-badge&logo=apachemaven&logoColor=white" target="_blank"></a>

<a href="https://tomcat.apache.org/" target="_blank"><img src="https://img.shields.io/badge/Apache%20Tomcat-F8DC75.svg?&style=for-the-badge&logo=apachetomcat&logoColor=black" target="_blank"></a>

<a href="https://www.docker.com/" target="_blank"><img src="https://img.shields.io/badge/Docker-2496ED.svg?&style=for-the-badge&logo=docker&logoColor=white" target="_blank"></a>
<a href="https://www.postgresql.org/" target="_blank"><img src="https://img.shields.io/badge/PostgreSQL-4169E1.svg?&style=for-the-badge&logo=postgresql&logoColor=white" target="_blank"></a>
<a href="https://flywaydb.org/" target="_blank"><img src="https://img.shields.io/badge/Flyway-CC0200.svg?&style=for-the-badge&logo=flyway&logoColor=white" target="_blank"></a>

<a href="https://projectlombok.org/" target="_blank"><img src="https://img.shields.io/badge/Lombok-a4a4a4.svg?&style=for-the-badge&logo=lombok&logoColor=black" target="_blank"></a>
<a href="https://github.com/jwtk/jjwt" target="_blank"><img src="https://img.shields.io/badge/JJWT-a4a4a4.svg?&style=for-the-badge&logo=JJWT&logoColor=black" target="_blank"></a>

<a href="https://swagger.io/" target="_blank"><img src="https://img.shields.io/badge/Swagger-85EA2D.svg?&style=for-the-badge&logo=swagger&logoColor=black" target="_blank"></a>
<a href="https://springdoc.org/" target="_blank"><img src="https://img.shields.io/badge/Spring%20Doc-85EA2D.svg?&style=for-the-badge" target="_blank"></a>

<a href="https://junit.org/junit5/" target="_blank"><img src="https://img.shields.io/badge/JUnit%205-25A162.svg?&style=for-the-badge&logo=junit5&logoColor=white" target="_blank"></a>
<a href="https://site.mockito.org/" target="_blank"><img src="https://img.shields.io/badge/Mockito-C5D9C8.svg?&style=for-the-badge" target="_blank"></a>
<a href="https://www.testcontainers.org/" target="_blank"><img src="https://img.shields.io/badge/TestContainers-291A3F.svg?&style=for-the-badge&logo=testcontainers&logoColor=white" target="_blank"></a>
<a href="https://www.postman.com/" target="_blank"><img src="https://img.shields.io/badge/postman-ff6c37.svg?&style=for-the-badge&logo=postman&logoColor=white" target="_blank"></a>
<a href="https://en.wikipedia.org/wiki/Unit_testing" target="_blank"><img src="https://img.shields.io/badge/Unit%20Tests-5a61d6.svg?&style=for-the-badge&logo=unittest&logoColor=white" target="_blank"></a>
<a href="https://en.wikipedia.org/wiki/Integration_testing" target="_blank"><img src="https://img.shields.io/badge/Integration%20Tests-5a61d6.svg?&style=for-the-badge&logo=unittest&logoColor=white" target="_blank"></a>
