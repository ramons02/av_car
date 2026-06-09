package br.edu.senai.fatesg.avcar.business.pecas;

import br.edu.senai.fatesg.avcar.core.dtos.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class PecaDTO extends BaseDTO {
    private long codigoNacional;
    private String codigoInterno;
    private String nome;
    private String descricao;
    private String fabricante;
    private String categoria;
    private double precoCusto;
    private double precoVenda;
    private int quantidadeEstoque;
    private int garantiaPeca;
    private LocalDate dataCompraPeca;
    private Long fornecedorId;
    private String fornecedorNome;

    public PecaDTO() {}
}
