#  Garage Service

Projeto desenvolvido como parte do desafio Estapar.  
Sistema backend para gerenciar estacionamento, controlando vagas, entrada/saída de veículos e cálculo de receita.

---

##  Stack de Tecnologias

- **Java 21**
- **Spring Boot 3**
    - Spring Web
    - Spring Data JPA
    - Spring Validation
    - Spring Actuator
- **PostgreSQL 16**
- **Flyway** (migrações de banco)
- **MapStruct** (mapeamento DTO <-> Entidade)
- **Lombok**
- **Springdoc OpenAPI** (Swagger UI)
- **Docker Compose** (infra e app)
- **JUnit 5 + Mockito** (testes unitários)

---

## Estrutura do Projeto

```
garage-service
├── src
│   ├── main
│   │   ├── java/com/breno/garage_service
│   │   │   ├── controller   -> Controllers REST
│   │   │   ├── dto          -> Data Transfer Objects
│   │   │   ├── entity       -> Entidades JPA
│   │   │   ├── enumerate    -> Enums
│   │   │   ├── exception    -> Exceptions globais
│   │   │   ├── mapper       -> MapStruct mappers
│   │   │   ├── repository   -> Repositórios JPA
│   │   │   └── service      -> Regras de negócio
│   │   └── resources
│   │       ├── application.yml -> Configurações
│   │       └── db/migration   -> Scripts Flyway
│   └── test/java/com/breno/garage_service
│       ├── service         -> Testes unitários dos serviços
│       └── util/TestUtils  -> Objetos mock para testes
├── Dockerfile
├── docker-compose.yml
└── README.md
```

---

## Como Rodar

### Pré-requisitos
- **Docker** e **Docker Compose** instalados

### Subindo a aplicação

```bash
docker compose up --build
```

- PostgreSQL: porta **5432**
- Simulador Estapar: porta **3000**
- Garage Service: porta **3003**

---

## Documentação da API

Após subir o projeto, acessar:

👉 [http://localhost:3003/swagger-ui/index.html](http://localhost:3003/swagger-ui/index.html)

---

## Testes

Rodar testes unitários com:

```bash
mvn test
```

- Framework: **JUnit 5**
- Mocking: **Mockito**
- Classe utilitária: `TestUtils` para objetos mockados.

---

## Endpoints Principais

- **POST** `/webhook` → Recebe eventos `ENTRY`, `PARKED`, `EXIT`
- **GET** `/spot-status/{spotId}` → Consulta status da vaga
- **GET** `/revenue` → Calcula receita por setor/período

---

## Autor

**Breno Delgado**  
Desenvolvedor Fullstack Java
