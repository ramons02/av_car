package br.edu.senai.fatesg.avcar.business.ordemservico;

import br.edu.senai.fatesg.avcar.core.exceptions.NegocioException;

import java.time.LocalDate;

// PADRÃO TEMPLATE METHOD: Define o esqueleto de um algoritmo em uma operação,
// postergando alguns passos para subclasses. Aplicado aqui para fixar o fluxo
// de transição de status de OS (validar → executar → notificar) no método
// executar(), enquanto subclasses implementam apenas as etapas variáveis
// (validação e próximo status), eliminando duplicação de código.
public abstract class OrdemServicoTemplate {

    public final OrdemServico executar(OrdemServico os) {
        validarTransicao(os);
        executarTransicao(os);
        notificar(os);
        return os;
    }

    protected abstract void validarTransicao(OrdemServico os);

    protected void executarTransicao(OrdemServico os) {
        StatusOrdemServico proximo = proximoStatus(os.getStatus());
        os.setStatus(proximo);
        if (proximo == StatusOrdemServico.FINALIZADA) {
            os.setDataFinalizacao(LocalDate.now());
        }
        recalcularValor(os);
    }

    protected void notificar(OrdemServico os) {
        System.out.println("[NOTIFICAÇÃO] OS " + os.getNumeroOs()
            + " alterada para: " + os.getStatus().getDescricao());
    }

    protected abstract StatusOrdemServico proximoStatus(StatusOrdemServico atual);

    protected void recalcularValor(OrdemServico os) {
        double totalServicos = os.getItensServico().stream()
            .mapToDouble(i -> i.getQuantidade() * i.getValorUnitario()).sum();
        double totalPecas = os.getItensPeca().stream()
            .mapToDouble(i -> i.getQuantidade() * i.getValorUnitario()).sum();
        os.setValorTotal(totalServicos + totalPecas);
    }

    public static void performCancel(OrdemServico os) {
        if (os.getStatus() == StatusOrdemServico.FINALIZADA) {
            throw new NegocioException("OS finalizada não pode ser cancelada");
        }
        os.setStatus(StatusOrdemServico.CANCELADA);
        System.out.println("[NOTIFICAÇÃO] OS " + os.getNumeroOs()
            + " cancelada.");
    }

    public static void performPause(OrdemServico os) {
        if (os.getStatus() != StatusOrdemServico.EM_ORCAMENTO
            && os.getStatus() != StatusOrdemServico.EM_EXECUCAO) {
            throw new NegocioException("OS deve estar em Orçamento ou Execução para pausar");
        }
        os.setStatus(StatusOrdemServico.AGUARDANDO_PECA);
        System.out.println("[NOTIFICAÇÃO] OS " + os.getNumeroOs()
            + " pausada para aguardar peça.");
    }

    public static void performReturn(OrdemServico os) {
        if (os.getStatus() != StatusOrdemServico.AGUARDANDO_PECA) {
            throw new NegocioException("OS deve estar em Aguardando Peça para retornar");
        }
        os.setStatus(StatusOrdemServico.EM_ORCAMENTO);
        System.out.println("[NOTIFICAÇÃO] OS " + os.getNumeroOs()
            + " retornada para Orçamento.");
    }
}
