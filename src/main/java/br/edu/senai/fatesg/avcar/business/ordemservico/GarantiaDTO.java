package br.edu.senai.fatesg.avcar.business.ordemservico;

import lombok.Data;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Data
public class GarantiaDTO {
    private String tipo;
    private String item;
    private LocalDate dataFinalizacao;
    private LocalDate dataVencimento;
    private int diasRestantes;
    private boolean vencida;
    private String colaboradorNome;

    public GarantiaDTO() {}

    public GarantiaDTO(String tipo, String item, LocalDate dataFinalizacao, int prazoDias) {
        this(tipo, item, dataFinalizacao, prazoDias, null);
    }

    public GarantiaDTO(String tipo, String item, LocalDate dataFinalizacao, int prazoDias, String colaboradorNome) {
        this.tipo = tipo;
        this.item = item;
        this.dataFinalizacao = dataFinalizacao;
        this.colaboradorNome = colaboradorNome;
        if (dataFinalizacao != null) {
            this.dataVencimento = dataFinalizacao.plusDays(prazoDias);
            this.diasRestantes = (int) ChronoUnit.DAYS.between(LocalDate.now(), dataVencimento);
            this.vencida = diasRestantes <= 0;
        }
    }
}
