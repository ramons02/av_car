package br.edu.senai.fatesg.avcar.business.ordemservico;

// PADRÃO DECORATOR: Anexa responsabilidades adicionais a um objeto dinamicamente,
// fornecendo uma alternativa flexível à herança para extensão de funcionalidade.
// Aplicado aqui como classe base abstrata que envolve uma OrdemServico para
// adicionar comportamentos extras (desconto, garantia etc.) sem modificar a
// classe original — subclasses sobrescrevem apenas os métodos necessários.
public abstract class OrdemServicoDecorator extends OrdemServico {
    protected OrdemServico wrappee;

    public OrdemServicoDecorator(OrdemServico wrappee) {
        this.wrappee = wrappee;
        this.setId(wrappee.getId());
        this.setNumeroOs(wrappee.getNumeroOs());
        this.setVeiculo(wrappee.getVeiculo());
        this.setStatus(wrappee.getStatus());
        this.setDataAbertura(wrappee.getDataAbertura());
        this.setDataFinalizacao(wrappee.getDataFinalizacao());
        this.setEntradaVeiculo(wrappee.getEntradaVeiculo());
        this.setDefeitoRelatado(wrappee.getDefeitoRelatado());
        this.setQuantidadePecas(wrappee.getQuantidadePecas());
        this.setValorTotalPecas(wrappee.getValorTotalPecas());
        this.setValorMaoObra(wrappee.getValorMaoObra());
        this.setValorServicoExterno(wrappee.getValorServicoExterno());
        this.setFormaPagamento(wrappee.getFormaPagamento());
        this.setValorDesconto(wrappee.getValorDesconto());
        this.setValorTotal(wrappee.getValorTotal());
        this.setGarantia(wrappee.getGarantia());
        this.setColaboradorNome(wrappee.getColaboradorNome());
        this.setItensServico(wrappee.getItensServico());
        this.setItensPeca(wrappee.getItensPeca());
    }

    public OrdemServico getWrappee() { return wrappee; }
}
