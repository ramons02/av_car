package br.edu.senai.fatesg.avcar.business.servicos;

import br.edu.senai.fatesg.avcar.business.ordemservico.OrdemServico;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ItemServico {
    private Long id;
    private OrdemServico ordemServico;
    private Servico servico;
    private int quantidade;
    private double valorUnitario;
    private LocalDateTime horaInicio;
    private LocalDateTime horaFim;
    private String status;
    private String colaboradorNome;

    public ItemServico() {}

    public ItemServico(Long id, OrdemServico ordemServico, Servico servico,
                       int quantidade, double valorUnitario) {
        this.id = id;
        this.ordemServico = ordemServico;
        this.servico = servico;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
    }

    public double getSubtotal() { return quantidade * valorUnitario; }
}
