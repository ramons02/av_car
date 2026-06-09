package br.edu.senai.fatesg.avcar.business.colaboradores;

import br.edu.senai.fatesg.avcar.core.helpers.IGenericMapper;
import org.springframework.stereotype.Component;

@Component
public class ColaboradorMapper implements IGenericMapper<ColaboradorModel, ColaboradorDTO> {

    @Override
    public ColaboradorDTO toDto(ColaboradorModel m) {
        if (m == null) return null;
        ColaboradorDTO dto = new ColaboradorDTO();
        dto.setId(m.getId());
        dto.setAtivo(m.isAtivo());
        dto.setNome(m.getNome());
        dto.setMatricula(m.getMatricula());
        dto.setCpf(m.getCpf());
        dto.setEmail(m.getEmail());
        dto.setDataAdmissao(m.getDataAdmissao());
        dto.setDataDemissao(m.getDataDemissao());
        dto.setSalario(m.getSalario());
        dto.setObservacoes(m.getObservacoes());
        dto.setFuncoes(m.getFuncaoNomes());
        // Compor telefone a partir de partes
        String ddi = m.getDdi1();
        String ddd = m.getDdd1();
        String num = m.getNumerotelefone1();
        if (ddd != null && !ddd.isBlank() && num != null && !num.isBlank()) {
            dto.setTelefone((ddi != null ? ddi : "55") + ddd + num);
        } else {
            dto.setTelefone("");
        }
        return dto;
    }

    @Override
    public ColaboradorModel toEntity(ColaboradorDTO dto) {
        if (dto == null) return null;
        ColaboradorModel m = new ColaboradorModel();
        m.setId(dto.getId());
        m.setAtivo(dto.isAtivo());
        m.setNome(dto.getNome());
        m.setMatricula(dto.getMatricula());
        m.setCpf(dto.getCpf());
        m.setEmail(dto.getEmail());
        m.setDataAdmissao(dto.getDataAdmissao());
        m.setDataDemissao(dto.getDataDemissao());
        m.setSalario(dto.getSalario());
        m.setObservacoes(dto.getObservacoes());
        return m;
    }
}
