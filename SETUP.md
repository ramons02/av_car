# Guia de Setup — AV-CAR Auto Center

Guia para baixar do Git, configurar o ambiente e executar o projeto do zero (faculdade/lab).

---

## Pré-requisitos

| Ferramenta | Versão | Para que serve | Como verificar |
|------------|--------|----------------|----------------|
| **Git** | qualquer | Baixar o projeto | `git --version` |
| **Java JDK** | 21+ | Compilar e rodar | `java -version` |
| **Maven** | 3.8+ | Gerenciar dependências | `mvn -version` |
| **PostgreSQL** | 16+ | Banco de dados | `psql --version` |
| **Docker** (opcional) | 24+ | Subir PostgreSQL rápido | `docker --version` |

---

## 1. Clonar o repositório

```bash
# Substitua pela URL do repositório da sua turma
git clone <url-do-repositorio>
cd av-car
```

A estrutura do projeto após clonar:

```
av-car/
├── pom.xml                         ← Maven (único módulo)
├── SETUP.md                        ← Este guia
├── BEANS.md                        ← Guia para NetBeans
├── src/
│   ├── main/java/br/edu/senai/fatesg/avcar/
│   │   ├── AvCarApplication.java   ← API REST (Spring Boot)
│   │   ├── controllers/            ← Endpoints REST
│   │   ├── services/               ← Regras de negócio
│   │   ├── repositories/           ← Persistência (JdbcTemplate)
│   │   ├── model/                  ← Entidades + DTOs
│   │   ├── validations/            ← Validadores
│   │   ├── patterns/               ← Design patterns (GoF)
│   │   ├── datastructures/         ← Estruturas de Dados I
│   │   │   ├── FilaEsperaOS.java   ← Fila circular
│   │   │   ├── OrdenacaoOS.java    ← MergeSort + QuickSort
│   │   │   └── CalculoOS.java      ← Recursão
│   │   └── swing/
│   │       ├── AvCarSwingApp.java  ← Interface gráfica (Swing)
│   │       ├── client/ApiClient.java
│   │       └── views/              ← Telas do sistema
│   └── main/resources/
│       ├── application.yml         ← Configuração do banco
│       └── db/
│           ├── schema.sql          ← Criação das tabelas
│           └── seed.sql            ← Dados iniciais
└── docs/
    ├── specs/                      ← Especificação do sistema
    ├── technical/                  ← Documento técnico (Estrutura de Dados)
    └── adr/                        ← ADRs
```

---

## 2. Configurar o banco PostgreSQL

### Opção A — Docker (recomendado para lab)

```bash
# Baixar e rodar PostgreSQL
docker run --name avcar-postgres \
  -e POSTGRES_DB=avcar \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  -d postgres:16-alpine

# Verificar se está rodando
docker ps --filter name=avcar-postgres
```

### Opção B — PostgreSQL instalado na máquina

```bash
# Criar o banco (se não existir)
psql -U postgres -c "CREATE DATABASE avcar;"
```

---

## 3. Criar tabelas e dados iniciais

```bash
# Via Docker
docker exec -i avcar-postgres psql -U postgres -d avcar < src/main/resources/db/schema.sql
docker exec -i avcar-postgres psql -U postgres -d avcar < src/main/resources/db/seed.sql

# Via psql local
psql -U postgres -d avcar < src/main/resources/db/schema.sql
psql -U postgres -d avcar < src/main/resources/db/seed.sql
```

O seed cria: 10 marcas, 28 modelos, 5 clientes (PF + PJ), 3 veículos, 5 fornecedores, 3 colaboradores, 5 peças, 8 serviços, 4 OS com itens, 3 parceiros externos.

---

## 4. Verificar configuração do banco

**Arquivo:** `src/main/resources/application.yml`

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/avcar
    username: postgres
    password: postgres
server:
  port: 8080
