package br.edu.senai.fatesg.avcar.business.fornecedores;

import br.edu.senai.fatesg.avcar.core.domains.BaseModel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Fornecedor extends BaseModel {
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

    public Fornecedor() {}

    public Fornecedor(Long id, String razaoSocial, String cnpj, String ddi, String ddd,
                      String numeroFornecedor, String email, String enderecoFornecedor,
                      String bairroFornecedor, String cidadeFornecedor, String estadoFornecedor,
                      int cepFornecedor) {
        setId(id);
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
        this.ddi = ddi;
        this.ddd = ddd;
        this.numeroFornecedor = numeroFornecedor;
        this.email = email;
        this.enderecoFornecedor = enderecoFornecedor;
        this.bairroFornecedor = bairroFornecedor;
        this.cidadeFornecedor = cidadeFornecedor;
        this.estadoFornecedor = estadoFornecedor;
        this.cepFornecedor = cepFornecedor;
    }
}
