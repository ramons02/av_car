# ADR-002: MergeSort como Algoritmo de Ordenação Principal

## Status
Aprovado

## Contexto
O sistema precisa ordenar listas de ordens de serviço por diferentes
critérios (data, valor, número) para exibição na interface, sem usar
bibliotecas prontas de ordenação.

## Decisão
Implementar **MergeSort manual** (`OrdenacaoOS`) como algoritmo de
ordenação principal, com QuickSort como alternativa didática.

## Consequências
- Positivas: O(n log n) garantido, ordenação estável, implementação
  naturalmente recursiva (atende duplo propósito)
- Negativas: O(n) de espaço extra para o merge, overhead de cópia

## Alternativas Consideradas
| Alternativa | Motivo para aceitar/rejeitar |
|-------------|------------------------------|
| BubbleSort | Rejeitado: O(n²) lento para listas grandes |
| QuickSort | Aceito como alternativa: O(n log n) médio, mas instável |
| InsertionSort | Rejeitado: O(n²) embora melhor para dados quase ordenados |
| Collections.sort() | Rejeitado: requisito explícito de implementação manual |

## Referências
- `docs/specs/especificacao-estrutura-dados.md` seção 2.2
