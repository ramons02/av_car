package br.edu.senai.fatesg.avcar.business.clientes;

import br.edu.senai.fatesg.avcar.core.dtos.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class ClienteDTO extends BaseDTO {
    private String nome;
    private String tipo;
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

    public ClienteDTO() {}

    /** Retorna CPF para PF ou CNPJ para PJ, compatível com código legado. */
    public String getDocumento() {
        return "PF".equals(tipo) ? cpf : cnpj;
    }
}
