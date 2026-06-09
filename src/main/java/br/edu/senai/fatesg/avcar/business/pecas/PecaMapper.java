package br.edu.senai.fatesg.avcar.business.pecas;

import br.edu.senai.fatesg.avcar.core.helpers.IGenericMapper;
import org.springframework.stereotype.Component;

@Component
public class PecaMapper implements IGenericMapper<PecaModel, PecaDTO> {

    @Override
    public PecaDTO toDto(PecaModel m) {
        if (m == null) return null;
        PecaDTO dto = new PecaDTO();
        dto.setId(m.getId());
        dto.setAtivo(m.isAtivo());
        dto.setCodigoNacional(m.getCodigoNacional());
        dto.setCodigoInterno(m.getCodigoInterno());
        dto.setNome(m.getNome());
        dto.setDescricao(m.getDescricao());
        dto.setFabricante(m.getFabricante());
        dto.setCategoria(m.getCategoria());
        dto.setPrecoCusto(m.getPrecoCusto());
        dto.setPrecoVenda(m.getPrecoVenda());
        dto.setQuantidadeEstoque(m.getQuantidadeEstoque());
        dto.setGarantiaPeca(m.getGarantiaPeca());
        dto.setDataCompraPeca(m.getDataCompraPeca());
        dto.setFornecedorId(m.getFornecedorId());
        dto.setFornecedorNome(m.getFornecedorNome());
        return dto;
    }

    @Override
    public PecaModel toEntity(PecaDTO dto) {
        if (dto == null) return null;
        PecaModel m = new PecaModel();
        m.setId(dto.getId());
        m.setAtivo(dto.isAtivo());
        m.setCodigoNacional(dto.getCodigoNacional());
        m.setCodigoInterno(dto.getCodigoInterno());
        m.setNome(dto.getNome());
        m.setDescricao(dto.getDescricao());
        m.setFabricante(dto.getFabricante());
        m.setCategoria(dto.getCategoria());
        m.setPrecoCusto(dto.getPrecoCusto());
        m.setPrecoVenda(dto.getPrecoVenda());
        m.setQuantidadeEstoque(dto.getQuantidadeEstoque());
        m.setGarantiaPeca(dto.getGarantiaPeca());
        m.setDataCompraPeca(dto.getDataCompraPeca());
        m.setFornecedorId(dto.getFornecedorId());
        m.setFornecedorNome(dto.getFornecedorNome());
        return m;
    }
}
