package br.edu.senai.fatesg.avcar.business.ordemservico;

import br.edu.senai.fatesg.avcar.core.domains.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrdemServicoModel extends BaseModel {

    private Long idVeiculo;
    private Long idCliente;
    private Long idColaboradorResponsavel;
    private String status;
    private String defeito;
    private String solucao;
    private LocalDateTime dataAbertura;
    private LocalDateTime dataFechamento;
    private LocalDate dataPrevisao;
    private double valorTotal;
    private String observacoes;

    public OrdemServicoModel() {}
}
