package br.edu.senai.fatesg.avcar.business.parceiros;

import br.edu.senai.fatesg.avcar.core.dtos.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ParceiroExternoDTO extends BaseDTO {
    private String nome;
    private String cnpj;
    private String tipoServico;
    private String telefone;
    private String email;

    public static ParceiroExternoDTO from(ParceiroExterno p) {
        ParceiroExternoDTO dto = new ParceiroExternoDTO();
        dto.setId(p.getId());
        dto.setNome(p.getNome());
        dto.setCnpj(p.getCnpj());
        dto.setTipoServico(p.getTipoServico());
        dto.setTelefone(p.getTelefone());
        dto.setEmail(p.getEmail());
        dto.setAtivo(p.isAtivo());
        return dto;
    }
}