```

Se seu PostgreSQL usar senha diferente, edite `password`.

---

## 5. Compilar o projeto

```bash
mvn clean package -DskipTests
```

Saída esperada: `BUILD SUCCESS`. O JAR gerado fica em `target/av-car-1.0.0.jar`.

---

## 6. Executar

### 6.1 API (Spring Boot)

A API precisa estar rodando para o Swing funcionar.

```bash
# Terminal 1
mvn spring-boot:run
```

A API sobe em `http://localhost:8080`. Teste:

```bash
curl http://localhost:8080/api/clientes
```

Deverá retornar um JSON com 5 clientes.

### 6.2 Swing (interface gráfica)

Com a API rodando, abra **outro terminal**:

```bash
# Terminal 2 (deixe a API rodando no terminal 1)
mvn compile exec:java -Pswing
```

A janela "AV-CAR Auto Center - Sistema de Gestão" aparecerá com 8 abas.

> ⚠️ Precisa de ambiente gráfico (Linux com X11, Windows, macOS).  
> Em laboratório sem display gráfico, apenas a API funciona.

---

## 7. Estruturas de Dados — Como testar

O pacote `datastructures/` contém 3 classes independentes da API:

```bash
# Compilar (já fez no passo 5)
mvn compile -q

# Executar testes manuais (exemplos)
java -cp "target/classes:$(mvn dependency:build-classpath -q -DincludeScope=runtime -Dmdep.outputFile=/dev/stdout)" \
  -ea br.edu.senai.fatesg.avcar.datastructures.FilaEsperaOS
```

Mas o mais prático é testar pela interface Swing:

| Funcionalidade | Como testar |
|----------------|-------------|
| **Fila de Espera** | Abrir Swing → aba "Ordens de Serviço" → botão "Fila de Espera" |
| **Ordenação** | Abrir Swing → aba "Ordens de Serviço" → clicar nos cabeçalhos da tabela |
| **Recursão** | Interno (soma de valores no cálculo de total da OS) |

---

## 8. Verificação rápida

```bash
# 1. Banco rodando?
docker ps --filter name=avcar-postgres

# 2. API no ar?
curl -s http://localhost:8080/api/clientes | head -c 100

# 3. Compilação OK?
mvn compile -q && echo "OK"
```

---

## Troubleshooting

| Problema | Solução |
|----------|---------|
| **`java: command not found`** | Instale o JDK 21: `sudo apt install openjdk-21-jdk` |
| **`mvn: command not found`** | Instale o Maven: `sudo apt install maven` |
| **`docker: command not found`** | Instale o Docker ou use PostgreSQL local (Opção B) |
| **`psql: could not connect to server`** | PostgreSQL não iniciou: `sudo systemctl start postgresql` |
| **Porta 8080 ocupada** | `sudo lsof -i :8080` → `kill <PID>` ou troque a porta no `application.yml` |
| **`ERROR: relation "cliente" does not exist`** | Esqueceu de rodar o `schema.sql` — volte ao passo 3 |
| **`BUILD FAILURE`** | Verifique `java -version` (precisa ser 21+) e depois `mvn clean compile` |
| **Swing: janela aparece e some** | Use `mvn compile exec:java -Pswing` em vez de `java -jar` |
| **Swing: `ClassNotFoundException`** | Certifique-se de rodar com Maven (`mvn compile exec:java -Pswing`), não com `java` puro |
| **Sem Docker no lab** | Instale PostgreSQL manualmente (Opção B) ou peça ao professor para instalar o Docker |

---

## Resumo de comandos

```bash
# 1. Clonar
git clone <url> && cd av-car

# 2. Subir banco
docker run --name avcar-postgres -e POSTGRES_DB=avcar -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres:16-alpine

# 3. Popular banco
docker exec -i avcar-postgres psql -U postgres -d avcar < src/main/resources/db/schema.sql
docker exec -i avcar-postgres psql -U postgres -d avcar < src/main/resources/db/seed.sql

# 4. Compilar
mvn clean package -DskipTests

# 5. Rodar API (terminal 1)
mvn spring-boot:run

# 6. Rodar Swing (terminal 2, com API rodando)
mvn compile exec:java -Pswing
```
