# Tasks — AV-CAR Auto Center

## Legenda
- [x] Concluído
- [ ] Pendente

---

## Fase 1: Estrutura do Projeto

- [x] Criar parent POM multi-módulo
- [x] Criar módulo `av-car-core` com dependências (spring-jdbc, jackson)
- [x] Criar módulo `av-car-api` com dependências (spring-boot-starter-web, spring-boot-starter-jdbc, postgresql)
- [x] Criar módulo `av-car-swing` com dependências (jackson)
- [x] Configurar `application.yml` com datasource PostgreSQL
- [x] Criar `AvCarApplication.java` (Spring Boot main)
- [x] Criar `AvCarSwingApp.java` (Swing main)

## Fase 2: Domínios (Core)

- [x] Implementar `Cliente` (abstract), `PessoaFisica`, `PessoaJuridica`
- [x] Implementar `Marca`, `Modelo`, `Veiculo`, `HistoricoClienteVeiculo`
- [x] Implementar `Colaborador`, `Funcao`
- [x] Implementar `Fornecedor`
- [x] Implementar `Peca`, `ItemPeca`
- [x] Implementar `Servico`, `StatusOrdemServico` (enum), `OrdemServico`, `ItemServico`, `ServicoExterno`
- [x] Implementar `ParceiroExterno`
- [x] Implementar DTOs: `ClienteDTO`, `OrdemServicoDTO`, `VeiculoDTO`, `ServicoDTO`
- [x] Implementar Exceções: `NegocioException`, `EntidadeNaoEncontradaException`
- [x] Implementar Helpers: `FormatadorHelper`, `ClienteHelper`
- [x] Implementar Validações: `Validator<T>`, `ClienteValidator`
- [x] Implementar interfaces de Repositório: `Repository<T,ID>`, `ClienteRepository`, `VeiculoRepository`, `OrdemServicoRepository`, `PecaRepository`, `ServicoRepository`

## Fase 3: Design Patterns

- [x] **Singleton** — `ConfigManager` com `getInstance()`
- [x] **Factory Method** — `ClienteFactory` criando PF/PJ
- [x] **Iterator** — `OrdemServicoIterator` filtrando por status
- [x] **Template Method** — `OrdemServicoTemplate` + `OrdemServicoOrcamentoTemplate`, `OrdemServicoExecucaoTemplate`, `OrdemServicoPagamentoTemplate`
- [x] **Adapter** — `ParceiroExternoAdapter` adaptando `ServicoExterno` para `Servico`
- [x] **Decorator** — `OrdemServicoDecorator` (abstract) + `OrdemServicoGarantiaDecorator`, `OrdemServicoDescontoDecorator`

## Fase 4: API REST (Spring Boot)

- [x] Configurar `DatabaseConfig` (JdbcTemplate)
- [x] Configurar `AppConfig` (ConfigManager bean)
- [x] Implementar `ClienteRepositoryImpl` (JdbcTemplate)
- [x] Implementar `VeiculoRepositoryImpl` (JdbcTemplate)
- [x] Implementar `OrdemServicoRepositoryImpl` (JdbcTemplate — JOINs + KeyHolder)
- [x] Implementar `ServicoRepositoryImpl` (JdbcTemplate)
- [x] Implementar `ClienteService`
- [x] Implementar `VeiculoService` (com vínculo cliente-veículo)
- [x] Implementar `OrdemServicoService`
- [x] Implementar `ServicoService`
- [x] Implementar `ParceiroExternoService`
- [x] Implementar `ClienteController` (CRUD + busca + soft delete)
- [x] Implementar `VeiculoController` (CRUD + marcas/modelos + soft delete)
- [x] Implementar `OrdemServicoController` (CRUD + avanço status + decorators + itens + garantia)
- [x] Implementar `ServicoController` (CRUD + soft delete)
- [x] Adicionar coluna `ativo` na tabela `servico` + migração DB
- [x] Tornar `responsavel_id` NOT NULL na `ordem_servico` (ATA: obrigatório atrelar colaborador)
- [x] Implementar endpoints de itens da OS: GET/POST/DELETE para `itens-servico`, `itens-peca`, `servicos-externos`
- [x] Implementar `GET /{id}/garantia` — cálculo de garantia baseado em `data_finalizacao + prazo_garantia_dias`

## Fase 5: Cliente Swing

### Status Atual (31/05/2026)

| Painel | Status | Funcional |
|--------|--------|-----------|
| `ClientePanel` | ✅ Completo | CRUD, busca, soft delete, mostrar inativos |
| `VeiculoPanel` | ✅ Completo | CRUD, marcas/modelos com logo, cliente vinculado, soft delete, mostrar inativos |
| `OrdemServicoPanel` | ✅ Completo | Lista, criação com dialog (veículo/cliente/responsável), avanço status, gerenciar itens (3 abas), ver garantia, garantia estendida, desconto |
| `ServicoPanel` | ✅ Completo | CRUD, busca, soft delete, mostrar inativos |
| `PecaPanel` | ✅ Completo | CRUD, busca, soft delete, mostrar inativos |
| `FornecedorPanel` | ✅ Completo | CRUD, busca, soft delete, mostrar inativos |
| `ColaboradorPanel` | ✅ Completo | CRUD, busca, soft delete, mostrar inativos |
| `ParceiroPanel` | ✅ Completo | CRUD, busca, soft delete, mostrar inativos |

