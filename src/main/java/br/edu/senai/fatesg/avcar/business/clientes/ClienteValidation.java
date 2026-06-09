package br.edu.senai.fatesg.avcar.business.clientes;

import br.edu.senai.fatesg.avcar.core.validations.GenericValidation;
import org.springframework.stereotype.Component;

@Component
public class ClienteValidation extends GenericValidation<ClienteModel> implements IClienteValidation {

    @Override
    public void validar(ClienteModel c) {
        naoVazio(c.getNome(), "Nome");
        validarTelefone(c.getTelefone());
        if (c.isPessoaFisica()) {
            validarCpf(c.getCpf());
        } else if (c.isPessoaJuridica()) {
            validarCnpj(c.getCnpj());
        }
    }
}
