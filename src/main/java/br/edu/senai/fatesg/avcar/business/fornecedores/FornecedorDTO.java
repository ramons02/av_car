package br.edu.senai.fatesg.avcar.business.fornecedores;

import br.edu.senai.fatesg.avcar.core.dtos.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FornecedorDTO extends BaseDTO {
    private String razaoSocial;
    private String cnpj;
    private String ddi;
    private String ddd;
    private String numeroFornecedor;
    private String email;
    private String enderecoFornecedor;
    private String bairroFornecedor;
    private String cidadeFornecedor;
    private String estadoFornecedor;
    private int cepFornecedor;

    public FornecedorDTO() {}
}
