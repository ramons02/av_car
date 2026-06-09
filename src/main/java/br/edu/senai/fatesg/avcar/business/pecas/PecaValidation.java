package br.edu.senai.fatesg.avcar.business.pecas;

import br.edu.senai.fatesg.avcar.core.exceptions.NegocioException;
import br.edu.senai.fatesg.avcar.core.validations.GenericValidation;
import org.springframework.stereotype.Component;

@Component
public class PecaValidation extends GenericValidation<PecaModel> implements IPecaValidation {

    @Override
    public void validar(PecaModel m) {
        naoVazio(m.getNome(), "Nome da peça");
        if (m.getPrecoVenda() < 0) throw new NegocioException("Preço de venda não pode ser negativo");
        if (m.getPrecoCusto() < 0) throw new NegocioException("Preço de custo não pode ser negativo");
        if (m.getQuantidadeEstoque() < 0) throw new NegocioException("Quantidade em estoque não pode ser negativa");
    }
}
