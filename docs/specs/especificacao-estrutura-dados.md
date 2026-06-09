# Especificação — Recursos de Estrutura de Dados

## 1. Visão Geral

Implementação de estruturas de dados lineares, algoritmos de ordenação e funções
recursivas no sistema AV-CAR Auto Center para atender aos requisitos da disciplina
Estrutura de Dados I.

**Sistema alvo:** AV-CAR Auto Center — gestão de oficina mecânica
**Stack:** Java 21, Spring Boot 3.4.1, JdbcTemplate, PostgreSQL, Java Swing
**Estilo arquitetural:** Monolito em camadas com Generics

## 2. Recursos Implementados

### 2.1 Estrutura Linear: Fila de Espera de Ordens de Serviço

**Tipo:** Fila (FIFO — First In, First Out)

**Onde:** `src/main/java/br/edu/senai/fatesg/avcar/datastructures/FilaEsperaOS.java`

**Propósito:** Gerenciar ordens de serviço que estão no status "Aguardando Peça".
Quando uma peça chega ao estoque, a OS que está há mais tempo esperando é
automaticamente notificada para retornar à execução.

**Justificativa:** O modelo FIFO reflete o comportamento real de uma oficina:
a primeira OS que fica aguardando peça deve ser a primeira a ser retomada quando
a peça chega. Uma fila circular com array é escolhida por:
- Performance O(1) para inserção e remoção
- Tamanho máximo previsível (limitado pelo número de ordens em aberto)
- Sem alocação dinâmica por nó (menos overhead que LinkedList)

**Operações:**
- `enqueue(OrdemServicoDTO os)`: Adiciona OS ao final da fila
- `dequeue()`: Remove e retorna a OS do início
- `peek()`: Consulta a primeira OS sem remover
- `isEmpty()`, `isFull()`: Estado da fila
- `size()`: Quantidade de elementos
- `listar()`: Retorna todas as OS na fila sem remover

**Capacidade:** Array estático redimensionável (cresce automaticamente quando
cheia, seguindo o padrão `crescer()`)

### 2.2 Algoritmo de Ordenação: Merge Sort

**Onde:** `src/main/java/br/edu/senai/fatesg/avcar/datastructures/OrdenacaoOS.java`

**Propósito:** Ordenar listas de ordens de serviço por diferentes critérios
(data de abertura, valor total, número da OS) sem uso de bibliotecas prontas
(`Collections.sort`, `Arrays.sort`, `Stream.sorted`).

**Justificativa:** Merge Sort é escolhido por:
- Complexidade O(n log n) garantida (pior caso)
- Ordenação estável (preserva ordem relativa de elementos com chaves iguais)
- Naturalmente recursivo — aproveita para também atender ao requisito de
  função recursiva
- Bom para estruturas de dados lineares (arrays e listas)

**Critérios de ordenação:**
- `porDataAbertura(lista, crescente)`: Ordena OS por `dataAbertura`
- `porValorTotal(lista, crescente)`: Ordena OS por `valorTotal`
- `porNumero(lista, crescente)`: Ordena OS por `numero` (ordem lexicográfica)

### 2.3 Função Recursiva: Cálculo de Valor Total

**Onde:** `src/main/java/br/edu/senai/fatesg/avcar/datastructures/CalculoOS.java`

**Propósito:** Calcular recursivamente o valor total de uma ordem de serviço,
incluindo itens de serviço, itens de peça, serviços externos e descontos
aplicados em cascata.

**Justificativa:** A estrutura de uma OS é naturalmente recursiva:
- Uma OS contém N itens de serviço
- Cada item pode ter descontos ou acréscimos
- Serviços externos podem ter garantias que alteram o valor final
- O cálculo recursivo permite processar essa hierarquia de forma limpa

**Funções:**
- `calcularValorTotal(OrdemServicoDTO os)`: Ponto de entrada — soma valor
  base + valor itens + valor externos - descontos
- `somarItensServico(List<ItemServicoDTO> itens, int indice)`: Percorre
  recursivamente a lista de itens de serviço somando `quantidade * valorUnitario`
