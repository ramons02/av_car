package br.edu.senai.fatesg.avcar.business.fornecedores;

import br.edu.senai.fatesg.avcar.core.repositories.IGenericRepository;

import java.util.List;

public interface IFornecedorRepository extends IGenericRepository<FornecedorModel> {
    List<FornecedorModel> buscarPorNome(String nome);
}
