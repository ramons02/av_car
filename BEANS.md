# Guia de Execução — AV-CAR Auto Center

Passo a passo para baixar do GitHub, configurar o banco com pgAdmin e executar **API + Swing** com um único comando (faculdade/lab).

---

## Pré-requisitos

| Ferramenta | Versão | Como verificar |
|------------|--------|----------------|
| **Git** | qualquer | `git --version` |
| **JDK** | 21+ | `java -version` |
| **Maven** | 3.8+ | `mvn -version` |
| **PostgreSQL** | 16+ | `psql --version` |
| **pgAdmin** | 4+ | Acompanha o PostgreSQL |

---

## 1. Baixar o projeto do GitHub

```bash
git clone <url-do-repositorio>
cd av-car
```

Sem Git? Baixe o ZIP: **Code → Download ZIP** no GitHub, extraia e entre na pasta.

---

## 2. Configurar o banco PostgreSQL com pgAdmin

Use o **pgAdmin** para criar o banco e rodar os scripts — sem precisar de terminal.

### 2.1 Abrir e conectar no pgAdmin

| Sistema | Como abrir |
|---------|------------|
| **Windows** | Iniciar → pgAdmin 4 |

1. Clique direito em **Servers → Register → Server**
2. Aba **General**: nome qualquer (ex: `Local`)
3. Aba **Connection**:
   - Host: `localhost`
   - Port: `5432`
   - Username: `postgres`
   - Password: *(a senha que você definiu na instalação)*
4. **Save**

### 2.2 Criar o banco

Clique direito em **Databases → Create → Database** → Nome: `avcar` → Owner: `postgres` → **Save**

### 2.3 Rodar os scripts SQL

1. Clique direito em **public → Query Tool**
2. Clique no ícone **Open File** (pasta) → selecione `db/schema.sql`
3. Execute (⚡)
4. Repita com `db/seed.sql`

Pronto — banco criado com 18 tabelas e dados iniciais.

---

## 3. Configurar senha no application.yml

Arquivo: `src/main/resources/application.yml`

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/avcar
    username: postgres
    password: postgres
server:
  port: 8080
```

Troque `password` pela senha do seu PostgreSQL.

---

## 4. Rodar no NetBeans

### 4.1 Rodar a API

1. Botão direito no projeto `av-car` → **Run Maven** → **Custom...**
2. Em **Goals**: digite `spring-boot:run`
3. Clique **OK**

A API sobe em `http://localhost:8080`. Deixe rodando.

### 4.2 Rodar o Swing

Com a API já rodando (passo 4.1):

1. Botão direito no projeto `av-car` → **Run Maven** → **Custom...**
2. Em **Goals**: digite `compile exec:java -Pswing`
3. Clique **OK**

A janela do Swing vai abrir.

> Dica: deixe a API rodando em segundo plano e execute o Swing em outra aba do Output.

---

## 5. Rodar pelo terminal (cmd/PowerShell)

Se preferir rodar fora do NetBeans, abra o **Prompt de Comando** ou **PowerShell** na pasta do projeto:

```cmd
:: Terminal 1 — API
mvn spring-boot:run
```

```cmd
:: Terminal 2 (com a API rodando) — Swing
mvn compile exec:java -Pswing
```

---

---

## Troubleshooting

| Problema | Solução |
|----------|---------|
| **`java' não é reconhecido`** | Instale o JDK 21 e configure a variável `JAVA_HOME` |
| **`mvn' não é reconhecido`** | Instale o Maven e adicione ao `PATH` |
| **"Connection refused"** | PostgreSQL não está rodando — abra o pgAdmin e veja se o servidor está verde |
| **"database 'avcar' does not exist"** | Esqueceu de criar o banco no pgAdmin — volte ao passo 2.2 |
| **"relation 'cliente' does not exist"** | Esqueceu de executar o schema.sql — volte ao passo 2.3 |
| **Porta 8080 ocupada** | Altere `server.port` no `application.yml` |
| **Swing não abre** | Verifique se a API está rodando primeiro (passo 4.1) |
| **pgAdmin não conecta** | PostgreSQL não foi iniciado — procure por **pgAdmin** ou **Services** no Windows e inicie o PostgreSQL |
| **Git não encontrado** | Baixe o ZIP manualmente do GitHub |
