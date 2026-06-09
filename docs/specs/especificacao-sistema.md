# Especificação do Sistema — AV-CAR Auto Center

## 1. Visão Geral

Sistema de gestão desktop/local para controle de ordens de serviço, histórico de veículos, estoque de peças, garantias e parceiros comerciais de uma oficina mecânica.

**Arquitetura:** Monólito em camadas com API REST (Spring Boot 3.4.1) + Cliente Desktop (Java Swing) + PostgreSQL. Uso intensivo de Generics nas camadas Model/DTO, Repository, Service, Validator, Controller e View para eliminar duplicação de código.

## 2. Entidades e Domínios

### 2.1 Cliente
- Pessoa Física (CPF) ou Pessoa Jurídica (CNPJ + Inscrição Estadual)
- **Tabelas:** `cliente` (base) + `cliente_pf` (específicos PF) + `cliente_pj` (específicos PJ)
- Atributos base (tabela `cliente`): id, nome, endereço, bairro, cidade, estado, cep, telefone, e-mail, tipo (PF/PJ), documento (CPF/CNPJ), data de cadastro (preenchido automaticamente via `LocalDate.now()` pelo service se nulo), observacoes, ativo (soft delete)
- PF (tabela `cliente_pf`): cliente_id (FK), cpf, rg, data_nascimento
- PJ (tabela `cliente_pj`): cliente_id (FK), cnpj, inscricao_estadual, razao_social
- DTO: id, nome, tipo, documento, rg, dataNascimento, razaoSocial, inscricaoEstadual, telefone, email, endereco, bairro, cidade, estado, cep, observacoes, ativo

### 2.2 Veículo
- Vinculado a um modelo e marca
- Histórico de proprietários (Cliente pode ter múltiplos veículos; veículo pode ter múltiplos proprietários ao longo do tempo)
- Atributos: id, placa, chassi, ano fabricação, ano modelo, cor, quilometragem, acessórios, modelo, ativo (soft delete)
- Proprietário atual via `historico_cliente_veiculo` (data_fim IS NULL)
- DTO: id, placa, chassi, anoFabricacao, anoModelo, cor, quilometragem, acessorios, modeloId, modeloNome, marcaId, marcaNome, marcaLogoUrl, clienteId, clienteNome, ativo

### 2.3 Marca / Modelo
- Marca: id, nome, logo_url
- Modelo: id, nome, marca (FK)

### 2.4 Colaborador
- Funcionário da oficina, pode exercer uma ou mais funções (N:N via `colaborador_funcao`)
- Atributos: id, nome, matrícula, CPF, telefone, e-mail, data_admissao, data_demissao, salario, observacoes, funcoes (lista), ativo (soft delete)
- DTO: id, nome, matricula, cpf, telefone, email, dataAdmissao, dataDemissao, salario, observacoes, funcoes, ativo

### 2.5 Função
- Atributos: id, nome, descrição, especialidade, comissao

### 2.6 Fornecedor
- Empresa fornecedora de peças
- Atributos: id, nome, razao_social, CNPJ, telefone, e-mail, endereço, bairro, cidade, estado, cep, ativo (soft delete)
- DTO: id, nome, razaoSocial, cnpj, telefone, email, endereco, bairro, cidade, estado, cep, ativo

### 2.7 Peça
- Código de Identificação Nacional, prazo de garantia (validade) e fornecedor de origem
- Atributos: id, código nacional, código interno, nome, descrição, fabricante, categoria, preço custo, preço venda, quantidade estoque, prazo garantia (dias), data_compra, fornecedor (FK), ativo (soft delete)
- DTO: id, codigoNacional, codigoInterno, nome, descricao, fabricante, categoria, precoCusto, precoVenda, quantidadeEstoque, prazoGarantiaDias, dataCompra, fornecedorId, fornecedorNome, ativo

### 2.8 Parceiro Externo
- Empresas terceirizadas (retífica, guincho, etc.)
- Atributos: id, nome, CNPJ, tipo de serviço, telefone, e-mail, ativo (soft delete)

