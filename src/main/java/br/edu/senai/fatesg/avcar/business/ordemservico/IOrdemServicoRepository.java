package br.edu.senai.fatesg.avcar.business.ordemservico;

import br.edu.senai.fatesg.avcar.business.pecas.ItemPeca;
import br.edu.senai.fatesg.avcar.business.servicos.ItemServico;
import br.edu.senai.fatesg.avcar.business.ordemservico.OrdemServico;
import br.edu.senai.fatesg.avcar.business.servicos.ServicoExterno;
import br.edu.senai.fatesg.avcar.core.repositories.Repository;

import java.util.List;

public interface IOrdemServicoRepository extends Repository<OrdemServico> {
    List<OrdemServico> buscarPorStatus(String status);
    double somarValorItens(Long osId);

    ItemServico adicionarItemServico(Long osId, Long servicoId, int quantidade,
                                     double valorUnitario, int garantiaDias, Long colaboradorId);
    void removerItemServico(Long itemId);
    List<ItemServico> listarItensServico(Long osId);

    ItemPeca adicionarItemPeca(Long osId, Long pecaId, int quantidade,
                                double valorUnitario, int garantiaDias);
    void removerItemPeca(Long itemId);
    List<ItemPeca> listarItensPeca(Long osId);

    ServicoExterno adicionarServicoExterno(Long osId, Long fornecedorId, String descricao,
                                            double valor, int garantiaDias);
    void removerServicoExterno(Long itemId);
    List<ServicoExterno> listarServicosExternos(Long osId);
}
