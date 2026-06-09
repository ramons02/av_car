package br.edu.senai.fatesg.avcar.business.ordemservico;

import br.edu.senai.fatesg.avcar.core.exceptions.NegocioException;
import br.edu.senai.fatesg.avcar.core.validations.GenericValidation;
import org.springframework.stereotype.Component;

@Component
public class OrdemServicoValidation extends GenericValidation<OrdemServicoModel> implements IOrdemServicoValidation {

    @Override
    public void validar(OrdemServicoModel os) {
        if (os.getIdVeiculo() == null)
            throw new NegocioException("Veículo é obrigatório na Ordem de Serviço");
        if (os.getIdCliente() == null)
            throw new NegocioException("Cliente é obrigatório na Ordem de Serviço");
        if (os.getDefeito() == null || os.getDefeito().isBlank())
            throw new NegocioException("Defeito relatado é obrigatório");
    }
}