### 2.9 Serviço
- Procedimentos realizados nos veículos (internos ou terceirizados)
- Catálogo de serviços da oficina
- Atributos: id, nome, descrição, valor mão-de-obra, prazo garantia (dias), tempo_estimado, responsável (FK colaborador), terceirizado, ativo (soft delete)
- DTO: id, nome, descricao, valorMaoObra, prazoGarantiaDias, tempoEstimado, responsavelId, responsavelNome, terceirizado, ativo

### 2.10 Ordem de Serviço (OS)
- Vinculada estritamente ao veículo, independente do proprietário atual
- Ciclo de vida obrigatório: **Aberta** → **Orçamento** → (**Aguardando Peça**) → **Execução** → **Aguardando Pagamento** → **Finalizada**
- Cancelamento possível a partir de qualquer status não-terminal
- Itens de serviço, itens de peça e serviços externos associados
- Responsável técnico (FK colaborador) — mecânico responsável pela execução (NOT NULL)
- Atributos: id, número, veículo (FK), cliente (FK), responsável (FK), status, data abertura, data finalização, entrada_veiculo, defeito_relatado, forma_pagamento, valor_desconto, valor total, observações
- DTO: id, numero, veiculo, cliente, responsavelId, responsavelNome, status, dataAbertura, dataFinalizacao, entradaVeiculo, defeitoRelatado, formaPagamento, valorDesconto, valorTotal, observacoes, ativo (possui construtor padrão público obrigatório para Jackson)

### 2.11 ItemServico / ItemPeca
- Itens associados a uma OS com quantidade e valor unitário
- ItemServico: id, ordem_servico_id (FK), servico_id (FK), quantidade, valor_unitario, hora_inicio, hora_fim, status
- ItemPeca: id, ordem_servico_id (FK), peca_id (FK), quantidade, preco_unitario
- DTOs: ItemServicoDTO (servicoId, servicoNome, quantidade, valorUnitario, horaInicio, horaFim, status), ItemPecaDTO (pecaId, pecaNome, quantidade, precoUnitario, subtotal)

### 2.12 ServicoExterno
- Serviço prestado por parceiro externo vinculado a uma OS
- Atributos: id, ordem_servico_id (FK), parceiro_id (FK), descricao, valor, prazo_garantia_dias
- DTO: ServicoExternoDTO (parceiroId, parceiroNome, descricao, valor, prazoGarantiaDias)

### 2.13 Histórico Cliente-Veículo
- Registro de proprietários ao longo do tempo
- Atributos: id, veiculo (FK), cliente (FK), data_inicio, data_fim (NULL = proprietário atual)

### 2.14 Soft Delete
- `Cliente`, `Veiculo`, `Colaborador`, `Fornecedor`, `Peça`, `ParceiroExterno`, `Servico` possuem coluna `ativo BOOLEAN NOT NULL DEFAULT TRUE` no banco e campo correspondente nas classes Java
- Repositórios filtram automaticamente `WHERE ativo = true` em consultas de listagem
- `toggleStatus(Long id)` alterna via `UPDATE ... SET ativo = NOT ativo`
- Endpoints expõem `PATCH /{id}/toggle-status`
- Endpoints `GET` aceitam `?inativos=true` para incluir inativos
- `OrdemServico` não possui soft delete (exclusão física via DELETE)

### 2.15 GarantiaDTO
- DTO auxiliar para exibição de garantia de itens da OS
- Atributos: tipo, item, dataFinalizacao, dataVencimento, diasRestantes, vencida
- Os prazos são calculados a partir de `dataFinalizacao + prazoGarantiaDias` de cada item

## 3. Regras de Negócio

1. **Histórico de Propriedade:** Veículo pode ter múltiplos proprietários. Sistema deve registrar histórico de vínculo em `historico_cliente_veiculo`.
2. **Vínculo da OS:** Ordem de Serviço pertence ao veículo, independente do proprietário no momento.
3. **Rastreabilidade de Peças:** Cada peça vinculada ao seu fornecedor para acionamento de garantia.
4. **Vigência da Garantia:** Prazo de garantia de peças e serviços começa na data de finalização da OS.
5. **Responsabilidade Técnica:** Todo serviço registrado em uma OS deve ter um colaborador responsável pela execução.
6. **Ciclo de Vida da OS:** ABERTA → ORCAMENTO → (AGUARDANDO_PECA) → EXECUCAO → AGUARDANDO_PAGAMENTO → FINALIZADA. Cancelamento: qualquer status não-terminal → CANCELADA. Pausa: ORCAMENTO/EXECUCAO → AGUARDANDO_PECA. Retorno: AGUARDANDO_PECA → ORCAMENTO.

