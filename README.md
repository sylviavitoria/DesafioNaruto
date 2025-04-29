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

# 🦊 Parte 1: Programação Orientada a Objetos (POO) - Naruto

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
## ⚔️ Ações Ninja: Usar Jutsu & Desviar

Durante o desenvolvimento, além de atender ao requisito de criar um método `GET` que retorna uma mensagem personalizada ao chamar `usarJutsu()` ou `desviar()`, também foi implementado o comportamento no console através da execução dos métodos das classes.

### 💥 Usar Jutsu
Exemplo:
```
mensagem = personagem.getNome() + " está usando um golpe de Taijutsu!";
ninja.usarJutsu();
```
- A resposta da API retorna um JSON com as informações.
- E o console exibe a mensagem do método `usarJutsu()` da classe específica.

---

### 💨 Desviar
Exemplo:
```
mensagem = personagem.getNome() + " está desviando usando suas habilidades de Taijutsu!";
ninja.desviar();
```
- O endpoint retorna o JSON com o nome e a descrição.
- E no console aparece a mensagem personalizada do método `desviar()`.

---

💡 **Resumo:**
O método `usarJutsu` foi implementado para buscar um personagem pelo ID e definir a mensagem conforme o tipo de ninja, usando o polimorfismo.  
A chamada `ninja.usarJutsu();` ou `ninja.desviar();` não apenas executa a lógica no back-end, como também imprime no console.

---
# 🍥 Parte 2: Sistema de Batalhas

### Classe Personagem:
- **Atributos:**
  - `nome` (String)
  - `idade` (int)
  - `aldeia` (String)
  - `jutsusMap` (Map<String, Jutsu>)
  - `chakra` (int = 100)
  - `vida` (int = 100)

- **Métodos:**
  - **adicionarJutsu(String nomeJutsu, int dano, int consumoDeChakra):** Adiciona um novo jutsu ao mapa.
  - **diminuirChakra(int quantidade):** Reduz o chakra do personagem.
  - **receberDano(int dano):** Reduz a vida do personagem.
  - **podeLutar():** Verifica se o personagem tem vida e chakra suficientes.

---

### Interface Ninja
- **Métodos:**
  - **usarJutsu(String nomeJutsu, Personagem oponente):** Usa um jutsu específico contra um oponente.
  - **desviar(String jutsuRecebido, int danoPotencial):** Tenta desviar de um ataque específico.
  - **podeLutar():** Verifica se o ninja pode continuar lutando.

---

### Classes Específicas

#### NinjaDeTaijutsu
- Especialista em Taijutsu (combate físico).
- **usarJutsu():** Mensagem específica para Taijutsu.
- **desviar():** Mensagem específica para esquiva com Taijutsu.
- **Alto potencial de esquiva:** 60% de chance.

#### NinjaDeNinjutsu
- Especialista em Ninjutsu (técnicas de chakra).
- **usarJutsu():** Mensagem específica para Ninjutsu.
- **desviar():** Mensagem específica para esquiva com Ninjutsu.
- **Chance média de esquiva:** 40% de chance.

#### NinjaDeGenjutsu
- Especialista em Genjutsu (ilusões).
- **usarJutsu():** Mensagem específica para Genjutsu.
- **desviar():** Mensagem específica para esquiva com Genjutsu.
- **Chance moderada de esquiva:** 30% de chance.

---

### Classe Jutsu
- Representa as técnicas que os ninjas podem utilizar.
- **Atributos:**
  - `dano`: Valor do dano que o jutsu causa.
  - `consumoDeChakra`: Quantidade de chakra necessária para usar o jutsu.
- **Método:**
  - **calcularDanoReal():** Calcula o dano efetivo com base em um modificador.

---

### Regras de Batalha

1. **Condições para Lutar:**
   - Um ninja só pode atacar ou defender se tiver `chakra > 0` e `vida > 0`.
   - Cada ataque consome 10 pontos de chakra.

