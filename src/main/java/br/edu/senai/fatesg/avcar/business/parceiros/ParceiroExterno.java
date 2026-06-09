package br.edu.senai.fatesg.avcar.business.parceiros;

import br.edu.senai.fatesg.avcar.core.domains.BaseModel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class ParceiroExterno extends BaseModel {
    private String nome;
    private String cnpj;
    private String tipoServico;
    private String telefone;
    private String email;

    public ParceiroExterno() {}

    public ParceiroExterno(Long id, String nome, String cnpj, String tipoServico,
                           String telefone, String email) {
        setId(id);
        this.nome = nome;
        this.cnpj = cnpj;
        this.tipoServico = tipoServico;
        this.telefone = telefone;
        this.email = email;
    }
}
