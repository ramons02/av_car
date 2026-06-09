package br.edu.senai.fatesg.avcar.business.ordemservico;

public enum StatusOrdemServico {
    ABERTA("Aberta", "Aberta"),
    EM_ORCAMENTO("Em orçamento", "Em orçamento"),
    AGUARDANDO_PECA("Aguardando peça", "Aguardando peça"),
    EM_EXECUCAO("Em execução", "Em execução"),
    FINALIZADA("Finalizada", "Finalizada"),
    CANCELADA("Cancelada", "Cancelada");

    private final String descricao;
    private final String rotulo;

    StatusOrdemServico(String descricao, String rotulo) {
        this.descricao = descricao;
        this.rotulo = rotulo;
    }

    public String getDescricao() { return descricao; }
    public String getRotulo() { return rotulo; }

    public static StatusOrdemServico fromRotulo(String rotulo) {
        for (StatusOrdemServico s : values()) {
            if (s.rotulo.equalsIgnoreCase(rotulo) || s.name().equalsIgnoreCase(rotulo)) {
                return s;
            }
        }
        return ABERTA;
    }
}