### Itens da OS (31/05/2026)

- [x] DTOs: `ItemServicoDTO`, `ItemPecaDTO`, `ServicoExternoDTO`
- [x] Repository: 12 métodos para adicionar/remover/listar itens + `somarValorItens()`
- [x] Service: injeção de ServicoRepository, PecaRepository, ParceiroExternoRepository para validação
- [x] Controller: endpoints REST para cada tipo de item
- [x] Swing: `ItensOSDialog` com JTabbedPane (3 abas) + botões Adicionar/Remover
- [x] Botão "Gerenciar Itens" no `OrdemServicoPanel`

### Garantia (31/05/2026)

- [x] `GarantiaDTO`: tipo, item, dataFinalizacao, dataVencimento, diasRestantes, vencida
- [x] Service: `calcularGarantia(Long id)` percorre itens e soma prazos via `dataFinalizacao + prazo_garantia_dias`
- [x] Controller: `GET /{id}/garantia`
- [x] Swing: botão "Ver Garantia" + JTextArea dialog exibindo todas as garantias

### Bugs Fixados

- [x] `OrdemServicoTemplate.recalcularValor()` sobrescrevia `valorTotal` para 0 porque iterava listas vazias do domínio; sobrescrito como no-op nos 3 templates
- [x] `somarValorItens()` migrado de `queryForObject(Double.class)` para RowMapper por incompatibilidade de tipo (PostgreSQL SUM retorna BigDecimal)
- [x] `OrdemServicoOrcamentoTemplate.validarTransicao()` removida validação de itens (domínio não carrega listas); movida para service via `somarValorItens() > 0`



- [x] `ApiClient` com métodos: `getList`, `getOne`, `post`, `postWithResponse`, `put`, `patch`, `delete`
- [x] `AtivoCellRenderer` — bolinha verde/vermelha para coluna "Ativo"
- [x] Checkbox "Mostrar inativos" em todos os painéis
- [x] Duplo-clique para editar em todos os painéis
- [x] Marca/Modelo com logotipos no `VeiculoDialog`
- [x] `OrdemServicoDialog` com seleção de veículo, cliente e responsável (obrigatório)
- [x] `ServicoPanel` + `ServicoDialog` com seleção de colaborador responsável
- [x] `ItensOSDialog` — diálogo com 3 abas (Serviços, Peças, Serv. Externos) + Adicionar/Remover

## Fase 6: Banco de Dados

- [x] Criar `db/schema.sql` com DDL completo (21 tabelas — incluindo cliente_pf, cliente_pj)
- [x] **Executar schema.sql no PostgreSQL** (container Docker)
- [x] **Popular dados iniciais (marcas, modelos, funções, fornecedores, colaboradores)**
- [x] Migração: adicionar coluna `ativo` em todas as tabelas (cliente, veiculo, colaborador, fornecedor, peca, servico, parceiro_externo)
- [x] Migração: alterar `responsavel_id` para NOT NULL na `ordem_servico`

## Fase 7: Build e Qualidade

- [x] `mvn clean compile` — **BUILD SUCCESS**
- [x] `mvn clean package -DskipTests` — Gerar JARs
- [x] Testar execução da API (`java -jar av-car-api-1.0.0.jar`)
- [x] Testar execução do Swing (`java -jar av-car-swing-1.0.0.jar`)

## Fase 8: Gaps do MER/ATA — Implementados

### Cliente
- [x] `RG`, `dataNascimento` via tabela `cliente_pf`
- [x] `razaoSocial`, `inscricaoEstadual` via tabela `cliente_pj`
- [x] Endereço completo: `bairro`, `cidade`, `estado`, `cep`
- [x] `observacoes` no cliente
- [x] Soft delete (`ativo`) na tabela `cliente`

### Colaborador
- [x] `matricula` (auto-gerada como "COL"+id)
- [x] `dataAdmissao`, `dataDemissao`
- [x] `salario`, `observacoes`
- [x] Soft delete (`ativo`)

### Função
- [x] `especialidade`
- [x] `comissao` (percentual)

### Serviço
- [x] `tempoEstimado`

### Peça
- [x] `codigoInterno`, `fabricante`, `categoria`, `dataCompra`
- [x] Soft delete (`ativo`)

### Fornecedor
- [x] `razaoSocial`
- [x] Endereço completo: `bairro`, `cidade`, `estado`, `cep`
- [x] Soft delete (`ativo`)

### OS — Status
- [x] `ABERTA` (pré-orçamento)
- [x] `AGUARDANDO_PECA` (pausa)
- [x] `CANCELADA` (terminal)
- [x] Fluxo: ABERTA → ORCAMENTO → (AGUARDANDO_PECA) → EXECUCAO → AGUARDANDO_PAGAMENTO → FINALIZADA

