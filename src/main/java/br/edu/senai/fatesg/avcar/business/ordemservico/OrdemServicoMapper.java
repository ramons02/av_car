package br.edu.senai.fatesg.avcar.business.ordemservico;

import br.edu.senai.fatesg.avcar.core.helpers.IGenericMapper;
import org.springframework.stereotype.Component;

@Component
public class OrdemServicoMapper implements IGenericMapper<OrdemServicoModel, OrdemServicoDTO> {

    @Override
    public OrdemServicoDTO toDto(OrdemServicoModel e) {
        if (e == null) return null;
        OrdemServicoDTO dto = new OrdemServicoDTO();
        dto.setId(e.getId());
        dto.setAtivo(e.isAtivo());
        dto.setIdVeiculo(e.getIdVeiculo());
        dto.setIdCliente(e.getIdCliente());
        dto.setIdColaboradorResponsavel(e.getIdColaboradorResponsavel());
        dto.setStatus(e.getStatus());
        dto.setDefeito(e.getDefeito());
        dto.setSolucao(e.getSolucao());
        dto.setDataAbertura(e.getDataAbertura());
        dto.setDataFechamento(e.getDataFechamento());
        dto.setDataPrevisao(e.getDataPrevisao());
        dto.setValorTotal(e.getValorTotal());
        dto.setObservacoes(e.getObservacoes());
        return dto;
    }

    @Override
    public OrdemServicoModel toEntity(OrdemServicoDTO dto) {
        if (dto == null) return null;
        OrdemServicoModel e = new OrdemServicoModel();
        e.setId(dto.getId());
        e.setAtivo(dto.isAtivo());
        e.setIdVeiculo(dto.getIdVeiculo());
        e.setIdCliente(dto.getIdCliente());
        e.setIdColaboradorResponsavel(dto.getIdColaboradorResponsavel());
        e.setStatus(dto.getStatus());
        e.setDefeito(dto.getDefeito());
        e.setSolucao(dto.getSolucao());
        e.setDataAbertura(dto.getDataAbertura());
        e.setDataFechamento(dto.getDataFechamento());
        e.setDataPrevisao(dto.getDataPrevisao());
        e.setValorTotal(dto.getValorTotal());
        e.setObservacoes(dto.getObservacoes());
        return e;
    }
}
