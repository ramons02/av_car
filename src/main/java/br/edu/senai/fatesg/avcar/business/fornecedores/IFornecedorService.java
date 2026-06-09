package br.edu.senai.fatesg.avcar.business.fornecedores;

import br.edu.senai.fatesg.avcar.core.services.IGenericService;

import java.util.List;

public interface IFornecedorService extends IGenericService<FornecedorDTO> {
    List<FornecedorDTO> buscarPorNome(String nome);
    FornecedorDTO salvar(FornecedorModel model);
    void atualizar(FornecedorModel model);
}
