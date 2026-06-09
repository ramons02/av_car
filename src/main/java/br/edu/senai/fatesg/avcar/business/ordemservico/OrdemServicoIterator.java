package br.edu.senai.fatesg.avcar.business.ordemservico;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

// PADRÃO ITERATOR: Fornece uma maneira de acessar sequencialmente os elementos
// de um objeto agregado sem expor sua estrutura interna. Aplicado aqui para
// percorrer ordens de serviço filtrando por status, encapsulando a lógica de
// navegação e filtro para que o cliente use apenas hasNext() e next().
public class OrdemServicoIterator implements Iterator<OrdemServico> {
    private final List<OrdemServico> ordens;
    private final StatusOrdemServico filtroStatus;
    private int posicaoAtual;
    private int proximoIndex;

    public OrdemServicoIterator(List<OrdemServico> ordens, StatusOrdemServico filtroStatus) {
        this.ordens = ordens;
        this.filtroStatus = filtroStatus;
        this.posicaoAtual = 0;
        this.proximoIndex = -1;
        avancarParaProximo();
    }

    private void avancarParaProximo() {
        proximoIndex = -1;
        for (int i = posicaoAtual; i < ordens.size(); i++) {
            if (ordens.get(i).getStatus() == filtroStatus) {
                proximoIndex = i;
                break;
            }
        }
    }

    @Override
    public boolean hasNext() {
        return proximoIndex >= 0;
    }

    @Override
    public OrdemServico next() {
        if (!hasNext()) throw new NoSuchElementException();
        OrdemServico atual = ordens.get(proximoIndex);
        posicaoAtual = proximoIndex + 1;
        avancarParaProximo();
        return atual;
    }
}
