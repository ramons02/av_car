package br.edu.senai.fatesg.avcar.business.colaboradores;

import br.edu.senai.fatesg.avcar.core.domains.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

@Data
@EqualsAndHashCode(callSuper = true)
public class ColaboradorModel extends BaseModel {
    private String nome;
    private String matricula;
    private String cpf;
    private String ddi1;
    private String ddd1;
    private String numerotelefone1;
    private String email;
    private LocalDate dataAdmissao;
    private LocalDate dataDemissao;
    private Double salario;
    private String observacoes;
    private Long idPessoa;
    private List<Long> funcaoIds;
    private List<String> funcaoNomes;

    public ColaboradorModel() {
        this.funcaoIds = new ArrayList<>();
        this.funcaoNomes = new ArrayList<>();
    }
}
