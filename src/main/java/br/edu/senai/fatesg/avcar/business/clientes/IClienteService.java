package br.edu.senai.fatesg.avcar.business.clientes;

import br.edu.senai.fatesg.avcar.core.services.IGenericService;

import java.util.List;

public interface IClienteService extends IGenericService<ClienteDTO> {
    List<ClienteDTO> buscarPorNome(String nome);
    ClienteDTO salvar(ClienteModel model);
    void atualizar(ClienteModel model);
    ClienteDTO atualizarPorRequest(Long id, ClienteController.AtualizarClienteRequest req);
}
