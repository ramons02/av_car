package br.edu.senai.fatesg.avcar.business.clientes;

import br.edu.senai.fatesg.avcar.core.domains.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class ClienteModel extends BaseModel {
    private String nome;
    private String tipo; // "PF" ou "PJ"
    private String cpf;
    private String cnpj;
    private String rg;
    private LocalDate dataNascimento;
    private String razaoSocial;
    private String inscricaoEstadual;
    private String telefone;
    private String email;
    private String endereco;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String observacoes;
    private LocalDate dataCadastro;

    // Campos de IDs internos para atualização
    private Long idPessoa;
    private Long idPessoaFisica;
    private Long idPessoaJuridica;

    public ClienteModel() {}

    public boolean isPessoaFisica() { return "PF".equals(tipo); }
    public boolean isPessoaJuridica() { return "PJ".equals(tipo); }
}
