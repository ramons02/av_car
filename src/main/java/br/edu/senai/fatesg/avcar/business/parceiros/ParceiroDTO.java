package br.edu.senai.fatesg.avcar.business.parceiros;

import br.edu.senai.fatesg.avcar.core.dtos.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ParceiroDTO extends BaseDTO {
    private String nome;
    private String cnpj;
    private String tipoServico;
    private String telefone;
    private String email;

    public ParceiroDTO() {}
}
