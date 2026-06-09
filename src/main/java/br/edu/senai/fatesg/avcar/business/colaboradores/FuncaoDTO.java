package br.edu.senai.fatesg.avcar.business.colaboradores;

import lombok.Data;

@Data
public class FuncaoDTO {
    private Long idFuncao;
    private String especialidade;
    private Double comissao;
    private String funcaoColaborador;

    public FuncaoDTO() {}

    public FuncaoDTO(Long idFuncao, String especialidade, Double comissao, String funcaoColaborador) {
        this.idFuncao = idFuncao;
        this.especialidade = especialidade;
        this.comissao = comissao;
        this.funcaoColaborador = funcaoColaborador;
    }
}
