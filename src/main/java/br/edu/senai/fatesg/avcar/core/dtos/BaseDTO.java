package br.edu.senai.fatesg.avcar.core.dtos;

import lombok.Data;

@Data
public abstract class BaseDTO {
    private Long id;
    private boolean ativo;
}
