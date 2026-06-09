package br.edu.senai.fatesg.avcar.business.servicos;

import br.edu.senai.fatesg.avcar.core.dtos.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ServicoDTO extends BaseDTO {
    private String nomeServico;
    private String descricaoServico;
    private double valorServico;
    private int garantiaDias;
    private String tempoEstimado;

    public ServicoDTO() {}
}
