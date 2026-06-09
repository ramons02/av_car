package br.edu.senai.fatesg.avcar.core.services;

import br.edu.senai.fatesg.avcar.core.dtos.BaseDTO;
import br.edu.senai.fatesg.avcar.core.exceptions.EntidadeNaoEncontradaException;
import br.edu.senai.fatesg.avcar.core.domains.BaseModel;
import br.edu.senai.fatesg.avcar.core.repositories.Repository;

import java.util.List;

public abstract class GenericService<T extends BaseModel, D extends BaseDTO, R extends Repository<T>> implements IGenericService<D> {

    protected final R repository;
    private final String entityName;

    public GenericService(R repository, String entityName) {
        this.repository = repository;
        this.entityName = entityName;
    }

    public List<D> listarTodos() {
        return listarTodos(false);
    }

    public List<D> listarTodos(boolean incluirInativos) {
        var lista = incluirInativos ? repository.listarTodosIncluindoInativos() : repository.listarTodos();
        return lista.stream().map(this::toDTO).toList();
    }

    public D buscarPorId(Long id) {
        T entity = repository.buscarPorId(id)
            .orElseThrow(() -> new EntidadeNaoEncontradaException(entityName, id));
        return toDTO(entity);
    }

    public void toggleStatus(Long id) {
        if (repository.buscarPorId(id).isEmpty()) {
            throw new EntidadeNaoEncontradaException(entityName, id);
        }
        repository.toggleStatus(id);
    }

    public void deletar(Long id) {
        if (repository.buscarPorId(id).isEmpty()) {
            throw new EntidadeNaoEncontradaException(entityName, id);
        }
        repository.deletar(id);
    }

    protected abstract D toDTO(T entity);
}
