package br.edu.senai.fatesg.avcar.datastructures;

import java.util.List;

public class CalculoOS {

    private CalculoOS() {}

    // ---------------------------------------------------------------
    // SOMADOR RECURSIVO GENÉRICO
    // Percorre a lista do índice `i` até o final, acumulando valores.
    // Caso base: i >= lista.size() → 0
    // Passo recursivo: lista.get(i) + somar(lista, i + 1)
    // ---------------------------------------------------------------

    public static double somarValores(List<Double> valores, int i) {
        if (i >= valores.size()) return 0;
        return valores.get(i) + somarValores(valores, i + 1);
    }

    // ---------------------------------------------------------------
    // CÁLCULO DO VALOR TOTAL DA OS
    // Extrai os valores das listas de itens e aplica recursão.
    // ---------------------------------------------------------------

    public static double calcularValorTotal(
            List<Double> servicos,
            List<Double> pecas,
            List<Double> externos,
            double valorDesconto) {
        double totalServicos = somarValores(servicos, 0);
        double totalPecas = somarValores(pecas, 0);
        double totalExternos = somarValores(externos, 0);
        double total = totalServicos + totalPecas + totalExternos;
        return total - Math.min(total, valorDesconto);
    }

    // ---------------------------------------------------------------
    // FATORIAL — função recursiva clássica (didática)
    // Útil para calcular permutações de agendamento.
    // Caso base: n <= 1 → 1
    // Passo recursivo: n * fatorial(n - 1)
    // ---------------------------------------------------------------

    public static long fatorial(int n) {
        if (n <= 1) return 1;
        return n * fatorial(n - 1);
    }
}
