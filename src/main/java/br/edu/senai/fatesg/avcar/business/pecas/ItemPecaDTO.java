package br.edu.senai.fatesg.avcar.business.pecas;

import br.edu.senai.fatesg.avcar.core.dtos.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ItemPecaDTO extends BaseDTO {
    private Long pecaId;
    private String pecaNome;
    private int quantidade;
    private double valorUnitario;
    private double subtotal;

    public ItemPecaDTO() {}

    public static ItemPecaDTO from(ItemPeca item) {
        ItemPecaDTO dto = new ItemPecaDTO();
        dto.setId(item.getId());
        dto.setPecaId(item.getPeca().getId());
        dto.setPecaNome(item.getPeca().getNome());
        dto.setQuantidade(item.getQuantidade());
        dto.setValorUnitario(item.getValorUnitario());
        dto.setSubtotal(item.getSubtotal());
        return dto;
    }
}
