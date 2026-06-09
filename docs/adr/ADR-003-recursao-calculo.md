# ADR-003: Função Recursiva para Cálculo de Valor Total da OS

## Status
Aprovado

## Contexto
O cálculo do valor total de uma ordem de serviço envolve somar múltiplas
listas aninhadas (itens de serviço, itens de peça, serviços externos) e
aplicar descontos. A estrutura naturalmente hierárquica sugere uma
solução recursiva.

## Decisão
Implementar **funções recursivas** em `CalculoOS` para percorrer e somar
os componentes de uma OS, usando recursão linear nas listas.

## Consequências
- Positivas: Código mais legível e alinhado com a estrutura hierárquica
  dos dados; atende requisito didático da disciplina
- Negativas: Risco de StackOverflow para listas muito grandes (milhares
  de itens) — mitigado pelo limite prático de itens por OS (< 100)

## Alternativas Consideradas
| Alternativa | Motivo para aceitar/rejeitar |
|-------------|------------------------------|
| Loop iterativo (for/while) | Rejeitado: não atende requisito de recursão |
| Stream.reduce() | Rejeitado: não atende requisito de recursão manual |
| Recursão em árvore | Rejeitado: OS não tem estrutura de árvore, apenas listas |

## Estrutura Recursiva
```
calcularValorTotal(os)
  ├── somarServicos(itens, 0)
  │   ├── base: i >= tamanho → 0
  │   └── passo: subtotal + somarServicos(itens, i+1)
  ├── somarPecas(itens, 0)
  │   └── (mesmo padrão)
  ├── somarExternos(externos, 0)
  │   └── (mesmo padrão)
  └── aplicarDescontos(total)
```

## Referências
- `docs/specs/especificacao-estrutura-dados.md` seção 2.3
