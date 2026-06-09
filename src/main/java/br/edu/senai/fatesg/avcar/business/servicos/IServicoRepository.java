package br.edu.senai.fatesg.avcar.business.servicos;

import br.edu.senai.fatesg.avcar.core.repositories.IGenericRepository;

import java.util.List;

public interface IServicoRepository extends IGenericRepository<ServicoModel> {
    List<ServicoModel> buscarPorNome(String nome);
}
