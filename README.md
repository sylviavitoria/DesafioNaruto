# 🍜 Exercício de Programação Orientada a Objetos (POO) em Java Spring Boot - Anime Naruto

## 🍃 Descrição Geral

Este projeto consiste em uma aplicação Java que explora os conceitos de Programação Orientada a Objetos (POO) através do anime Naruto, além disso o desafio propõe a criação de uma API utilizando Spring Boot, que permite realizar um CRUD completo sobre personagens 

O objetivo é unir teoria e prática em uma solução que implemente:
- Conceitos de POO: Classes, Interfaces, Herança e Polimorfismo.

---

## 🍣 Tecnologias Utilizadas

| Ferramenta         | Finalidade                          |
|---------------------|-------------------------------------|
| Java 21 LTS         | Linguagem principal.                |
| Spring Boot         | Framework para API REST.           |
| Spring Security     | Controle de autenticação.          |
| Spring Data JPA     | Acesso ao banco de dados.          |
| JWT                 | Autenticação e autorização.        |
| Swagger OpenAPI     | Documentação automática.           |
| Flyway              | Controle de versionamento do banco.|
| Docker              | Contêinerização da aplicação.      |
| PostgreSQL / H2      | Banco de dados relacional.         |

---

## 🦊 Parte 1: Programação Orientada a Objetos (POO) - Naruto

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


## 🍡 Parte 2: Desenvolvimento da API

### Recursos
A API deverá expor um CRUD completo para personagens (`Personagem`).

### 🌸 Endpoints Padrão

| Método | URL                           | Descrição                     |
|--------|-------------------------------|--------------------------------|
| `GET`  | `http://localhost:8080/api/v1/personagens/api/v1/personagens`         | Lista todos os personagens.    |
| `POST` | `http://localhost:8080/api/v1/personagens/api/v1/personagens`         | Cria um novo personagem.       |
| `GET`  | `http://localhost:8080/api/v1/personagens/api/v1/personagens/{id}`    | Busca um personagem pelo ID.   |
| `GET`  | `http://localhost:8080/api/v1/personagens/{id}/usar-jutsu`| Irá realizar jutsu.   |
| `GET`  | `http://localhost:8080/api/v1/personagens/{id}/desviar`| Irá realizar desviar.   |
| `PUT`  | `http://localhost:8080/api/v1/personagens/api/v1/personagens/{id}`    | Atualiza dados do personagem.  |
| `DELETE` | `http://localhost:8080/api/v1/personagens/api/v1/personagens/{id}`  | Remove um personagem.          |

---

### 🍙 Padrões e Boas Práticas

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
    docker compose up --build
    ```

- **Flyway**
  - Controle de versão de banco de dados com `Flyway`.
  - Scripts SQL versionados em:
    ```
    src/main/resources/db/migration/
    ```


---


## 🏮 Como Executar

1. Clone o repositório:
    ```bash
    git clone https://github.com/sylviavitoria/DesafioNaruto.git

    
2. Configure o `application.yml` com seu banco de dados.
3. Rode a aplicação com:
    ```bash
    mvn spring-boot:run
    ```
4. Acesse:
    - Swagger: `http://localhost:8080/swagger-ui.html`
    - H2 Console: `http://localhost:8080/h2-console` *(caso use H2)*

---



