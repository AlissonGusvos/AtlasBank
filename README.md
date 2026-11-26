# AtlasBank

API REST de simulação bancária desenvolvida com Java e Spring Boot. O projeto oferece cadastro e autenticação de clientes, consulta de saldo, depósitos, saques, transferências entre contas e consulta de extrato. Os dados são persistidos em MySQL.

## Funcionalidades

- Cadastro de cliente com criação automática de conta.
- Login por CPF e senha, com autenticação baseada em sessão HTTP.
- Consulta de nome e saldo da conta autenticada.
- Depósito, saque e transferência mediante senha da conta.
- Registro e consulta das movimentações da conta.
- Proteção das rotas de conta para usuários autenticados.

## Tecnologias

- Java 27
- MySQL

## Pré-requisitos

- JDK compatível com a versão configurada no projeto (Java 27).
- MySQL em execução e um banco de dados criado para a aplicação.

## Configuração

A aplicação lê as credenciais do banco por variáveis de ambiente. Configure `DB_LINK`, `DB_USERNAME` e `DB_PASSWORD` antes de iniciá-la.

## Executar localmente

Na raiz do projeto, inicie a aplicação com o Maven Wrapper:

```bash
./mvnw spring-boot:run
```

No Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

Por padrão, a API fica disponível em `http://localhost:8080`.

## API

As rotas de autenticação são públicas. As rotas `/account/**` exigem sessão autenticada. Após o login, mantenha e envie o cookie de sessão `JSESSIONID` nas chamadas protegidas. As requisições com corpo devem usar `Content-Type: application/json`.

| Método | Rota | Acesso | Descrição |
|---|---|---|---|
| `POST` | `/auth/register` | Público | Cadastra cliente e cria conta |
| `POST` | `/auth/login` | Público | Autentica por CPF e senha |
| `GET` | `/account/` | Autenticado | Consulta nome e saldo |
| `POST` | `/account/deposit` | Autenticado | Realiza depósito |
| `POST` | `/account/withdraw` | Autenticado | Realiza saque |
| `POST` | `/account/transfer` | Autenticado | Transfere para outra conta |
| `GET` | `/account/extract` | Autenticado | Consulta movimentações da conta |
| `POST` | `/logout` | Autenticado | Encerra a sessão |

### Cadastro

```http
POST /auth/register
Content-Type: application/json
```

```json
{
  "email": "cliente@exemplo.com",
  "cpf": "12345678900",
  "password": "senha-de-acesso",
  "username": "Ana Silva",
  "accPass": "senha-da-conta"
}
```

O cadastro cria uma conta com saldo inicial zero e um número de conta gerado pela aplicação. CPFs e e-mails já cadastrados são rejeitados.

### Login

```http
POST /auth/login
Content-Type: application/json
```

```json
{
  "cpf": "12345678900",
  "password": "senha-de-acesso"
}
```

Uma autenticação bem-sucedida cria uma sessão HTTP. Guarde o cookie `JSESSIONID` retornado para usar nas rotas autenticadas.

### Consultar conta

```http
GET /account/
Cookie: JSESSIONID=<cookie-da-sessao>
```

Resposta ilustrativa:

```json
{
  "name": "Ana Silva",
  "balance": 0.00
}
```

### Depósito e saque

As duas operações recebem um valor positivo e a senha da conta:

```json
{
  "value": 150.00,
  "accPass": "senha-da-conta"
}
```

Use o corpo acima em `POST /account/deposit` ou `POST /account/withdraw`.

### Transferência

```http
POST /account/transfer
```

```json
{
  "value": 50.00,
  "recvAccNum": 12345,
  "accPass": "senha-da-conta"
}
```

`recvAccNum` é o número da conta de destino.

### Extrato

```http
GET /account/extract
```

O extrato retorna uma lista de movimentações. Cada item contém tipo (`DEPOSIT`, `SAKE` ou `TRANSFER`), valor, data, direção e, quando aplicável, o nome associado à transferência.

## Segurança e configuração operacional

- As senhas de acesso dos usuários são armazenadas com BCrypt.