## 4. Tratamento de Exceções

- `GlobalExceptionHandler` (`@RestControllerAdvice`) mapeia exceções para HTTP status codes:
  - `NegocioException` (validação/regra) → **HTTP 422 (Unprocessable Entity)** com `ApiResponse.erro()`
  - `EntidadeNaoEncontradaException` → **HTTP 404 (Not Found)** com `ApiResponse.erro()`
  - `Exception` (qualquer outra) → **HTTP 500** com `ApiResponse.erro()` em formato padronizado

### 4.1 Observações sobre o Tratamento de Exceções

- o `GlobalExceptionHandler` catch genérico (`Exception.class`) garante que mesmo erros inesperados (ex: NPE, DataIntegrityViolation) retornem `ApiResponse` padronizado em vez do formato padrão Spring Boot
- Na camada Swing, `MainView.criarPainel()` captura exceções na criação de cada aba (try-catch por Supplier), evitando que um erro em um painel quebre toda a UI — a aba problemática exibe uma mensagem de erro no lugar do conteúdo
- `AvCarSwingApp` possui try-catch geral no startup com stack trace em stderr + JOptionPane de erro fatal

## 5. Stack Tecnológica

| Componente | Tecnologia |
|------------|-----------|
| Build | Maven (monolito) |
| API | Java 21 + Spring Boot 3.4.1 |
| Persistência | Spring JdbcTemplate + PostgreSQL 16+ |
| Cliente Desktop | Java Swing (REST client via HttpClient) |
| Comunicação | REST via HTTP (localhost:8080) |
| JSON | Jackson (ObjectMapper + JavaTimeModule + FAIL_ON_UNKNOWN_PROPERTIES=false) |

### 6.3 Padrões de Estruturas de Dados

| Estrutura | Localização | Propósito |
|-----------|-------------|-----------|
| Fila Circular (FIFO) | `datastructures/FilaEsperaOS<T>` | Fila de espera genérica com array redimensionável e `enqueue`/`dequeue` O(1) |
| MergeSort | `datastructures/OrdenacaoOS` | Ordenação O(n log n) estável, implementada manualmente com recursão |
| QuickSort | `datastructures/OrdenacaoOS` | Ordenação O(n log n) médio (Lomuto partition), implementada manualmente |
| Busca Linear | `datastructures/BuscaOS` | Localizar elemento usando critério `contains` para matches parciais O(n) |
| Busca Binária | `datastructures/BuscaOS` | Localizar elemento exato em lista ordenada O(log n) |
| Recursão (soma) | `datastructures/CalculoOS` | `somarValores(List<Double>, int i)` — percorre lista recursivamente acumulando valores |
| Recursão (fatorial) | `datastructures/CalculoOS` | `fatorial(int n)` — função recursiva clássica para cálculo de permutações |

**Restrições:** Nenhum algoritmo usa `Collections.sort`, `Arrays.sort`, `Stream.sorted` ou `List.sort` — toda ordenação é manual.

## 6. Design Patterns

### 6.1 Padrões Estruturais (Generics)

| Padrão | Localização | Propósito |
|--------|-------------|-----------|
| Template Method (Generics) | `core/domains/BaseModel` + `core/dtos/BaseDTO` | Classe base para todas as entidades e DTOs com `id` e `ativo` |
| Template Method (Generics) | `core/repositories/AbstractRepository<T extends BaseModel>` | CRUD genérico com soft delete e toggle-status |
| Template Method (Generics) | `core/validations/GenericValidation<T>` | Validação genérica com helpers e validação padrão |
| Template Method (Generics) | `core/services/GenericService<T,D>` | Service genérico com listar, buscar, toggle-status |
| Template Method (Generics) | `core/controllers/GenericController<D extends BaseDTO>` | Controller REST genérico com GET /, GET /{id}, PATCH toggle-status |
| Template Method (Generics) | `swing/views/AbstractPanel<D extends BaseDTO>` | Painel Swing genérico com JTable, toolbar, busca — recebe `Class<D> dtoClass` para evitar type erasure no Jackson |
| Template Method (Generics) | `swing/views/AbstractDialog<D extends BaseDTO>` | Dialog Swing genérico com helpers de layout e formulário |

