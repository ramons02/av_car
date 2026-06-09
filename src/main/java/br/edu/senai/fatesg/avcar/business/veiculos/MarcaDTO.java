package br.edu.senai.fatesg.avcar.business.veiculos;

import lombok.Data;

@Data
public class MarcaDTO {
    private Long idMarca;
    private String nomeMarca;
    private String logoUrl;

    public MarcaDTO() {}

    public MarcaDTO(Long idMarca, String nomeMarca, String logoUrl) {
        this.idMarca = idMarca;
        this.nomeMarca = nomeMarca;
        this.logoUrl = logoUrl;
    }
}
