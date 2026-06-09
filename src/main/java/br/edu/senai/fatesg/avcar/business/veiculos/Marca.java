package br.edu.senai.fatesg.avcar.business.veiculos;

import lombok.Data;

@Data
public class Marca {
    private Long idMarca;
    private String nomeMarca;
    private String logoUrl;

    public Marca() {}

    public Marca(Long idMarca, String nomeMarca) {
        this.idMarca = idMarca;
        this.nomeMarca = nomeMarca;
    }

    public Marca(Long idMarca, String nomeMarca, String logoUrl) {
        this.idMarca = idMarca;
        this.nomeMarca = nomeMarca;
        this.logoUrl = logoUrl;
    }
}