**Nota sobre type erasure:** `AbstractPanel` recebe `Class<D> dtoClass` explicitamente no construtor e utiliza `ApiClient.getList(path, Class<T>)` (que usa `TypeFactory.constructCollectionType` internamente) para que o Jackson conheça o tipo concreto em tempo de execução, evitando o erro "Cannot construct instance of BaseDTO".

### 6.2 Padrões de Domínio (preservados)

| Padrão | Localização | Propósito |
|--------|-------------|-----------|
| Singleton | `patterns/ConfigManager` | Gerenciamento centralizado de configurações |
| Factory Method | `patterns/ClienteFactory` | Criação de PessoaFisica ou PessoaJuridica |
| Iterator | `patterns/OrdemServicoIterator` | Iteração de OS por status |
| Template Method | `patterns/OrdemServicoTemplate` + subclasses | Ciclo de vida da OS (Orçamento→Execução→Pagamento→Finalizada) |
| Adapter | `patterns/ParceiroExternoAdapter` | Adaptação de serviços terceirizados para Servico |
| Decorator | `patterns/OrdemServicoDecorator` + subclasses | Garantia estendida e desconto sobre OS |

## 7. Estrutura de Módulos

```
av-car/
├── pom.xml                     (monolito — único módulo)
├── db/schema.sql               (DDL PostgreSQL)
├── docs/                       (documentação)
└── src/main/java/br/edu/senai/fatesg/avcar/
    ├── AvCarApplication.java   (Spring Boot entrypoint)
    ├── business/               (domínios e regras de negócio)
    │   ├── clientes/           (Classes Cliente, Controller, Service, Repository, DTO)
    │   ├── colaboradores/      (Classes Colaborador)
    │   ├── fornecedores/       (Classes Fornecedor)
    │   ├── ordemservico/       (Classes Ordem de Serviço e Itens)
    │   ├── parceiros/          (Classes Parceiros Externos)
    │   ├── pecas/              (Classes Peças)
    │   ├── servicos/           (Classes Serviços)
    │   └── veiculos/           (Classes Veículos)
    ├── core/                   (arquitetura genérica base)
    │   ├── controllers/        (GenericController)
    │   │   └── response/       (ApiResponse)
    │   ├── domains/            (BaseModel)
    │   ├── dtos/               (BaseDTO)
    │   ├── exceptions/         (GlobalExceptionHandler, NegocioException, etc.)
    │   ├── helpers/            (IGenericMapper)
    │   ├── repositories/       (AbstractRepository, Repository)
    │   ├── services/           (GenericService)
    │   └── validations/        (GenericValidation, Validator)
    ├── datastructures/          (estruturas de dados — Estrutura de Dados I)
    │   ├── BuscaOS.java          (Algoritmos de busca linear e binária genéricos)
    │   ├── FilaEsperaOS.java     (fila circular genérica com array redimensionável)
    │   ├── OrdenacaoOS.java      (MergeSort + QuickSort manuais)
    │   └── CalculoOS.java        (funções recursivas: somarValores, fatorial)
    ├── swing/
    │   ├── AvCarSwingApp.java (Swing entrypoint)
    │   ├── client/
    │   │   └── ApiClient.java     (HTTP client com Jackson)
    │   └── views/
    │       ├── MainView.java      (JFrame com abas)
    │       ├── AbstractPanel.java (painel genérico)
    │       ├── AbstractDialog.java (dialog genérico)
    │       └── ... (Painéis e Dialogs específicos)
    ├── patterns/               (padrões de projeto implementados)
    │   ├── ConfigManager.java  (Singleton)
    │   ├── ClienteFactory.java (Factory Method)
    │   ├── OrdemServicoTemplate.java (Template Method)
    │   └── ...
    ├── helpers/
    │   └── FormatadorHelper.java
    └── config/
        ├── DatabaseConfig.java (JdbcTemplate bean)
        └── AppConfig.java (ConfigManager bean)
```

## 8. Banco de Dados (PostgreSQL)

### Schema

