# ADR-001: Fila Circular para Gerenciamento de OS Aguardando Peça

## Status
Aprovado

## Contexto
Ordens de serviço que entram no status "Aguardando Peça" precisam ser
retomadas quando a peça chega ao estoque. Sem uma estrutura de dados
dedicada, a ordem de retorno seria arbitrária ou dependeria de consultas
SQL sem priorização clara.

## Decisão
Implementar uma **fila circular FIFO com array redimensionável**
(`FilaEsperaOS<T>`) para gerenciar a ordem de retorno das OSs.

## Consequências
- Positivas: O(1) para enqueue/dequeue, previsível, justo (FIFO)
- Negativas: Tamanho máximo inicial fixo (redimensionável com custo O(n)
  quando necessário)

## Alternativas Consideradas
| Alternativa | Motivo para rejeitar |
|-------------|----------------------|
| LinkedList | Overhead de nó por objeto, menos cache-friendly |
| PriorityQueue | Complexidade maior, desnecessário para FIFO puro |
| Consulta SQL direta | Acoplamento com banco, sem estrutura didática |

## Referências
- `docs/specs/especificacao-estrutura-dados.md` seção 2.1
