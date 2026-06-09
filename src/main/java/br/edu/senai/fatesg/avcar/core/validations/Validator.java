package br.edu.senai.fatesg.avcar.core.validations;

import br.edu.senai.fatesg.avcar.core.exceptions.NegocioException;

public interface Validator<T> {
    void validar(T entity) throws NegocioException;
}
