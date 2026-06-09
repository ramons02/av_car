package br.edu.senai.fatesg.avcar.business.colaboradores;

import br.edu.senai.fatesg.avcar.core.services.IGenericService;

import java.util.List;

public interface IColaboradorService extends IGenericService<ColaboradorDTO> {
    List<ColaboradorDTO> buscarPorNome(String nome);
    ColaboradorDTO salvar(ColaboradorModel model);
    void atualizar(ColaboradorModel model);
    List<FuncaoDTO> listarFuncoes();
}
