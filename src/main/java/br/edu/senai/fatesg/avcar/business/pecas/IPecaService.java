package br.edu.senai.fatesg.avcar.business.pecas;

import br.edu.senai.fatesg.avcar.core.services.IGenericService;

import java.util.List;

public interface IPecaService extends IGenericService<PecaDTO> {
    PecaDTO salvar(PecaModel model);
    void atualizar(PecaModel model);
    List<PecaDTO> buscarEstoqueBaixo(int quantidadeMinima);
    List<PecaDTO> buscarPorCodigo(long codigo);
}
