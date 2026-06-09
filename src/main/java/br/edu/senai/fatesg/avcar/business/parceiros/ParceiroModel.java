package br.edu.senai.fatesg.avcar.business.parceiros;

import br.edu.senai.fatesg.avcar.core.domains.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ParceiroModel extends BaseModel {
    private String nome;
    private String cnpj;
    private String tipoServico;
    private String telefone;
    private String email;

    public ParceiroModel() {}
}