18 tabelas no schema (`db/schema.sql`, ~190 linhas):
- `marca`, `modelo`, `cliente`, `cliente_pf`, `cliente_pj`, `veiculo`, `historico_cliente_veiculo`
- `colaborador`, `funcao`, `colaborador_funcao`
- `fornecedor`, `peca`, `parceiro_externo`, `servico`
- `ordem_servico`, `item_servico`, `item_peca`, `servico_externo`

### Mapeamento Repository

Cada repositório concreto estende `AbstractRepository<T>` e sobrescreve os métodos de JOIN quando necessário. `OrdemServicoRepositoryImpl` é o único que não estende `AbstractRepository` por ter lógica de ciclo de vida própria.

**Importante:** INSERTs com retorno de ID usam `new String[]{"id"}` em vez de `Statement.RETURN_GENERATED_KEYS` para compatibilidade com PostgreSQL, que retorna todas as colunas da linha inserida (devido a `RETURNING *` interno) e causa erro "The getKey method should only be used when a single key is returned".

## 9. Endpoints da API

### Clientes
| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/api/clientes?inativos=false` | Listar todos |
| GET | `/api/clientes/{id}` | Buscar por ID |
| GET | `/api/clientes/buscar?nome=` | Buscar por nome |
| POST | `/api/clientes/pf` | Criar Pessoa Física |
| POST | `/api/clientes/pj` | Criar Pessoa Jurídica |
| PUT | `/api/clientes/{id}` | Atualizar |
| PATCH | `/api/clientes/{id}/toggle-status` | Alternar ativo/inativo (retorna ClienteDTO) |

### Veículos
| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/api/veiculos?inativos=false` | Listar todos |
| GET | `/api/veiculos/{id}` | Buscar por ID |
| GET | `/api/veiculos/buscar?placa=` | Buscar por placa |
| GET | `/api/veiculos/cliente/{clienteId}` | Buscar por cliente (proprietário atual) |
| POST | `/api/veiculos` | Criar (placa, chassi, anoFabricacao, anoModelo, cor, quilometragem, acessorios, modeloId, clienteId + vinculo historico) |
| PUT | `/api/veiculos/{id}` | Atualizar (se cliente mudar, atualiza histórico) |
| PATCH | `/api/veiculos/{id}/toggle-status` | Alternar ativo/inativo |
| GET | `/api/veiculos/marcas` | Listar marcas (com logo_url) |
| GET | `/api/veiculos/marcas/{marcaId}/modelos` | Listar modelos por marca |

### Ordens de Serviço
| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/api/ordens-servico` | Listar todas |
| GET | `/api/ordens-servico/{id}` | Buscar por ID |
| GET | `/api/ordens-servico/status/{status}` | Filtrar por status |
| POST | `/api/ordens-servico` | Criar OS |
| PATCH | `/api/ordens-servico/{id}` | Atualizar campos |
| POST | `/api/ordens-servico/{id}/avancar/orcamento` | Avançar de ABERTA para ORCAMENTO |
| POST | `/api/ordens-servico/{id}/avancar/execucao` | Avançar de ORCAMENTO/AGUARDANDO_PECA para EXECUCAO |
| POST | `/api/ordens-servico/{id}/avancar/pagamento` | Avançar de EXECUCAO para AGUARDANDO_PAGAMENTO |
| POST | `/api/ordens-servico/{id}/avancar/finalizar` | Avançar de AGUARDANDO_PAGAMENTO para FINALIZADA |
| POST | `/api/ordens-servico/{id}/pausar` | Pausar para AGUARDANDO_PECA |
| POST | `/api/ordens-servico/{id}/retornar` | Retornar de AGUARDANDO_PECA para ORCAMENTO |
| POST | `/api/ordens-servico/{id}/cancelar` | Cancelar OS (qualquer status não-terminal) |
| GET | `/api/ordens-servico/{id}/garantia` | Calcular garantia |
| POST | `/api/ordens-servico/{id}/garantia?dias=` | Aplicar garantia estendida (Decorator) |
| POST | `/api/ordens-servico/{id}/desconto?percentual=` | Aplicar desconto (Decorator) |
| DELETE | `/api/ordens-servico/{id}` | Remover OS |
| GET | `/api/ordens-servico/{id}/itens-servico` | Listar itens de serviço |
| POST | `/api/ordens-servico/{id}/itens-servico` | Adicionar item de serviço |
| DELETE | `/api/ordens-servico/{id}/itens-servico/{itemId}` | Remover item de serviço |
| GET | `/api/ordens-servico/{id}/itens-peca` | Listar itens de peça |
| POST | `/api/ordens-servico/{id}/itens-peca` | Adicionar item de peça |
| DELETE | `/api/ordens-servico/{id}/itens-peca/{itemId}` | Remover item de peça |
| GET | `/api/ordens-servico/{id}/servicos-externos` | Listar serviços externos |
| POST | `/api/ordens-servico/{id}/servicos-externos` | Adicionar serv. externo |
| DELETE | `/api/ordens-servico/{id}/servicos-externos/{itemId}` | Remover serv. externo |

### Serviços (Catálogo)
| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/api/servicos?inativos=false` | Listar todos |
| GET | `/api/servicos/{id}` | Buscar por ID |
| GET | `/api/servicos/buscar?nome=` | Buscar por nome |
| POST | `/api/servicos` | Criar |
| PUT | `/api/servicos/{id}` | Atualizar |
| PATCH | `/api/servicos/{id}/toggle-status` | Alternar ativo/inativo |

