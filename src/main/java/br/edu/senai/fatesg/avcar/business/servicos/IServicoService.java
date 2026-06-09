package br.edu.senai.fatesg.avcar.business.servicos;

import br.edu.senai.fatesg.avcar.core.services.IGenericService;

import java.util.List;

public interface IServicoService extends IGenericService<ServicoDTO> {
    List<ServicoDTO> buscarPorNome(String nome);
    ServicoDTO salvar(ServicoModel model);
    void atualizar(ServicoModel model);
}
