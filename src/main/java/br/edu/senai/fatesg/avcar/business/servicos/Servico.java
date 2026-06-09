package br.edu.senai.fatesg.avcar.business.servicos;

import br.edu.senai.fatesg.avcar.core.domains.BaseModel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Servico extends BaseModel {
    private String nomeServico;
    private String descricaoServico;
    private double valorServico;
    private int garantiaDias;
    private String tempoEstimado;

    public Servico() {}

    public Servico(Long id, String nomeServico, String descricaoServico, double valorServico,
                   int garantiaDias, String tempoEstimado) {
        setId(id);
        this.nomeServico = nomeServico;
        this.descricaoServico = descricaoServico;
        this.valorServico = valorServico;
        this.garantiaDias = garantiaDias;
        this.tempoEstimado = tempoEstimado;
    }
}