### Colaboradores
| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/api/colaboradores?inativos=false` | Listar todos |
| GET | `/api/colaboradores/{id}` | Buscar por ID |
| GET | `/api/colaboradores/buscar?nome=` | Buscar por nome |
| POST | `/api/colaboradores` | Criar (nome, cpf, telefone, email, funcaoIds) |
| PUT | `/api/colaboradores/{id}` | Atualizar |
| PATCH | `/api/colaboradores/{id}/toggle-status` | Alternar ativo/inativo |
| GET | `/api/colaboradores/funcoes` | Listar funções |

### Fornecedores
| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/api/fornecedores?inativos=false` | Listar todos |
| GET | `/api/fornecedores/{id}` | Buscar por ID |
| GET | `/api/fornecedores/buscar?nome=` | Buscar por nome |
| POST | `/api/fornecedores` | Criar |
| PUT | `/api/fornecedores/{id}` | Atualizar |
| PATCH | `/api/fornecedores/{id}/toggle-status` | Alternar ativo/inativo |

### Peças
| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/api/pecas?inativos=false` | Listar todos |
| GET | `/api/pecas/{id}` | Buscar por ID |
| GET | `/api/pecas/buscar?codigo=` | Buscar por código nacional |
| GET | `/api/pecas/estoque-baixo?min=5` | Estoque abaixo do mínimo |
| POST | `/api/pecas` | Criar |
| PUT | `/api/pecas/{id}` | Atualizar |
| PATCH | `/api/pecas/{id}/toggle-status` | Alternar ativo/inativo |

### Parceiros Externos
| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/api/parceiros?inativos=false` | Listar todos |
| GET | `/api/parceiros/{id}` | Buscar por ID |
| GET | `/api/parceiros/buscar?nome=` | Buscar por nome |
| POST | `/api/parceiros` | Criar |
| PUT | `/api/parceiros/{id}` | Atualizar |
| PATCH | `/api/parceiros/{id}/toggle-status` | Alternar ativo/inativo |

## 10. Integração Swing — Fila de Espera e Ordenação

### 10.1 FilaEsperaDialog
- JDialog modal acessado pelo botão **"Fila de Espera"** na toolbar de Ordens de Serviço
- Utiliza uma instância singleton de `FilaEsperaOS<String>` para gerenciar a fila de números de OS
- Interface: campo de texto + botão **Adicionar (Enqueue)** | botão **Remover (Dequeue)** | botão **Próximo (Peek)** | botão **Limpar Tudo**
- Itens exibidos em `JList` com fonte monoespaçada
- A fila persiste durante toda a sessão do Swing (não é enviada ao servidor)

### 10.2 Ordenação por Cabeçalho de Tabela
- `OrdemServicoPanel` implementa `MouseListener` no cabeçalho da `JTable`
- Ao clicar em uma coluna, os dados são ordenados via `OrdenacaoOS.quickSort()` usando `Comparator` extraído do campo correspondente do DTO
- Segundo clique na mesma coluna inverte a direção (ASC ↔ DESC)
- Colunas suportadas: ID, Número, Veículo, Cliente, Responsável, Status, Abertura, Entrada, Valor, Desconto

