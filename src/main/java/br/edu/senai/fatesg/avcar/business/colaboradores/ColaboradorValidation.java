package br.edu.senai.fatesg.avcar.business.colaboradores;

import br.edu.senai.fatesg.avcar.core.validations.GenericValidation;
import org.springframework.stereotype.Component;

@Component
public class ColaboradorValidation extends GenericValidation<ColaboradorModel> implements IColaboradorValidation {

    @Override
    public void validar(ColaboradorModel m) {
        naoVazio(m.getNome(), "Nome");
        validarCpf(m.getCpf());
        if (m.getEmail() != null && !m.getEmail().isBlank()) {
            validarEmail(m.getEmail());
        }
    }
}
