package br.edu.senai.fatesg.avcar.business.veiculos;

import br.edu.senai.fatesg.avcar.core.domains.BaseModel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Veiculo extends BaseModel {
    private String placa;
    private String chassi;
    private int anoFabricacao;
    private int anoModelo;
    private String cor;
    private int quilometragem;
    private String acessorios;
    private Modelo modelo;
    private List<HistoricoClienteVeiculo> historicoProprietarios;

    public Veiculo() {
        this.historicoProprietarios = new ArrayList<>();
    }

    public Veiculo(Long id, String placa, String chassi, int anoFabricacao, int anoModelo, Modelo modelo) {
        setId(id);
        this.placa = placa;
        this.chassi = chassi;
        this.anoFabricacao = anoFabricacao;
        this.anoModelo = anoModelo;
        this.modelo = modelo;
        this.historicoProprietarios = new ArrayList<>();
    }

    public Iterator<HistoricoClienteVeiculo> historicoIterator() {
        return historicoProprietarios.iterator();
    }
}
