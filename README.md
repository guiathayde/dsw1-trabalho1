# Grupo 8

Guilherme César Athayde - RA: 748175

Requisito: B1 - Sistema para oferta de vagas de estágios/empregos

## Sumário

- [Sistema de Oferta de Vagas de Estágios/Empregos](#sistema-de-oferta-de-vagas-de-estágiosempregos)
- [Usuários Testes](#usuários-testes)
- [Requisitos Implementados](#requisitos-implementados)
  - [R1: CRUD de profissionais](#r1-crud-de-profissionais-requer-login-de-administrador)
  - [R2: CRUD de empresas](#r2-crud-de-empresas-requer-login-de-administrador)
  - [R3: Cadastro de vagas](#r3-cadastro-de-vagas-de-estágiotrabalho-requer-login-da-empresa-via-e-mail--senha)
  - [R4: Listagem de vagas](#r4-listagem-de-todas-as-vagas-em-aberto-em-uma-única-página-não-requer-login)
  - [R5: Candidatura a vagas](#r5-candidatura-a-vaga-de-estágiotrabalho-requer-login-do-profissional-via-e-mail--senha)
  - [R6: Vagas por empresa](#r6-listagem-de-todas-as-vagas-de-uma-empresa-requer-login-da-empresa-via-e-mail--senha)
  - [R7: Candidaturas por profissional](#r7-listagem-de-todas-as-candidaturas-de-um-profissional-requer-login-do-profissional-via-email--senha)
  - [R8: Análise de candidaturas](#r8-ao-término-do-período-de-inscrição-inicia-se-a-fase-de-análise)
  - [R9: Internacionalização](#r9-o-sistema-deve-ser-internacionalizado-em-pelo-menos-dois-idiomas-português--outro-de-sua-escolha)
  - [R10: Validações](#r10-o-sistema-deve-validar-tamanho-formato-etc-todas-as-informações-campos-nos-formulários-cadastradas-eou-editadas)
- [Como Executar](#como-executar)

# Sistema de Oferta de Vagas de Estágios/Empregos

Este projeto implementa um sistema de oferta de vagas de estágios e empregos com APIs RESTful para gerenciamento de profissionais, empresas e vagas. As páginas web existentes foram refatoradas para consumir essas APIs.

## Usuários Testes

Podem ser encontrados em `src/main/java/br/ufscar/dc/dsw/LivrariaMvcApplication.java`.

## Requisitos Implementados

### R1: CRUD de profissionais (requer login de administrador)

As operações de CRUD para profissionais são expostas através da API REST e consumidas pelas páginas web.

- **Cria um novo profissional**

  - `POST http://localhost:8080/api/profissionais`
  - **Body (JSON):**
    ```json
    {
      "username": "prof4@work.com",
      "password": "prof4@work.com",
      "name": "Profissional 4",
      "enabled": true,
      "CPF": "444.444.444-44",
      "telefone": "(44) 4444-4444",
      "sexo": "F",
      "dataNascimento": "1994-04-04"
    }
    ```

- **Retorna a lista de profissionais**

  - `GET http://localhost:8080/api/profissionais`

- **Retorna o profissional de id = {id}**

  - `GET http://localhost:8080/api/profissionais/{id}`

- **Atualiza o profissional de id = {id}**

  - `PUT http://localhost:8080/api/profissionais/{id}`
  - **Body (JSON):**
    ```json
    {
      "username": "prof4@work.com",
      "password": "prof4@work.com",
      "name": "Profissional 44",
      "enabled": true,
      "CPF": "444.444.444-44",
      "telefone": "(44) 4444-4444",
      "sexo": "F",
      "dataNascimento": "1994-04-04"
    }
    ```

- **Remove o profissional de id = {id}**
  - `DELETE http://localhost:8080/api/profissionais/{id}`

### R2: CRUD de empresas (requer login de administrador)

As operações de CRUD para empresas são expostas através da API REST e consumidas pelas páginas web.

- **Cria uma nova empresa**

  - `POST http://localhost:8080/api/empresas`
  - **Body (JSON):**
    ```json
    {
      "username": "emp4@work.com",
      "password": "emp4@work.com",
      "name": "Empresa 4",
      "cnpj": "44.444.444/4444-44",
      "descricao": "Empresa 4",
      "cidade": "Araraquara"
    }
    ```

- **Retorna a lista de empresas**

  - `GET http://localhost:8080/api/empresas`

- **Retorna a empresa de id = {id}**

  - `GET http://localhost:8080/api/empresas/{id}`

- **Retorna a lista de todas as empresas da cidade de nome = {nome}**

  - `GET http://localhost:8080/api/empresas/cidades/{nome}`

- **Atualiza a empresa de id = {id}**

  - `PUT http://localhost:8080/api/empresas/{id}`
  - **Body (JSON):**
    ```json
    {
      "username": "emp4@work.com",
      "password": "emp4@work.com",
      "name": "Empresa 44",
      "cnpj": "44.444.444/4444-44",
      "descricao": "Empresa 4",
      "cidade": "Araraquara"
    }
    ```

- **Remove a empresa de id = {id}**
  - `DELETE http://localhost:8080/api/empresas/{id}`

### R3: Cadastro de vagas de estágio/trabalho (requer login da empresa via e-mail + senha)

O cadastro de vagas é realizado através da página web, que agora utiliza a API REST para salvar os dados.

- **Cria uma nova vaga**
  - `POST http://localhost:8080/api/vagas`
  - **Body (JSON):** (Exemplo, a empresa logada é associada automaticamente)
    ```json
    {
      "descricao": "Desenvolvedor Java Junior",
      "remuneracao": 2500.0,
      "dataLimiteInscricao": "2025-12-31"
    }
    ```

### R4: Listagem de todas as vagas (em aberto) em uma única página (não requer login)

As vagas são listadas através da API REST. A funcionalidade de filtro por cidade está disponível.

- **Retorna a lista de vagas**

  - `GET http://localhost:8080/api/vagas`

- **Retorna a vaga de id = {id}**

  - `GET http://localhost:8080/api/vagas/{id}`

- **Retorna a lista de vagas (em aberto) da empresa de id = {id}**
  - `GET http://localhost:8080/api/vagas/empresas/{id}`

### R5: Candidatura a vaga de estágio/trabalho (requer login do profissional via e-mail + senha)

O processo de candidatura envolve a interação com a API REST de candidaturas.

- **Listar candidaturas por vaga**
  - `GET http://localhost:8080/api/candidaturas/vagas/{id}`

### R6: Listagem de todas as vagas de uma empresa (requer login da empresa via e-mail + senha)

A listagem das vagas da empresa logada é feita através da API REST.

- **Retorna a lista de vagas (em aberto) da empresa de id = {id}**
  - `GET http://localhost:8080/api/vagas/empresas/{id}`

### R7: Listagem de todas as candidaturas de um profissional (requer login do profissional via email + senha)

Acessar o sistema com um login profissional, e acessar ["Minhas Candidaturas"](http://localhost:8080/profissionais/minhasCandidaturas) no menu lateral.

### R8: Ao término do período de inscrição, inicia-se a fase de análise.

A atualização do status da candidatura e o envio de e-mails são tratados no `VagaController` e utilizam o `ICandidaturaService` e `IEmailService`.

### R9: O sistema deve ser internacionalizado em pelo menos dois idiomas: português + outro de sua escolha.

O sistema já possui suporte a internacionalização via arquivos `.properties`.

### R10: O sistema deve validar (tamanho, formato, etc) todas as informações (campos nos formulários) cadastradas e/ou editadas.

As validações são realizadas através das anotações `@Valid` nas entidades e tratadas pelo `RestExceptionHandler` para as APIs REST, e pelo `BindingResult` nos controladores MVC.

## Como Executar

1.  **Rodar o banco de dados MySQL:**

    ```bash
    docker run --name mysql -e MYSQL_ROOT_PASSWORD=root -p 3306:3306 -d mysql:tag
    ```

2.  **Configurar o email e senha do serviço de e-mail:**
    Acessar `src/main/resources/application.properties` e configurar os campos `spring.mail.username` e `spring.mail.password`.

3.  **Rodar a aplicação Spring Boot:**

    ```bash
    mvnd spring-boot:run
    ```

4.  **Testar as APIs:**
    As APIs REST podem ser testadas usando ferramentas como o [Bruno](https://www.usebruno.com/) (o arquivo de testes está em `bruno`).

5.  **Acessar as páginas web:**
    Após iniciar a aplicação, acesse `http://localhost:8080/` no seu navegador. As páginas de listagem e cadastro de profissionais, empresas e vagas agora consomem as APIs REST.
