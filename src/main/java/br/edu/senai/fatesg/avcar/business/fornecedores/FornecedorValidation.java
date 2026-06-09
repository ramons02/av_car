package br.edu.senai.fatesg.avcar.business.fornecedores;

import br.edu.senai.fatesg.avcar.core.validations.GenericValidation;
import org.springframework.stereotype.Component;

@Component
public class FornecedorValidation extends GenericValidation<FornecedorModel> implements IFornecedorValidation {

    @Override
    public void validar(FornecedorModel m) {
        naoVazio(m.getRazaoSocial(), "Razão Social");
        validarCnpj(m.getCnpj());
        if (m.getEmail() != null && !m.getEmail().isBlank()) {
            validarEmail(m.getEmail());
        }
    }
}
