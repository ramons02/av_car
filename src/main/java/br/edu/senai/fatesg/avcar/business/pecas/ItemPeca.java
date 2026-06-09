package br.edu.senai.fatesg.avcar.business.pecas;

import br.edu.senai.fatesg.avcar.business.ordemservico.OrdemServico;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemPeca {
    private Long id;
    private Peca peca;
    private OrdemServico ordemServico;
    private int quantidade;
    private double valorUnitario;
    private double valorTotal;
    private int garantia;

    public ItemPeca() {}

    public ItemPeca(Long id, Peca peca, OrdemServico ordemServico, int quantidade, double valorUnitario) {
        this.id = id;
        this.peca = peca;
        this.ordemServico = ordemServico;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
    }

    public double getSubtotal() { return quantidade * valorUnitario; }
}
