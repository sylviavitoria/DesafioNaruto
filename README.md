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

## 🌸 Endpoints da API
## 1. 🥋 Autenticação de usuários na aplicação
`POST` `http://localhost:8080/api/v1/auth/login`
```
{
    "username": "admin",
    "password": "admin123"
}
```

## 2. 🐱‍👤 Criar um novo personagem
`POST` `http://localhost:8080/api/v1/personagens`
```
{
    "tipoNinja": "NINJUTSU",
    "nome": "Naruto Uzumaki",
    "idade": 17,
    "aldeia": "Aldeia da Folha",
    "chakra": 1000,
    "jutsus": [
        "Chidori",
        "Sharingan"
    ]
}
```
## 3. 🌀 Listagens
| Método | URL                           | Descrição                     |
|--------|-------------------------------|--------------------------------|
| `GET`  | `http://localhost:8080/api/v1/personagens`         | Lista todos os personagens.    |
| `GET`  | `http://localhost:8080/api/v1/personagens/{id}`    | Busca um personagem pelo ID.   |
| `GET`  | `http://localhost:8080/api/v1/personagens/{id}/usar-jutsu` | Irá realizar jutsu.   |
| `GET`  | `http://localhost:8080/api/v1/personagens/{id}/desviar` | Irá realizar desviar.   |


## 4. 💨 Atualiza dados do personagem
`PUT`  `http://localhost:8080/api/v1/personagens/{id}`
```
{
    "tipoNinja": "NINJUTSU",
    "nome": "sasuke uchiha",
    "idade": 16,
    "aldeia": "Aldeia da Folha",
    "chakra": 100,
    "jutsus": [
        "Chidori",
        "Sharingan"
    ]
}
```

## 5. 💥  Remove dados do personagem
`DELETE`  `http://localhost:8080/api/v1/personagens/{id}`

---

## 🌪 Configuração de Ambientes

### Autenticação JWT

Antes de acessar os endpoints protegidos:

1. Faça login usando:
`POST` `http://localhost:8080/api/v1/personagens`
```
{
    "username": "admin",
    "password": "admin123"
}
```

2. Utilize o token retornado em todas as requisições protegidas no cabeçalho:

```
Authorization: Bearer {token}
```

Exemplo Postman:

- Aba Authorization.
- Auth Type: Bearer Token.
- Cole o token obtido.

3. Funcionamento:
Quando o usuário envia essas informações, o sistema verifica se as credenciais estão corretas e, caso estejam, normalmente retorna um token de autenticação que será utilizado para autorizar o acesso aos demais endpoints protegidos da aplicação.
---

### 👁️ Configuração `.env`
Para facilitar a configuração do banco de dados e evitar informações sensíveis no código, crie um arquivo .env na raiz do projeto com o seguinte conteúdo:
```bash
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
POSTGRES_DB=naruto
POSTGRES_PORT=5432

SPRING_PROFILES_ACTIVE=postgres
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/${POSTGRES_DB}
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres

PGADMIN_DEFAULT_EMAIL=admin@example.com
PGADMIN_DEFAULT_PASSWORD=admin
PGADMIN_PORT=5050
```

Esse arquivo .env é utilizado para centralizar as variáveis de ambiente da aplicação, deixando sua configuração mais simples e segura, especialmente quando for rodar via Docker ou em ambientes diferentes.

🐳 Observação:
- O Docker Compose utiliza essas variáveis para subir os containers do PostgreSQL e PgAdmin com as credenciais corretas.
- O Spring Boot também lerá essas variáveis ao inicializar, garantindo que a conexão com o banco seja automática conforme o ambiente.

---

## 🌬️ Como Executar

1. Clone o repositório:

```bash
git clone https://github.com/sylviavitoria/DesafioNaruto.git
```

2. Configure o `application.yml` ou `.env` com seu banco de dados.

3. Para executar via Maven:

```bash
mvn spring-boot:run
```

Ou via Docker:

```bash
docker compose up --build
```

4. Acesse:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- H2 Console: `http://localhost:8080/h2-console` *(se usar H2)*

---


