package br.edu.senai.fatesg.avcar.business.parceiros;

import br.edu.senai.fatesg.avcar.business.fornecedores.Fornecedor;
import br.edu.senai.fatesg.avcar.business.servicos.Servico;
import br.edu.senai.fatesg.avcar.business.servicos.ServicoExterno;

// PADRÃO ADAPTER: Converte a interface de uma classe para outra interface
// que o cliente espera encontrar, permitindo que classes incompatíveis
// trabalhem juntas. Aplicado aqui para adaptar Fornecedor + ServicoExterno
// (parceiro externo) à interface Servico, permitindo que serviços terceirizados
// sejam tratados como serviços internos sem modificar o código consumidor.
public class ParceiroExternoAdapter extends Servico {
    private final Fornecedor fornecedor;
    private final ServicoExterno servicoExterno;

    public ParceiroExternoAdapter(Fornecedor fornecedor, ServicoExterno servicoExterno) {
        super(null, servicoExterno.getDescricao(), "Serviço terceirizado: " + fornecedor.getRazaoSocial(),
              servicoExterno.getValor(), servicoExterno.getGarantiaDias(), null);
        this.fornecedor = fornecedor;
        this.servicoExterno = servicoExterno;
    }

    public Fornecedor getFornecedor() { return fornecedor; }
    public ServicoExterno getServicoExterno() { return servicoExterno; }
}
