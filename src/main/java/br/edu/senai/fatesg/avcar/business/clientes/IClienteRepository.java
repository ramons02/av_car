package br.edu.senai.fatesg.avcar.business.clientes;

import br.edu.senai.fatesg.avcar.core.repositories.IGenericRepository;

import java.util.List;

public interface IClienteRepository extends IGenericRepository<ClienteModel> {
    List<ClienteModel> buscarPorNome(String nome);
}
