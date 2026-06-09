# Plano de Execução — Recursos de Estrutura de Dados

## Dependência
- `docs/specs/especificacao-estrutura-dados.md` (especificação)

---

## T1: Criar pacote `datastructures` e classe `FilaEsperaOS`

**Arquivo:** `src/main/java/br/edu/senai/fatesg/avcar/datastructures/FilaEsperaOS.java`

**Descrição:** Implementar fila circular FIFO com array redimensionável.

**Detalhes:**
- Tipo genérico `<T>` para reuso em qualquer entidade
- Array interno `Object[]` iniciado com capacidade 10
- Ponteiros `inicio` (front) e `fim` (rear) circulares
- Métodos: `enqueue(T)`, `dequeue()`, `peek()`, `isEmpty()`, `isFull()`, `size()`, `listar()`
- `crescer()`: dobra o tamanho do array quando cheio, copiando elementos na ordem FIFO
- Javadoc explicando uso, justificativa e complexidade O(1) para enqueue/dequeue

**Verificação:** Escrever `main()` com cenário: enfileirar 5 OSs, remover 2,
enfileirar mais 3, verificar ordem FIFO.

---

## T2: Implementar MergeSort manual

**Arquivo:** `src/main/java/br/edu/senai/fatesg/avcar/datastructures/OrdenacaoOS.java`

**Descrição:** Implementar MergeSort do zero (sem `Arrays.sort`, `Collections.sort`,
`Stream.sorted`, `List.sort` ou similar).

**Detalhes:**
- `mergeSort(List<OrdemServicoDTO>, Comparator<OrdemServicoDTO>)`: método público
- `mergeSortRecursivo(List<OrdemServicoDTO>, int left, int right, Comparator)`: recursive
- `merge(List<OrdemServicoDTO>, int left, int mid, int right, Comparator)`: intercalação
- Três comparadores pré-definidos como constantes: `POR_DATA`, `POR_VALOR`, `POR_NUMERO`
- Métodos helpers: `porDataAbertura(...)`, `porValorTotal(...)`, `porNumero(...)`
- Javadoc: complexidade O(n log n), estável, justificativa

**Verificação:** `main()` ordena lista de 10 OSs por data, valor e número;
compara visualmente se ordem está correta.

---

## T3: Implementar QuickSort manual (alternativa didática)

**Arquivo:** `src/main/java/br/edu/senai/fatesg/avcar/datastructures/OrdenacaoOS.java`

**Descrição:** Implementar QuickSort manual como segunda opção de ordenação.

**Detalhes:**
- `quickSort(List<OrdemServicoDTO>, Comparator)`: método público
- `quickSortRecursivo(List<OrdemServicoDTO>, int low, int high, Comparator)`: recursivo
- `partition(List<OrdemServicoDTO>, int low, int high, Comparator)`: pivô central
- Javadoc: complexidade O(n²) pior caso / O(n log n) médio, instável

**Verificação:** mesma lista ordenada com QuickSort, resultados idênticos ao
MergeSort.

---

## T3.5: Implementar Algoritmos de Busca

**Arquivo:** `src/main/java/br/edu/senai/fatesg/avcar/datastructures/BuscaOS.java`

**Descrição:** Implementar algoritmos de Busca Linear (parcial textual) e Busca Binária (exata em lista ordenada).

**Detalhes:**
- `buscaLinear(List<T>, Function<T, String>, String)`: O(n) percorrendo e comparando de forma "contains".
- `buscaBinaria(List<T>, T, Comparator<T>)`: O(log n) com divisão e conquista.
- Integrar com barra de busca na interface Swing.

**Verificação:** Pesquisar OS por termo na interface (ex: placa), e confirmar se seleciona a linha correta.

---

## T4: Implementar funções recursivas de cálculo

**Arquivo:** `src/main/java/br/edu/senai/fatesg/avcar/datastructures/CalculoOS.java`

**Descrição:** Funções recursivas para cálculo de valor total da OS.

**Detalhes:**
- `calcularValorTotal(OrdemServicoDTO)`: soma valorTotal + itens - descontos
  usando chamadas recursivas auxiliares
- `somarServicos(List<ItemServicoDTO>, int i)`: recursão linear na lista
  — base: `i >= lista.size()` → 0; passo: `lista.get(i).getSubtotal() + soma(lista, i+1)`
- `somarPecas(List<ItemPecaDTO>, int i)`: idem para peças
- `somarExternos(List<ServicoExternoDTO>, int i)`: idem para serviços externos
- `calcularFatorial(int n)`: função recursiva clássica (didática) para
  demonstrar recursão simples (ex: calcular permutações de agendamento)
- Javadoc explicando cada função, caso base e passo recursivo

**Verificação:** `main()` calcula valor de OS com 2 serviços + 3 peças + 1 serv.
externo; confere resultado manualmente.

---

## T5: Integrar Fila de Espera na tela Swing

**Arquivos:**
- `src/main/java/br/edu/senai/fatesg/avcar/swing/views/FilaEsperaDialog.java` (NOVO)
- Modificar `OrdemServicoPanel.java`

**Descrição:** Adicionar botão "Fila de Espera" no painel de OS que abre
diálogo com a fila atual.

**Detalhes:**
- `FilaEsperaDialog`: JDialog modal mostrando tabela com as OS da fila
  (número, cliente, veículo, data de entrada na fila)
- Botões: "Adicionar à Fila" (OS selecionada), "Remover da Fila" (volta
  para Orçamento), "Fechar"
- Implementar usando a `FilaEsperaOS` como singleton (`ConfigManager`)
- Javadoc indicando integração com a fila customizada

**Verificação:** Abrir o diálogo, adicionar OS em "Aguardando Peça", remover,
verificar ordem FIFO visualmente.

---

## T6: Integrar Ordenação nos cabeçalhos das JTables

**Arquivo:** Modificar `AbstractPanel.java` e painéis específicos

**Descrição:** Ao clicar no cabeçalho de uma coluna, ordenar a tabela usando
o MergeSort implementado.

**Detalhes:**
- Adicionar `MouseListener` no cabeçalho da JTable
- Mapear índice da coluna para o campo correspondente no DTO
- Chamar `OrdenacaoOS.mergeSort(lista, comparador)` ao clicar
- Alternar entre ascendente/descendente a cada clique

**Verificação:** Clicar nas colunas "Data" e "Valor" na aba OS, verificar
ordenação correta.

---

## T7: Compilar e executar testes

**Descrição:** Garantir `mvn clean compile` com ZERO erros.

**Detalhes:**
- Verificar se não há imports de `java.util.Collections` para sort
- Verificar se não há chamadas a `Arrays.sort`, `List.sort`, `Stream.sorted`
- Rodar `main()` de cada classe de estrutura de dados
- Executar API (`java -jar`) e Swing para verificar integração

**Verificação:** `mvn clean compile` → BUILD SUCCESS
