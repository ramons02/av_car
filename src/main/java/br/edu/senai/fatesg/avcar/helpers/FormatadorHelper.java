package br.edu.senai.fatesg.avcar.helpers;

import br.edu.senai.fatesg.avcar.business.ordemservico.StatusOrdemServico;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class FormatadorHelper {
    private static final DateTimeFormatter DATA_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final String MASCARA_CPF = "%s.%s.%s-%s";
    private static final String MASCARA_CNPJ = "%s.%s.%s/%s-%s";
    private static final String MASCARA_TELEFONE = "(%s) %s-%s";

    private FormatadorHelper() {}

    public static String formatarData(LocalDate data) {
        return data != null ? data.format(DATA_BR) : "";
    }

    public static String formatarCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) return cpf;
        return MASCARA_CPF.formatted(
            cpf.substring(0, 3), cpf.substring(3, 6),
            cpf.substring(6, 9), cpf.substring(9, 11)
        );
    }

    public static String formatarCnpj(String cnpj) {
        if (cnpj == null || cnpj.length() != 14) return cnpj;
        return MASCARA_CNPJ.formatted(
            cnpj.substring(0, 2), cnpj.substring(2, 5),
            cnpj.substring(5, 8), cnpj.substring(8, 12),
            cnpj.substring(12, 14)
        );
    }

    public static String formatarTelefone(String telefone) {
        if (telefone == null || telefone.length() < 10) return telefone;
        String ddd = telefone.substring(0, 2);
        String parte1 = telefone.substring(2, 7);
        String parte2 = telefone.substring(7);
        return MASCARA_TELEFONE.formatted(ddd, parte1, parte2);
    }

    public static String statusParaDescricao(StatusOrdemServico status) {
        return status.getDescricao();
    }
}

