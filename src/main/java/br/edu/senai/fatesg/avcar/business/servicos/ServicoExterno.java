package br.edu.senai.fatesg.avcar.business.servicos;

import br.edu.senai.fatesg.avcar.business.fornecedores.Fornecedor;
import br.edu.senai.fatesg.avcar.business.ordemservico.OrdemServico;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ServicoExterno {
    private Long id;
    private OrdemServico ordemServico;
    private Fornecedor fornecedor;
    private String descricao;
    private double valor;
    private LocalDate prazo;
    private String observacoes;
    private int garantiaDias;

    public ServicoExterno() {}

    public ServicoExterno(Long id, OrdemServico ordemServico, Fornecedor fornecedor,
                          String descricao, double valor, int garantiaDias) {
        this.id = id;
        this.ordemServico = ordemServico;
        this.fornecedor = fornecedor;
        this.descricao = descricao;
        this.valor = valor;
        this.garantiaDias = garantiaDias;
    }
}
