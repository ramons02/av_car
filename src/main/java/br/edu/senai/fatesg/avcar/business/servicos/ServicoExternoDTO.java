package br.edu.senai.fatesg.avcar.business.servicos;

import br.edu.senai.fatesg.avcar.core.dtos.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ServicoExternoDTO extends BaseDTO {
    private Long fornecedorId;
    private String fornecedorNome;
    private String descricao;
    private double valor;
    private int garantiaDias;

    public ServicoExternoDTO() {}

    public static ServicoExternoDTO from(ServicoExterno se) {
        ServicoExternoDTO dto = new ServicoExternoDTO();
        dto.setId(se.getId());
        dto.setFornecedorId(se.getFornecedor().getId());
        dto.setFornecedorNome(se.getFornecedor().getRazaoSocial());
        dto.setDescricao(se.getDescricao());
        dto.setValor(se.getValor());
        dto.setGarantiaDias(se.getGarantiaDias());
        return dto;
    }
}
