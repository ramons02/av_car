package br.edu.senai.fatesg.avcar.business.pecas;

import br.edu.senai.fatesg.avcar.core.repositories.IGenericRepository;

import java.util.List;

public interface IPecaRepository extends IGenericRepository<PecaModel> {
    List<PecaModel> buscarPorCodigoNacional(long codigo);
    List<PecaModel> buscarPorFornecedor(Long fornecedorId);
    List<PecaModel> buscarEstoqueBaixo(int quantidadeMinima);
}
