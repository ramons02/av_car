package br.edu.senai.fatesg.avcar.datastructures;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class BuscaOS {

    private BuscaOS() {}

    public static <T> int buscaLinear(List<T> lista, Function<T, String> extrator, String alvo) {
        if (lista == null || alvo == null) return -1;
        String target = alvo.toLowerCase();
        for (int i = 0; i < lista.size(); i++) {
            String valor = extrator.apply(lista.get(i));
            if (valor != null && valor.toLowerCase().contains(target)) {
                return i;
            }
        }
        return -1;
    }

    public static <T> int buscaBinaria(List<T> lista, T alvo, Comparator<T> comparator) {
        if (lista == null || lista.isEmpty()) return -1;
        int esq = 0, dir = lista.size() - 1;
        while (esq <= dir) {
            int meio = esq + (dir - esq) / 2;
            int cmp = comparator.compare(lista.get(meio), alvo);
            if (cmp == 0) return meio;
            if (cmp < 0) esq = meio + 1;
            else dir = meio - 1;
        }
        return -1;
    }
}
