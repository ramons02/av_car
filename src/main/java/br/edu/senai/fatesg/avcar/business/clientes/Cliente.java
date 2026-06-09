package br.edu.senai.fatesg.avcar.business.clientes;

import br.edu.senai.fatesg.avcar.core.domains.BaseModel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public abstract class Cliente extends BaseModel {
    private Long idPessoa;
    private String nome;
    private String endereco;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String telefone;
    private String email;
    private LocalDate dataCadastro;
    private String observacoes;

    public Cliente() {}

    public Cliente(Long id, String nome, String endereco, String telefone, String email) {
        setId(id);
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.email = email;
        this.dataCadastro = LocalDate.now();
    }

    public abstract String getDocumento();
}
