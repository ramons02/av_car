package br.edu.senai.fatesg.avcar.business.veiculos;

import br.edu.senai.fatesg.avcar.core.exceptions.NegocioException;
import br.edu.senai.fatesg.avcar.core.validations.GenericValidation;
import org.springframework.stereotype.Component;

@Component
public class VeiculoValidation extends GenericValidation<VeiculoModel> implements IVeiculoValidation {

    @Override
    public void validar(VeiculoModel m) {
        validarPlaca(m.getPlaca());
        naoNulo(m.getModeloId(), "Modelo");
        if (m.getAnoFabricacao() <= 0) throw new NegocioException("Ano de fabricação inválido");
        if (m.getAnoModelo() <= 0) throw new NegocioException("Ano do modelo inválido");
    }
}
