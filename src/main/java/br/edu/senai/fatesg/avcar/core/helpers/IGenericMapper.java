package br.edu.senai.fatesg.avcar.core.helpers;

import br.edu.senai.fatesg.avcar.core.domains.BaseModel;
import br.edu.senai.fatesg.avcar.core.dtos.BaseDTO;

public interface IGenericMapper<T extends BaseModel, D extends BaseDTO> {
    D toDto(T entity);
    T toEntity(D dto);
}
