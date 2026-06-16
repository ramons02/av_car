# 🚗 AV-CAR - Sistema de Gerenciamento Automotivo

> **Organização:** SENAI FATESG  
> **Status do Projeto:** Em Desenvolvimento Ativo  

O **AV-CAR** é um sistema completo e moderno para gerenciamento de oficinas mecânicas e centros automotivos. Ele foi projetado para englobar todo o fluxo de ponta a ponta: desde o cadastro de clientes e veículos até o controle avançado de Ordens de Serviço (OS), estoque de peças, serviços terceirizados, fila de espera e controle de garantias.

---

## ✨ Funcionalidades Principais

* **🧑‍🤝‍🧑 Gestão de Clientes e Colaboradores:** Suporte para Pessoa Física (CPF) e Jurídica (CNPJ), além do controle da equipe de mecânicos.
* **🚘 Cadastro de Veículos:** Controle de veículos com histórico de propriedade e cascata inteligente de Marcas e Modelos.
* **🛠️ Ordem de Serviço (Coração do Sistema):** 
  * Adição de peças e mão-de-obra (interna ou de parceiros externos).
  * Fluxo de status validado (Aberta → Em Andamento → Aguardando Peça → Concluída → Entregue).
  * Recálculo automático de custos e descontos.
* **📊 Dashboard Interativo:** Cards de indicadores de performance (KPIs) e gráficos nativos desenhados em Java2D.
* **📑 Geração de Documentos:** Exportação de Ordem de Serviço detalhada diretamente para PDF.
* **⏳ Fila de Espera:** Algoritmo circular para o controle otimizado e visual do fluxo de veículos na oficina.

---

## 🛠️ Tecnologias Utilizadas

Apesar de rodar como uma aplicação Desktop, o projeto adota uma estrutura híbrida robusta nos bastidores:

* **Java 21** - Linguagem principal de desenvolvimento.
* **Spring Boot 3.4.1** - Gerenciador central de Injeção de Dependências e ciclo de vida.
* **Java Swing + FlatLaf 3.4** - Construção de uma interface de usuário rica e com Tema Escuro Moderno nativo.
* **PostgreSQL (via JDBC/JdbcTemplate)** - Banco de dados relacional robusto.
* **OpenPDF (1.3.36)** - Geração de documentação/recibos dinâmicos.
* **Maven** - Automação e gerenciamento de dependências.

---

## 🏗️ Arquitetura do Sistema

O sistema separa claramente o "Visual" da "Regra de Negócio":

1. **Camada de Interface (Swing):** Segue o padrão **Model-View-Presenter (MVP)**. As `Views` apenas desenham a tela, enquanto os `Presenters` validam os cliques, mascaram os campos e enviam os dados.
2. **Camada REST (Controllers):** Controladores preparados com retornos padronizados `ApiResponse`.
3. **Camada de Serviço (Services):** Contém as validações complexas e lida com as transições de status e regras financeiras.
4. **Camada de Dados (Repositories):** Herdam do `AbstractRepository` usando um padrão "Template Method" para centralizar as operações via JDBC de forma enxuta.

---

## 🚀 Como Rodar o Projeto Localmente

### Pré-requisitos
* Java JDK 21 ou superior
* Maven 3.8+
* PostgreSQL instalado e rodando (Porta 5432)

### 1. Configurando o Banco de Dados
Certifique-se de ter um banco de dados vazio criado chamado `avcar` no seu PostgreSQL.
As credenciais padrão esperadas pelo Spring Boot (`src/main/resources/application.yml`) são:
* **Host:** localhost:5432
* **Database:** `avcar`
* **Usuário:** `postgres`
* **Senha:** `postgres`

**⚠️ PASSO OBRIGATÓRIO:** O sistema não cria as tabelas automaticamente. Você deve rodar os scripts localizados na pasta `db/` do projeto (usando pgAdmin, DBeaver, ou Query Tools):
1. Execute primeiro o **`db/schema.sql`** para criar as tabelas e relacionamentos.
2. Execute em seguida o **`db/seed.sql`** para inserir os dados iniciais de teste (massa de dados).

### 2. Rodando o Projeto
Abra seu terminal na raiz do projeto e execute:
```bash
# Baixar dependências e compilar
./mvnw clean install -DskipTests

# Rodar a aplicação
./mvnw spring-boot:run
```
*(Opcional: O projeto também pode ser executado diretamente pela sua IDE favorita executando a classe `AvCarApplication.java`)*

---

## 📂 Estrutura de Pastas e Módulos

* `br...core/`: Contém as classes abstratas (Controller, Service, Repository) que evitam a repetição de código (DRY).
* `br...business/`: Os 9 grandes domínios da aplicação (Clientes, Veículos, OS, Peças, Garantia, etc.).
* `br...datastructures/`: Implementações de algoritmos e estruturas próprias (Busca Binária, Fila, Merge Sort).
* `br...swing/views/`: Todas as mais de 10 telas do sistema, divididas entre Views, Presenters e Utils visuais gráficos.
