package br.edu.senai.fatesg.avcar.business.servicos;

import br.edu.senai.fatesg.avcar.core.helpers.IGenericMapper;
import org.springframework.stereotype.Component;

@Component
public class ServicoMapper implements IGenericMapper<ServicoModel, ServicoDTO> {

    @Override
    public ServicoDTO toDto(ServicoModel m) {
        if (m == null) return null;
        ServicoDTO dto = new ServicoDTO();
        dto.setId(m.getId());
        dto.setAtivo(m.isAtivo());
        dto.setNomeServico(m.getNomeServico());
        dto.setDescricaoServico(m.getDescricaoServico());
        dto.setValorServico(m.getValorServico());
        dto.setGarantiaDias(m.getGarantiaDias());
        dto.setTempoEstimado(m.getTempoEstimado());
        return dto;
    }

    @Override
    public ServicoModel toEntity(ServicoDTO dto) {
        if (dto == null) return null;
        ServicoModel m = new ServicoModel();
        m.setId(dto.getId());
        m.setAtivo(dto.isAtivo());
        m.setNomeServico(dto.getNomeServico());
        m.setDescricaoServico(dto.getDescricaoServico());
        m.setValorServico(dto.getValorServico());
        m.setGarantiaDias(dto.getGarantiaDias());
        m.setTempoEstimado(dto.getTempoEstimado());
        return m;
    }
}
