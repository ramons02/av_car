package br.edu.senai.fatesg.avcar.business.veiculos;

import lombok.Data;

@Data
public class Modelo {
    private Long idModelo;
    private String nomeModelo;
    private Marca marca;

    public Modelo() {}

    public Modelo(Long idModelo, String nomeModelo, Marca marca) {
        this.idModelo = idModelo;
        this.nomeModelo = nomeModelo;
        this.marca = marca;
    }
}
