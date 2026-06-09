package br.edu.senai.fatesg.avcar.business.veiculos;

import br.edu.senai.fatesg.avcar.core.domains.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class VeiculoModel extends BaseModel {
    private String placa;
    private String chassi;
    private int anoFabricacao;
    private int anoModelo;
    private String cor;
    private int quilometragem;
    private String acessorios;
    private Long modeloId;
    private String modeloNome;
    private Long marcaId;
    private String marcaNome;
    private String marcaLogoUrl;
    private Long clienteId;
    private String clienteNome;

    public VeiculoModel() {}
}
