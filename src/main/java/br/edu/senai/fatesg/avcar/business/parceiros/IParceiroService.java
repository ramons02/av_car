package br.edu.senai.fatesg.avcar.business.parceiros;

import br.edu.senai.fatesg.avcar.core.services.IGenericService;

import java.util.List;

public interface IParceiroService extends IGenericService<ParceiroDTO> {
    List<ParceiroDTO> buscarPorNome(String nome);
    ParceiroDTO salvar(ParceiroModel model);
    void atualizar(ParceiroModel model);
}
