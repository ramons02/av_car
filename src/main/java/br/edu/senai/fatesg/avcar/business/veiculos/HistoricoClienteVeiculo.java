package br.edu.senai.fatesg.avcar.business.veiculos;

import br.edu.senai.fatesg.avcar.business.clientes.Cliente;
import lombok.Data;

import java.time.LocalDate;

@Data
public class HistoricoClienteVeiculo {
    private Long idHistorico;
    private Veiculo veiculo;
    private Cliente proprietario;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    public HistoricoClienteVeiculo() {}

    public HistoricoClienteVeiculo(Long idHistorico, Veiculo veiculo, Cliente proprietario,
                                   LocalDate dataInicio, LocalDate dataFim) {
        this.idHistorico = idHistorico;
        this.veiculo = veiculo;
        this.proprietario = proprietario;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }
}
