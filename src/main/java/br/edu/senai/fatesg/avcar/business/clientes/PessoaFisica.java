package br.edu.senai.fatesg.avcar.business.clientes;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class PessoaFisica extends Cliente {
    private Long idPessoaFisica;
    private String cpf;
    private String rg;
    private LocalDate dataNascimento;

    public PessoaFisica() {}

    public PessoaFisica(Long id, String nome, String endereco, String telefone, String email, String cpf) {
        super(id, nome, endereco, telefone, email);
        this.cpf = cpf;
    }

    @Override
    public String getDocumento() { return cpf; }
}
