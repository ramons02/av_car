package br.edu.senai.fatesg.avcar.core.services;

import br.edu.senai.fatesg.avcar.core.dtos.BaseDTO;

import java.util.List;

public interface IGenericService<D extends BaseDTO> {
    List<D> listarTodos();
    List<D> listarTodos(boolean incluirInativos);
    D buscarPorId(Long id);
    void toggleStatus(Long id);
}
