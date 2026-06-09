package br.edu.senai.fatesg.avcar.business.fornecedores;

import br.edu.senai.fatesg.avcar.core.domains.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FornecedorModel extends BaseModel {
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

    public FornecedorModel() {}
}
