# Desafio Java spring Boot - Naruto

## Descrição Geral

Este projeto consiste em uma aplicação Java que explora os conceitos de Programação Orientada a Objetos (POO) através do universo do anime **Naruto** e a criação de uma **API ** utilizando **Spring Boot**.

O objetivo é unir teoria e prática em uma solução que implemente:
- Conceitos de POO: Classes, Interfaces, Herança e Polimorfismo.

---

## 💡 Parte 1: Programação Orientada a Objetos (POO) - Naruto

### Requisitos

**Classe `Personagem`:**
- `nome` (String)
- `idade` (int)
- `aldeia` (String)
- `jutsus` (Array de Strings)
- `chakra` (int)

**Métodos:**
- Construtor para inicializar os atributos.
- `adicionarJutsu(String jutsu)`: adiciona um novo jutsu ao array.
- `aumentarChakra(int quantidade)`: aumenta o chakra do personagem.
- `exibirInformacoes()`: exibe todos os dados do personagem.

---

### Interface `Ninja`

**Métodos:**
- `usarJutsu()`: imprime uma mensagem sobre o uso do jutsu.
- `desviar()`: imprime uma mensagem sobre desviar de um ataque.

---

### Classes Específicas

- `NinjaDeTaijutsu`  
Especialista em Taijutsu.
  - `usarJutsu()`: Mensagem específica para Taijutsu.
  - `desviar()`: Mensagem específica para esquiva com Taijutsu.

- `NinjaDeNinjutsu`  
Especialista em Ninjutsu.
  - `usarJutsu()`: Mensagem específica para Ninjutsu.
  - `desviar()`: Mensagem específica para esquiva com Ninjutsu.

- `NinjaDeGenjutsu`  
Especialista em Genjutsu.
  - `usarJutsu()`: Mensagem específica para Genjutsu.
  - `desviar()`: Mensagem específica para esquiva com Genjutsu.

---

## ⚙️ Parte 2: Desenvolvimento da API RESTful

### Recursos
A API deverá expor um CRUD completo para personagens (`Personagem`).

### Endpoints Exemplo:
```
GET    /api/v1/personagens
POST   /api/v1/personagens
GET    /api/v1/personagens/{id}
PUT    /api/v1/personagens/{id}
DELETE /api/v1/personagens/{id}
```

---

### ✅ Padrões e Boas Práticas

- **Documentação com Swagger**
  - Interface automática de teste via `Swagger UI` (http://localhost:8080/swagger-ui.html).

- **Versionamento de API**
  - URL com padrão `/api/v1/` para controle de versões.

- **Autenticação e Autorização com JWT**
  - Segurança usando `JSON Web Token` para autenticação de usuários.

- **Paginação nas Listagens**
  - Listagem paginada com `Pageable`:
    ```
    GET /api/v1/personagens?page=0&size=10
    ```

- **Logs**
  - Registro de eventos importantes, erros e fluxos de execução.

- **Dockerização**
  - Dockerfile configurado para build e deploy.
  - Comando padrão:
    ```
  
    ```

- **Flyway**
  - Controle de versão de banco de dados com `Flyway`.
  - Scripts SQL versionados em:
    ```
    src/main/resources/db/migration/
    ```


---

## 💻 Tecnologias Utilizadas

- Java 21LTS
- Spring Boot
- Spring Security
- Spring Data JPA
- Flyway
- Swagger OpenAPI
- JWT
- Docker
- PostgreSQL ou H2 Database

---

## 🚀 Como Executar

1. Clone o repositório.
2. Configure o `application.yml` com seu banco de dados.
3. Rode a aplicação com:
    ```bash
    ./mvnw spring-boot:run
    ```
4. Acesse:
    - Swagger: `http://localhost:8080/swagger-ui.html`
    - H2 Console: `http://localhost:8080/h2-console` *(caso use H2)*

---



