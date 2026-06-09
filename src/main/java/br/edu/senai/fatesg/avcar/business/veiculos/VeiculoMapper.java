package br.edu.senai.fatesg.avcar.business.veiculos;

import br.edu.senai.fatesg.avcar.core.helpers.IGenericMapper;
import org.springframework.stereotype.Component;

@Component
public class VeiculoMapper implements IGenericMapper<VeiculoModel, VeiculoDTO> {

    @Override
    public VeiculoDTO toDto(VeiculoModel m) {
        if (m == null) return null;
        VeiculoDTO dto = new VeiculoDTO();
        dto.setId(m.getId());
        dto.setAtivo(m.isAtivo());
        dto.setPlaca(m.getPlaca());
        dto.setChassi(m.getChassi());
        dto.setAnoFabricacao(m.getAnoFabricacao());
        dto.setAnoModelo(m.getAnoModelo());
        dto.setCor(m.getCor());
        dto.setQuilometragem(m.getQuilometragem());
        dto.setAcessorios(m.getAcessorios());
        dto.setModeloId(m.getModeloId());
        dto.setModeloNome(m.getModeloNome());
        dto.setMarcaId(m.getMarcaId());
        dto.setMarcaNome(m.getMarcaNome());
        dto.setMarcaLogoUrl(m.getMarcaLogoUrl());
        dto.setClienteId(m.getClienteId());
        dto.setClienteNome(m.getClienteNome());
        return dto;
    }

    @Override
    public VeiculoModel toEntity(VeiculoDTO dto) {
        if (dto == null) return null;
        VeiculoModel m = new VeiculoModel();
        m.setId(dto.getId());
        m.setAtivo(dto.isAtivo());
        m.setPlaca(dto.getPlaca());
        m.setChassi(dto.getChassi());
        m.setAnoFabricacao(dto.getAnoFabricacao());
        m.setAnoModelo(dto.getAnoModelo());
        m.setCor(dto.getCor());
        m.setQuilometragem(dto.getQuilometragem());
        m.setAcessorios(dto.getAcessorios());
        m.setModeloId(dto.getModeloId());
        m.setModeloNome(dto.getModeloNome());
        m.setMarcaId(dto.getMarcaId());
        m.setMarcaNome(dto.getMarcaNome());
        m.setMarcaLogoUrl(dto.getMarcaLogoUrl());
        m.setClienteId(dto.getClienteId());
        m.setClienteNome(dto.getClienteNome());
        return m;
    }
}