### Itens da OS
- [x] `horaInicio`, `horaFim` nos itens de serviço
- [x] `status` individual do item de serviço
- [x] Endpoints: POST `/{id}/cancelar`, POST `/{id}/pausar`, POST `/{id}/avancar/finalizar`

### Veículo
- [x] Soft delete (`ativo`) com toggle-status

## Fase 8a: Refatoração Monolito + Generics (AQS)

### Estrutura
- [x] Fundir 3 módulos Maven (`av-car-core`, `av-car-api`, `av-car-swing`) em um único `av-car`
- [x] Consolidar `src/main/java` e `src/main/resources` dos 3 módulos
- [x] Separar a arquitetura em pacotes `core` (base genérica) e `business` (regras de negócio e domínios)
- [x] Mover as classes de entidades de domínio para `business/` e classes base genéricas para `core/`

### Model / DTO (Fase 2 AQS)
- [x] Criar `core/domains/BaseModel.java` com `id` (Long) + `ativo` (boolean)
- [x] Fazer Cliente, Veiculo, Colaborador, Fornecedor, Peca, Servico, ParceiroExterno estenderem `BaseModel` (agora dentro de `business/`)
- [x] OrdemServico estende `BaseModel` (herda `id` e `ativo`), mas `ativo` não mapeado no banco
- [x] Criar `core/dtos/BaseDTO.java` com `id` + `ativo`
- [x] Fazer todos os DTOs estenderem `BaseDTO`

### Repository (Fase 3 AQS)
- [x] Refatorar interface `Repository<T, ID>` para `core/repositories/Repository<T>`
- [x] Criar `core/repositories/AbstractRepository.java` com `deletar()` e `toggleStatus()` genéricos
- [x] Refatorar repositórios em `business/` para estender `AbstractRepository`

### Validator (Fase 4 AQS)
- [x] Criar interfaces `Validator` e `IGenericValidation` e classe `GenericValidation` em `core/validations/`
- [x] Refatorar validações de `business/` para utilizar as validações genéricas

### Service (Fase 5 AQS)
- [x] Criar `core/services/GenericService.java` com `listarTodos()`, `buscarPorId()`, `toggleStatus()`
- [x] Refatorar services em `business/` para estender `GenericService`

### Controller (Fase 6 AQS)
- [x] Criar `core/controllers/response/ApiResponse.java` com respostas padronizadas
- [x] Criar `core/controllers/GenericController.java` com endpoints CRUD básicos genéricos
- [x] Refatorar controllers em `business/` para estender `GenericController`

### View (Fase 7 AQS)
- [x] Criar `AbstractPanel<D extends BaseDTO>` com toolbar, JTable, duplo-clique, AtivoCellRenderer
- [x] Criar `AbstractDialog<D extends BaseDTO>` com helpers de layout e formulário
- [x] Refatorar `FornecedorPanel extends AbstractPanel<FornecedorDTO>`
- [x] Refatorar `FornecedorDialog extends AbstractDialog<FornecedorDTO>`
- [x] Refatorar `ServicoPanel extends AbstractPanel<ServicoDTO>`
- [x] Refatorar `ServicoDialog extends AbstractDialog<ServicoDTO>`
- [x] Refatorar `ParceiroPanel extends AbstractPanel<ParceiroExternoDTO>`
- [x] Refatorar `ParceiroDialog extends AbstractDialog<ParceiroExternoDTO>`
- [ ] PecaPanel/Dialog mantidos próprios (JComboBox fornecedor)
- [ ] ColaboradorPanel/Dialog mantidos próprios (JList multi-função)
- [ ] ClientePanel/Dialog mantidos próprios (PF/PJ campos condicionais)
- [ ] VeiculoPanel/Dialog mantidos próprios (marca/modelo com logo)
- [ ] OrdemServicoPanel/Dialog mantidos próprios (ciclo de vida, itens, garantia)

### Build (Fase 8 AQS)
- [x] `mvn clean compile` — BUILD SUCCESS
- [x] `mvn clean package -DskipTests` — `target/av-car-1.0.0.jar` gerado
- [ ] Testes funcionais da API (CRUD + soft delete + busca em todas as entidades)
- [ ] Testes funcionais do Swing (todos os painéis)
- [ ] Testes do ciclo de vida da OS (avanço, pausa, cancelamento, garantia, desconto)

## Fase 9: Melhorias Futuras

- [ ] **Autenticação** — Login com HTTP Session + JSESSIONID
- [ ] **Relatórios** — Geração de relatórios em PDF (OS finalizada, garantias)
- [ ] **Notificações** — Alertas de garantia próxima do vencimento
- [ ] **Dashboard** — Visão geral com contadores (OS por status, estoque baixo)
- [ ] **Logs** — Auditoria de operações realizadas
- [ ] **Testes** — Testes unitários e de integração (milestone separada)
