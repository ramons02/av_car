package br.edu.senai.fatesg.avcar.business.parceiros;

import br.edu.senai.fatesg.avcar.core.repositories.IGenericRepository;

import java.util.List;

public interface IParceiroRepository extends IGenericRepository<ParceiroModel> {
    List<ParceiroModel> buscarPorNome(String nome);
}
