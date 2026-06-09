package br.edu.senai.fatesg.avcar.business.pecas;

import br.edu.senai.fatesg.avcar.core.domains.BaseModel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Peca extends BaseModel {
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

    public Peca() {}

    public Peca(Long id, long codigoNacional, String nome, String descricao,
                double precoCusto, double precoVenda, int quantidadeEstoque,
                int garantiaPeca, Long fornecedorId, String fornecedorNome) {
        setId(id);
        this.codigoNacional = codigoNacional;
        this.nome = nome;
        this.descricao = descricao;
        this.precoCusto = precoCusto;
        this.precoVenda = precoVenda;
        this.quantidadeEstoque = quantidadeEstoque;
        this.garantiaPeca = garantiaPeca;
        this.fornecedorId = fornecedorId;
        this.fornecedorNome = fornecedorNome;
    }
}
