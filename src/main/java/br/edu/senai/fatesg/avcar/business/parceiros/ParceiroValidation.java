package br.edu.senai.fatesg.avcar.business.parceiros;

import br.edu.senai.fatesg.avcar.core.validations.GenericValidation;
import org.springframework.stereotype.Component;

@Component
public class ParceiroValidation extends GenericValidation<ParceiroModel> implements IParceiroValidation {

    @Override
    public void validar(ParceiroModel m) {
        naoVazio(m.getNome(), "Nome");
        naoVazio(m.getTipoServico(), "Tipo de Serviço");
        if (m.getCnpj() != null && !m.getCnpj().isBlank()) {
            validarCnpj(m.getCnpj());
        }
        if (m.getEmail() != null && !m.getEmail().isBlank()) {
            validarEmail(m.getEmail());
        }
    }
}
