# Guia de Execução no NetBeans — AV-CAR Auto Center

Passo a passo para abrir, configurar e executar o projeto no Apache NetBeans.

---

## Pré-requisitos

| Ferramenta | Versão | Como verificar |
|------------|--------|----------------|
| **NetBeans** | 21+ (recomendo Apache NetBeans 22+) | `Help → About` |
| **JDK** | 21+ | `java -version` |
| **Maven** | 3.8+ (NetBeans já embute um) | — |
| **PostgreSQL** | 16+ | `psql --version` |
| **Docker** (opcional) | 24+ | `docker --version` |

---

## 1. Abrir o projeto no NetBeans

1. **File → Open Project** (ou `Ctrl+Shift+O`)
2. Navegue até `/home/ramon/Documentos/av_car/`
3. Selecione a pasta `av_car` e clique **Open Project**

O NetBeans reconhecerá automaticamente o `pom.xml` como um projeto Maven single-module. No painel **Projects** (`Ctrl+1`) você verá:

```
av-car
├── Source Packages
│   └── br.edu.senai.fatesg.avcar
│       ├── AvCarApplication.java     ← API entrypoint
│       ├── controllers/
│       ├── services/
│       ├── repositories/
│       ├── model/
│       ├── validations/
│       ├── patterns/
│       ├── exceptions/
│       ├── helpers/
│       ├── config/
│       ├── datastructures/           ← Fila, ordenação, recursão
│       └── swing/
│           ├── AvCarSwingApp.java    ← Swing entrypoint
│           ├── client/
│           └── views/
├── Other Sources
│   └── src/main/resources
│       ├── application.yml
│       └── db/
│           ├── schema.sql
│           └── seed.sql
└── Dependencies
```

> Se aparecer uma janela "Project not recognized", clique em **Open as Project** e escolha **Maven**.

---

## 2. Verificar JDK e configuração

1. **Tools → Java Platforms** → confirme que há um JDK 21+ registrado
2. Clique com botão direito no projeto `av-car` → **Properties**:
   - **Build → Compile**: Java Platform = JDK 21, Source/Binary Format = 21
   - **Run**: aqui você define qual main class será executada ao pressionar F6

O projeto tem **duas main classes**:

| Main Class | Propósito | Comando |
|-----------|-----------|---------|
| `br.edu.senai.fatesg.avcar.AvCarApplication` | API REST (Spring Boot) | `mvn spring-boot:run` |
| `br.edu.senai.fatesg.avcar.swing.AvCarSwingApp` | Interface desktop (Swing) | `mvn exec:java -Pswing` |

---

## 3. Configurar o banco PostgreSQL

### Opção A — Docker (recomendado)

Abra o **Terminal** no NetBeans (`Window → IDE Tools → Terminal`) ou use um externo:

```bash
docker run --name avcar-postgres \
  -e POSTGRES_DB=avcar \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  -d postgres:16-alpine
```

### Opção B — PostgreSQL local

```bash
psql -U postgres -c "CREATE DATABASE avcar;"
```

### Criar tabelas e dados iniciais

```bash
docker exec -i avcar-postgres psql -U postgres -d avcar < src/main/resources/db/schema.sql
docker exec -i avcar-postgres psql -U postgres -d avcar < src/main/resources/db/seed.sql
```

> Se estiver usando PostgreSQL local, substitua `docker exec -i avcar-postgres psql` por `psql`.

---

## 4. Verificar application.yml

O arquivo fica em `src/main/resources/application.yml`. A configuração padrão é:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/avcar
    username: postgres
    password: postgres
server:
  port: 8080
```

Altere `username` e `password` conforme sua instalação local do PostgreSQL.

---

## 5. Compilar o projeto

Clique com botão direito no projeto `av-car` → **Clean and Build**
(ou pressione `Shift+F11`)

O NetBeans executará `mvn clean package -DskipTests`. A saída aparecerá no painel **Output** (`Ctrl+4`).

> Se houver erro de compilação, verifique o JDK configurado no passo 2.

---

## 6. Executar a API (Spring Boot)

### Método A — Run Project (F6)

1. Botão direito no projeto `av-car` → **Properties** → **Run**
2. Em **Main Class**: digite `br.edu.senai.fatesg.avcar.AvCarApplication`
3. Clique **OK**
4. Pressione `F6` (ou clique no botão **Run Project** na toolbar)

### Método B — Maven goal (recomendado)

1. Botão direito no projeto → **Run Maven** → **Custom...**
2. Em **Goals**: digite `spring-boot:run`
3. Clique **OK**

A API sobe em `http://localhost:8080`. Para testar, abra o navegador e acesse `http://localhost:8080/api/clientes`.