## 11. Estruturas de Dados Implementadas

### 11.1 FilaEsperaOS\<T\> — Fila Circular Genérica

**Pacote:** `br.edu.senai.fatesg.avcar.datastructures` | **Arquivo:** `FilaEsperaOS.java`

Fila circular (FIFO) implementada com array de objetos redimensionável. Mantém ponteiros `front` (cabeça) e `rear` (cauda) para operações O(1).

| Método | Complexidade | Descrição |
|--------|-------------|-----------|
| `enqueue(T item)` | O(1)* | Insere ao final. *O(n) quando redimensiona |
| `dequeue()` | O(1) | Remove e retorna o elemento da frente |
| `peek()` | O(1) | Retorna o elemento da frente sem remover |
| `isEmpty()` | O(1) | Verifica se a fila está vazia |
| `isFull()` | O(1) | Verifica se a fila está cheia (capacidade atual) |
| `size()` | O(1) | Retorna a quantidade de elementos |
| `capacity()` | O(1) | Retorna a capacidade atual do array |
| `listar()` | O(n) | Retorna `List<T>` com todos os elementos em ordem FIFO |

**Redimensionamento:** Quando o array atinge capacidade máxima, um novo array com o dobro do tamanho é alocado e os elementos são copiados na ordem FIFO correta.

**Uso no sistema:** Gerenciamento de fila de espera de ordens de serviço via `FilaEsperaDialog` (Swing).

### 11.2 OrdenacaoOS — MergeSort e QuickSort Manuais

**Pacote:** `br.edu.senai.fatesg.avcar.datastructures` | **Arquivo:** `OrdenacaoOS.java`

Implementações didáticas de ordenação sem uso de bibliotecas padrão (`Collections.sort`, `Arrays.sort`, `Stream.sorted`, `List.sort`).

#### MergeSort
- Método: `mergeSort(List<T> lista, Comparator<T> c)`
- Estratégia: Divisão recursiva ao meio → ordenação das metades → merge O(n) com arrays auxiliares
- Complexidade: O(n log n) garantido (melhor, médio e pior caso)
- Estável: sim
- Uso: Ordenação previsível e estável, recomendada quando a ordem relativa de elementos iguais importa

#### QuickSort
- Método: `quickSort(List<T> lista, Comparator<T> c)`
- Estratégia: Lomuto partition com mediana `(low+high)/2` como pivô → partição in-place → recursão nas sublistas
- Complexidade: O(n log n) médio, O(n²) pior caso (pivô não-ótimo)
- Estável: não
- Uso: Ordenação rápida in-place, usada na UI para ordenar tabela ao clicar no cabeçalho (`OrdemServicoPanel`)

### 11.3 CalculoOS — Funções Recursivas

**Pacote:** `br.edu.senai.fatesg.avcar.datastructures` | **Arquivo:** `CalculoOS.java`

Implementações de funções recursivas puras (sem iteração) para demonstrar o paradigma de recursão.

#### somarValores
- Assinatura: `somarValores(List<Double> valores, int indice) → double`
- Caso base: `indice >= lista.size()` → retorna `0`
- Passo recursivo: `lista.get(indice) + somarValores(lista, indice + 1)`
- Uso: Cálculo de total de serviços, peças e serviços externos em `calcularValorTotal()`

#### fatorial
- Assinatura: `fatorial(int n) → long`
- Caso base: `n <= 1` → retorna `1`
- Passo recursivo: `n * fatorial(n - 1)`
- Uso: Cálculo didático de permutações (ex: número de formas de agendar N serviços)

#### calcularValorTotal
- Assinatura: `calcularValorTotal(List<Double> servicos, List<Double> pecas, List<Double> externos, double desconto) → double`
- Soma recursiva de cada lista via `somarValores()` e aplica desconto (limitado ao total)

## 12. Erro Padrão

Todas as respostas de erro seguem o formato `ApiResponse`:

```json
{
  "sucesso": false,
  "mensagem": "mensagem do erro",
  "dados": null,
  "erros": ["mensagem do erro"]
}
```

---

*Documento gerado em Junho de 2026. Para o schema DDL atualizado, consulte `db/schema.sql`.*
