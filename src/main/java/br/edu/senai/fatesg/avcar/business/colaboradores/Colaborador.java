package br.edu.senai.fatesg.avcar.business.colaboradores;

import br.edu.senai.fatesg.avcar.core.domains.BaseModel;
import br.edu.senai.fatesg.avcar.core.domains.Pessoa;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Colaborador extends BaseModel {
    private String matricula;
    private String cpf;
    private LocalDate dataAdmissao;
    private LocalDate dataDemissao;
    private Double salario;
    private String observacoes;
    private Pessoa pessoa;
    private List<Funcao> funcoes;

    public Colaborador() {
        this.funcoes = new ArrayList<>();
    }

    public Colaborador(Long id, String matricula, String cpf) {
        setId(id);
        this.matricula = matricula;
        this.cpf = cpf;
        this.dataAdmissao = LocalDate.now();
        this.funcoes = new ArrayList<>();
    }

    public void addFuncao(Funcao funcao) { this.funcoes.add(funcao); }
}
