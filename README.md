# ServiceHub API

API REST desenvolvida em **Spring Boot** para gerenciamento de um sistema de prestação de serviços, contemplando o cadastro de usuários, a criação de solicitações de serviço e o registro de avaliações sobre serviços concluídos.

Projeto acadêmico desenvolvido como parte da disciplina de Desenvolvimento de APIs / Governança de TI.

## 🎯 Objetivo

Aplicar na prática os princípios de arquitetura REST, persistência de dados e documentação profissional de APIs, construindo uma solução de backend completa, em camadas, seguindo boas práticas de mercado.

## 🛠️ Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.3.4**
- **Spring Web** — construção dos endpoints REST
- **Spring Data JPA** — persistência de dados
- **PostgreSQL** — banco de dados relacional
- **Lombok** — redução de código repetitivo (getters, setters, etc.)
- **SpringDoc OpenAPI (Swagger UI)** — documentação interativa da API
- **Maven** — gerenciamento de dependências e build

## 📦 Estrutura do Projeto

```
com.servicehub.servicehub_api
├── controller     # Camada de entrada HTTP (recebe requisições e retorna respostas)
├── service        # Regras de negócio da aplicação
├── repository     # Acesso e persistência de dados (Spring Data JPA)
├── model          # Entidades JPA (Usuario, ServiceRequest, Review)
└── exception      # Tratamento centralizado de exceções (@ControllerAdvice)
```

## 📋 Recursos (Entidades)

| Recurso | Descrição | Relacionamento |
|---|---|---|
| **Usuario** | Pessoas cadastradas na plataforma | — |
| **ServiceRequest** | Solicitações de serviço abertas por um usuário | `@ManyToOne` com Usuario |
| **Review** | Avaliação de uma solicitação de serviço concluída | `@OneToOne` com ServiceRequest |

## 🚀 Endpoints Principais

Todos os recursos seguem o mesmo padrão de CRUD REST:

| Método | Endpoint | Descrição | Status de sucesso |
|---|---|---|---|
| GET | `/api/users` | Lista todos os usuários | 200 |
| GET | `/api/users/{id}` | Busca um usuário pelo ID | 200 / 404 |
| POST | `/api/users` | Cria um novo usuário | 201 |
| PUT | `/api/users/{id}` | Atualiza um usuário existente | 200 / 404 |
| DELETE | `/api/users/{id}` | Remove um usuário | 204 |

O mesmo padrão se repete em `/api/service-requests` e `/api/reviews`.

## ⚠️ Tratamento de Erros

A API possui um manipulador global de exceções (`GlobalExceptionHandler`) que padroniza as respostas de erro:

- **404 Not Found** — quando o recurso solicitado não existe
- **400 Bad Request** — quando os dados enviados são inválidos

Exemplo de resposta de erro:
```json
{
  "status": 404,
  "mensagem": "Usuário não encontrado",
  "timestamp": "2026-09-22T10:15:30"
}
```

## 📖 Documentação da API (Swagger)

Com a aplicação rodando, a documentação interativa está disponível em:

```
http://localhost:8080/swagger-ui.html
```

Todos os endpoints estão documentados com `@Operation`, `@ApiResponse` e `@Schema`, incluindo descrições e exemplos de payload.

## ▶️ Como Executar o Projeto

### Pré-requisitos
- JDK 17
- Maven (ou usar o `mvnw` incluso no projeto)
- PostgreSQL instalado e rodando

### Passos

1. Clone o repositório:
   ```bash
   git clone <url-do-repositorio>
   cd servicehub-api
   ```

2. Crie o banco de dados no PostgreSQL:
   ```sql
   CREATE DATABASE servicehub_db;
   ```

3. Configure as credenciais do banco em `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/servicehub_db
   spring.datasource.username=postgres
   spring.datasource.password=SUA_SENHA_AQUI
   ```

4. Execute a aplicação:
   ```bash
   ./mvnw spring-boot:run
   ```

5. Acesse a documentação em `http://localhost:8080/swagger-ui.html`

## ✅ Status do Projeto

- [x] Configuração inicial do projeto (Spring Initializr)
- [x] Arquitetura em camadas (Controller, Service, Repository, Model)
- [x] Persistência com Spring Data JPA + PostgreSQL
- [x] Modelagem das entidades e relacionamentos
- [x] CRUD completo implementado e testado
- [x] Tratamento de erros (404, 400) com `@ControllerAdvice`
- [x] Documentação interativa via Swagger/OpenAPI
- [ ] Autenticação e autorização com Spring Security + JWT (próxima etapa)

## 👤 Autor

Murillo — Estudante de Engenharia de Software
