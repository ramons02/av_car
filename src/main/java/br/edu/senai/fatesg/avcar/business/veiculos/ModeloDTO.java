package br.edu.senai.fatesg.avcar.business.veiculos;

import lombok.Data;

@Data
public class ModeloDTO {
    private Long idModelo;
    private String nomeModelo;
    private Long idMarca;
    private String nomeMarca;

    public ModeloDTO() {}

    public ModeloDTO(Long idModelo, String nomeModelo, Long idMarca, String nomeMarca) {
        this.idModelo = idModelo;
        this.nomeModelo = nomeModelo;
        this.idMarca = idMarca;
        this.nomeMarca = nomeMarca;
    }
}
