package br.edu.senai.fatesg.avcar.business.clientes;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class PessoaJuridica extends Cliente {
    private Long idPessoaJuridica;
    private String cnpj;
    private String inscricaoEstadual;
    private String razaoSocial;

    public PessoaJuridica() {}

    public PessoaJuridica(Long id, String nome, String endereco, String telefone, String email,
                          String cnpj, String inscricaoEstadual) {
        super(id, nome, endereco, telefone, email);
        this.cnpj = cnpj;
        this.inscricaoEstadual = inscricaoEstadual;
    }

    @Override
    public String getDocumento() { return cnpj; }
}
