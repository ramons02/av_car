package br.edu.senai.fatesg.avcar.core.domains;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class Pessoa extends BaseModel {
    private String nome;
    private String ddi1;
    private String ddd1;
    private String numerotelefone1;
    private String ddi2;
    private String ddd2;
    private String numerotelefone2;
    private String email;
    private String endereco;
    private String bairro;
    private String cidade;
    private String estado;
    private Integer cep;
    private LocalDate dataCadastro;

    public Pessoa() {}
}
