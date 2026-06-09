package br.edu.senai.fatesg.avcar.business.colaboradores;

import br.edu.senai.fatesg.avcar.core.dtos.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ColaboradorDTO extends BaseDTO {
    private String nome;
    private String matricula;
    private String cpf;
    private String telefone;
    private String email;
    private LocalDate dataAdmissao;
    private LocalDate dataDemissao;
    private Double salario;
    private String observacoes;
    private List<String> funcoes;

    public ColaboradorDTO() {}
}
