package br.edu.senai.fatesg.avcar.business.servicos;

import br.edu.senai.fatesg.avcar.core.exceptions.NegocioException;
import br.edu.senai.fatesg.avcar.core.validations.GenericValidation;
import org.springframework.stereotype.Component;

@Component
public class ServicoValidation extends GenericValidation<ServicoModel> implements IServicoValidation {

    @Override
    public void validar(ServicoModel m) {
        naoVazio(m.getNomeServico(), "Nome do serviço");
        if (m.getValorServico() < 0) throw new NegocioException("Valor do serviço não pode ser negativo");
        if (m.getGarantiaDias() < 0) throw new NegocioException("Garantia em dias não pode ser negativa");
    }
}