- `somarItensPeca(List<ItemPecaDTO> itens, int indice)`: Percorre recursivamente
  a lista de itens de peça somando `quantidade * precoUnitario`
- `somarServicosExternos(List<ServicoExternoDTO> externos, int indice)`:
  Percorre recursivamente a lista de serviços externos

### 2.4 Recursividade Cruzada: QuickSort + MergeSort

**Onde:** `datastructures/OrdenacaoOS.java`

**Propósito:** Demonstrar dois algoritmos de ordenação diferentes — MergeSort
(recursivo) e QuickSort (recursivo) — para efeito de comparação didática.

**Critério adicional:** QuickSort implementado como alternativa, usando o
elemento central como pivô.

### 2.5 Algoritmo de Busca: Busca Linear e Binária

**Onde:** `src/main/java/br/edu/senai/fatesg/avcar/datastructures/BuscaOS.java`

**Propósito:** Prover métodos utilitários genéricos para localizar elementos em listas usando Busca Linear (para buscas por termo textual/parcial) e Busca Binária (para buscas exatas em listas previamente ordenadas).

**Justificativa:** A busca linear atende ao requisito de pesquisar OS por trechos (ex: placa ou nome do cliente) na tabela da interface sem necessitar pré-ordenação. A busca binária aplica a técnica de divisão e conquista O(log n) exigida pela disciplina, demonstrando eficiência em buscas exatas.

## 3. Integração com o Sistema Existente

### 3.1 Fila de Espera na Interface Swing

A aba "Ordens de Serviço" ganha um botão **"Fila de Espera"** que abre
`FilaEsperaDialog` — um diálogo que exibe a fila atual de OSs aguardando peça,
com opções para:
- **Adicionar à fila**: Coloca a OS selecionada na fila (se estiver em
  "Aguardando Peça")
- **Remover da fila**: Retira a OS da fila e avança para "Orçamento"
  (peça chegou)
- **Ver próxima**: Mostra qual é a próxima OS a ser processada

### 3.2 Ordenação na Interface Swing

Cada painel com JTable (Clientes, OS, Peças, etc.) ganha a opção de ordenar
as colunas clicando no cabeçalho, usando o MergeSort implementado.

### 3.3 Cálculo Recursivo na Finalização da OS

Quando uma OS é finalizada, o `OrdemServicoService.finalizar()` chama
`CalculoOS.calcularValorTotal()` para exibir o detalhamento no diálogo de
confirmação.

## 4. Estrutura de Diretórios (nova)

```
src/main/java/br/edu/senai/fatesg/avcar/datastructures/
├── FilaEsperaOS.java           # Fila circular genérica (array redimensionável)
├── OrdenacaoOS.java            # MergeSort + QuickSort manuais
├── CalculoOS.java              # Funções recursivas de cálculo
└── BuscaOS.java                # Algoritmos de busca linear e binária genéricos
```

## 5. Documentação Técnica Exigida

Para cada estrutura/algoritmo, o código fonte conterá comentários Javadoc
indicando:
- Onde foi utilizada (classes/métodos que a consomem)
- Justificativa de escolha
- Complexidade assintótica (Big O)

Além disso, este documento e o plano de execução servem como registro
técnico da decisão e implementação.

## 6. Critérios de Aceitação

1. `FilaEsperaOS` deve funcionar como uma fila FIFO completa com array interno
2. O array deve redimensionar automaticamente quando a capacidade esgotar
3. `OrdenacaoOS.mergeSort()` deve ordenar corretamente sem usar bibliotecas
4. `OrdenacaoOS.quickSort()` deve ordenar corretamente sem usar bibliotecas
5. `CalculoOS.calcularValorTotal()` deve ser recursiva e retornar valor correto
6. Nenhuma das funções pode usar `Collections.sort`, `Arrays.sort`,
   `Stream.sorted`, `List.sort` ou qualquer método pronto de ordenação
7. Compilação: `mvn clean compile` deve ter ZERO erros
