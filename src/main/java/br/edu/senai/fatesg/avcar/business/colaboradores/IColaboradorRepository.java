package br.edu.senai.fatesg.avcar.business.colaboradores;

import br.edu.senai.fatesg.avcar.core.repositories.IGenericRepository;

import java.util.List;

public interface IColaboradorRepository extends IGenericRepository<ColaboradorModel> {
    List<ColaboradorModel> buscarPorNome(String nome);
    void salvarFuncoes(Long colaboradorId, List<Long> funcaoIds);
    List<FuncaoDTO> listarFuncoes();
}