> Para parar: clique no botão **Stop** (quadrado vermelho) no painel **Output**.

---

## 7. Executar o Swing (interface gráfica)

**⚠️ A API precisa estar rodando primeiro** (passo 6). O Swing depende da API para carregar dados.

### Método A — Maven com profile swing

1. Botão direito no projeto → **Run Maven** → **Custom...**
2. Em **Goals**: digite `compile exec:java -Pswing`
3. Clique **OK**

Isso compila e executa a classe `AvCarSwingApp` com todas as dependências no classpath.

### Método B — Run Project com main class alternativa

1. API já rodando em outro terminal ou run
2. Botão direito no projeto → **Properties** → **Run**
3. Em **Main Class**: troque para `br.edu.senai.fatesg.avcar.swing.AvCarSwingApp`
4. Clique **OK** e pressione `F6`

**Importante:** Lembre de trocar a Main Class de volta para `AvCarApplication` quando quiser rodar a API novamente.

### Método C — JAR com shade plugin (para testes)

```bash
mvn clean package -Pswing -DskipTests
java -jar target/av-car-1.0.0.jar
```

---

## 8. Dica: executar ambos de uma vez

Crie duas configurações de Run no NetBeans:

1. **Serviço**: `spring-boot:run` (goal Maven)
2. **Swing**: `compile exec:java -Pswing` (goal Maven)

Use o seletor de configuração na toolbar para alternar entre elas sem precisar reconfigurar.

---

## Troubleshooting

| Problema | Solução |
|----------|---------|
| **"Java platform missing"** | Tools → Java Platforms → Add Platform → selecione o JDK 21 |
| **"Cannot find symbol"** | Clean and Build novamente; verifique JDK 21 |
| **"Connection refused"** | PostgreSQL não está rodando: `docker ps \| grep avcar` |
| **Porta 8080 ocupada** | Altere `server.port` em `application.yml` |
| **Swing não abre / janela aparece e some** | Classpath sem Jackson: use `mvn compile exec:java -Pswing` em vez de executar direto |
| **Swing não abre (sem display)** | Verifique ambiente gráfico: `echo $DISPLAY`. Em WSL, use `xming` ou `vcxsrv` |
| **Erro Maven no NetBeans** | Botão direito no projeto → **Reload Project** |
| **Build lento** | Primeira vez com Maven (baixando dependências). Próximas serão mais rápidas. |

---

## Estrutura de diretórios (referência)

```
av-car/
├── pom.xml                         ← Maven single-module
├── BEANS.md                        ← Este guia
├── src/main/java/br/edu/senai/fatesg/avcar/
│   ├── AvCarApplication.java       ← API entrypoint
│   ├── controllers/                ← REST endpoints
│   ├── services/                   ← Regras de negócio
│   ├── repositories/               ← Persistência (JdbcTemplate)
│   ├── model/                      ← Entidades + DTOs
│   ├── validations/                ← Validadores
│   ├── patterns/                   ← Design patterns (GoF)
│   ├── exceptions/                 ← Exceções customizadas
│   ├── helpers/                    ← Utilitários
│   ├── config/                     ← Beans Spring
│   ├── datastructures/             ← Estruturas de Dados I
│   │   ├── FilaEsperaOS.java       ← Fila circular genérica
│   │   ├── OrdenacaoOS.java        ← MergeSort + QuickSort manuais
│   │   └── CalculoOS.java          ← Funções recursivas
│   └── swing/
│       ├── AvCarSwingApp.java      ← Swing entrypoint
│       ├── client/ApiClient.java   ← HTTP client
│       └── views/                  ← Janelas (JFrame, JDialog, JPanel)
└── src/main/resources/
    ├── application.yml             ← Config Spring Boot
    └── db/
        ├── schema.sql              ← DDL — 18 tabelas
        └── seed.sql                ← Dados iniciais
```

---

## Resumo rápido

```bash
# 1. Subir banco
docker start avcar-postgres

# 2. Rodar API
mvn spring-boot:run

# 3. Rodar Swing (outro terminal, com API já rodando)
mvn compile exec:java -Pswing
```
