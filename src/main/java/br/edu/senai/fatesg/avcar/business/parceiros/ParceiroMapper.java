package br.edu.senai.fatesg.avcar.business.parceiros;

import br.edu.senai.fatesg.avcar.core.helpers.IGenericMapper;
import org.springframework.stereotype.Component;

@Component
public class ParceiroMapper implements IGenericMapper<ParceiroModel, ParceiroDTO> {

    @Override
    public ParceiroDTO toDto(ParceiroModel m) {
        if (m == null) return null;
        ParceiroDTO dto = new ParceiroDTO();
        dto.setId(m.getId());
        dto.setAtivo(m.isAtivo());
        dto.setNome(m.getNome());
        dto.setCnpj(m.getCnpj());
        dto.setTipoServico(m.getTipoServico());
        dto.setTelefone(m.getTelefone());
        dto.setEmail(m.getEmail());
        return dto;
    }

    @Override
    public ParceiroModel toEntity(ParceiroDTO dto) {
        if (dto == null) return null;
        ParceiroModel m = new ParceiroModel();
        m.setId(dto.getId());
        m.setAtivo(dto.isAtivo());
        m.setNome(dto.getNome());
        m.setCnpj(dto.getCnpj());
        m.setTipoServico(dto.getTipoServico());
        m.setTelefone(dto.getTelefone());
        m.setEmail(dto.getEmail());
        return m;
    }
}