2. **Ataques:**
   - Cada ninja possui jutsus específicos armazenados em um `Map`.
   - Ao usar um jutsu, o ninja causa dano ao oponente e consome chakra.
   - Se o ninja não conhece o jutsu ou não tem chakra suficiente, o ataque falha.

3. **Defesa:**
   - Ao receber um ataque, o ninja tenta desviar.
   - A chance de desvio varia conforme a especialização:
     - Taijutsu: 60% de chance.
     - Ninjutsu: 40% de chance.
     - Genjutsu: 30% de chance.
   - Se conseguir desviar, não recebe dano.
   - Caso contrário, recebe o dano completo do jutsu.

4. **Fim da Batalha:**
   - Um ninja perde quando:
     - Sua vida chega a 0.
     - Seu chakra chega a 0.
---


# 🍡 Parte 2: Desenvolvimento da API

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
    "tipoNinja": "GENJUTSU",
    "nome": "sasuke",
    "idade": 17,
    "aldeia": "Aldeia da Folha",
    "chakra": 100,
    "jutsus": {
        "RAZENGAN": {
            "dano": 70,
            "consumoDeChakra": 30
        },
        "Kage Bunshin": {
            "dano": 40,
            "consumoDeChakra": 20
        }
    }
}
```
## 3. 🌀 Listagens
| Método | URL                           | Descrição                     |
|--------|-------------------------------|--------------------------------|
| `GET`  | `http://localhost:8080/api/v1/personagens`         | Lista todos os personagens.    |
| `GET`  | `http://localhost:8080/api/v1/personagens/{id}`    | Busca um personagem pelo ID.   |
| `GET`  | `http://localhost:8080/api/v1/personagens/{id}/jutsus` | Irá listar todos os jutsus do personagem. |

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
## 5. 🌀 Adicionar Jutsu a um Personagem
🌀 `POST` `http://localhost:8080/api/v1/personagens` 
```
{
    "nome": "Sabio",
    "dano": 70,
    "consumoDeChakra": 30
}
```

## 6. 💥  Remove dados do personagem
`DELETE`  `http://localhost:8080/api/v1/personagens/{id}`


# 7. ⚔️ Sistema de Batalhas
Este endpoint permite que um personagem ataque outro usando um jutsu específico.
`POST` `http://localhost:8080/api/v1/batalhas/atacar/{atacanteId}/{defensorId}?nomeJutsu=Rasengan`

1. Parâmetros:

- atacanteId: ID do personagem que realizará o ataque.
- defensorId: ID do personagem que receberá o ataque.
- nomeJutsu: Nome do jutsu que será utilizado (parâmetro query).


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

# 🌬️ Como Executar

## Pré-requisitos
- Java 21+
- Maven
- Docker
- PostgreSQL ou H2 Database

## Passos para Execução

### 1. Clone o repositório
```bash
git clone https://github.com/sylviavitoria/DesafioNaruto.git
```

### 2. Configure o ambiente
Ajuste as configurações de banco de dados em um dos arquivos:
- Configure suas credencias citado anteriormente e defina variáveis no arquivo `.env`

### 3. Execute a aplicação

**Opção 1: Via Maven**
```bash
mvn spring-boot:run
```

**Opção 2: Via Docker**
```bash
docker compose up --build
```

## 4. Acesse a Aplicação

### Documentação da API
- **Swagger UI:** `http://localhost:8080/swagger-ui.html`
  
  ![Interface do Swagger](https://github.com/user-attachments/assets/89f89dca-a858-45ba-81f7-f55b68de2a3e)

### Teste dos Endpoints
- **Postman:**:  Adicione os Endpoints da API mencionados anteriormente `http://localhost:8080/api/v1/...`

### Gerenciamento do Banco de Dados
- **H2 Database** (para ambiente de desenvolvimento):
  - Console: `http://localhost:8080/h2-console`

- **PostgreSQL** (recomendado para ambiente Docker):
  - **pgAdmin:** `http://localhost:5050`
  - Credenciais de login: Definidas no arquivo `.env`

  


