# Contexto do Projeto AV-CAR (Para Assistentes de IA)

Este documento contém todas as diretrizes, regras de negócio e estado atual do projeto AV-CAR. **LEIA-O ATENTAMENTE ANTES DE PROPOR QUALQUER CÓDIGO.**

## 1. Arquitetura e Stack Tecnológico
* **Linguagem**: Java 21
* **Framework Back-end**: Spring Boot 3.4.1 (com Spring Data JPA, Hibernate, PostgreSQL)
* **Framework Front-end**: Java Swing (com UI desenvolvida via NetBeans Form Designer)
* **Gerenciador de Dependências**: Maven
* **Paradigma**: Monolito. O Spring Boot sobe a aplicação e a GUI Swing roda acoplada ao contexto do Spring (usando `@Component`, `@Autowired` e `ApplicationReadyEvent`).

## 2. O Grande Objetivo (Migração UX/UI 2026)
O sistema está passando por um grande "Facelift" para o padrão "UX/UI 2026". 
* Estamos abandonando os antigos painéis (`MainView`, `ClientePanel`, `VeiculoPanel`, etc.).
* Estamos integrando a lógica legada nas **novas telas** (`TelaPrincipalGUI`, etc.) desenhadas pelo usuário no NetBeans.
* A regra de ouro é: **Só desligaremos as telas antigas quando as novas estiverem 100% sem bugs**.

## 3. Regras Críticas de Desenvolvimento (NÃO ALUCINE)

### A. Back-end é Intocável
* **NUNCA** altere a lógica de negócio, estrutura de dados ou validações do Back-end (`Controllers`, `Services`, `Models`, `Repositories`, `Validations`). 
* O projeto é acadêmico e as regras de negócio pré-existentes devem ser estritamente preservadas. Exemplo: O `GenericValidation.validarTelefone` exige rigorosamente 10 ou 11 dígitos. Se a IA gerar dados fora do padrão, a aplicação vai estourar um `NegocioException`. Adapte o front-end/scripts ao back-end, nunca o contrário.

### B. O Designer do NetBeans
* **NUNCA** modifique a criação de componentes UI (como `initComponents()`) manualmente no código. O usuário utiliza o "Design View" do NetBeans, e edições manuais podem corromper o arquivo `.form`.
* O usuário é responsável por apagar ou arrastar botões/tabelas na tela. A função da IA é **injetar o comportamento** (Eventos, TableModels, Renderers) em métodos seguros como `configurarEventosTabelaX()` chamados no construtor ou no evento `setVisible`.

### C. Filosofia Clean Code
* Não coloque lógica complexa dentro de eventos de botões.
* Tudo que for reutilizável deve ir para as classes utilitárias:
  * `TabelaUtil.java`: Centralizar colunas, ocultar colunas, injetar busca em tempo real, aplicar renderizadores de logomarcas, etc.
  * `FormatadorUtil.java`: Máscaras de CPF, Telefone, Placa, Quilometragem, etc.

## 4. O que já foi feito (Fase 1 - "Acender a luz" da TelaPrincipalGUI)

A `TelaPrincipalGUI` já está com o Spring injetado e os seguintes módulos estão conectados e finalizados:

* **Módulo de Clientes:**
  * Dados carregam do `ClienteController`.
  * Busca em Tempo Real (digitou, filtrou) respeitando o `buscarPorNome` do back-end. Botão "Buscar" físico foi removido.
  * `TabelaUtil` centralizando as colunas, ocultando o ID e colorindo o "Status" (Ativo/Inativo).
  * Criado o script `ClienteDataSeeder.java` que roda em uma **Thread em Background** (para não travar a inicialização do Swing) gerando 100 clientes mockados.

* **Módulo de Veículos:**
  * Dados carregam do `VeiculoController`.
  * Busca em tempo real pela Placa (`buscarPorPlaca`).
  * `FormatadorUtil` aplicando máscara em Placa (AAA-1234) e KM (125.000 km). Chassi forçado para `toUpperCase()`.
  * **Logo da Marca:** Criado o `TabelaUtil.aplicarRenderizadorDeImagem()`. A IA adaptou o antigo renderizador para ler a URL da imagem no banco, calcular a proporção matemática (Aspect Ratio) mantendo o logo não-distorcido dentro de 28x28px, ajustando dinamicamente a altura das linhas da tabela (`RowHeight = 36`) e adicionando um Padding (Margem Interna) para as logos não colarem nas bordas das linhas. 

## 5. Próximos Passos (Para a IA)

* **Fase 1 (Continuação):** Conectar os próximos módulos na `TelaPrincipalGUI`. Faltam: **Serviços, Peças (Estoque), Fornecedores, Colaboradores, Parceiros e Ordem de Serviço (OS)**.
  * Para cada um: Injetar o Controller, criar `carregarTabelaX()`, aplicar formatadores, usar `TabelaUtil.centralizarColunas` e `ocultarColuna(ID)`, e plugar a `TabelaUtil.adicionarBuscaEmTempoReal`.
* **Fase 2:** Quando o painel principal estiver totalmente operacional, o usuário guiará a IA para integrar as ações secundárias (Duplo-clique para Editar, Botão Novo, Inativar, etc.) nas suas respectivas novas telas de formulários.
