package br.edu.senai.fatesg.avcar.business.fornecedores;

import br.edu.senai.fatesg.avcar.core.helpers.IGenericMapper;
import org.springframework.stereotype.Component;

@Component
public class FornecedorMapper implements IGenericMapper<FornecedorModel, FornecedorDTO> {

    @Override
    public FornecedorDTO toDto(FornecedorModel m) {
        if (m == null) return null;
        FornecedorDTO dto = new FornecedorDTO();
        dto.setId(m.getId());
        dto.setAtivo(m.isAtivo());
        dto.setRazaoSocial(m.getRazaoSocial());
        dto.setCnpj(m.getCnpj());
        dto.setDdi(m.getDdi());
        dto.setDdd(m.getDdd());
        dto.setNumeroFornecedor(m.getNumeroFornecedor());
        dto.setEmail(m.getEmail());
        dto.setEnderecoFornecedor(m.getEnderecoFornecedor());
        dto.setBairroFornecedor(m.getBairroFornecedor());
        dto.setCidadeFornecedor(m.getCidadeFornecedor());
        dto.setEstadoFornecedor(m.getEstadoFornecedor());
        dto.setCepFornecedor(m.getCepFornecedor());
        return dto;
    }

    @Override
    public FornecedorModel toEntity(FornecedorDTO dto) {
        if (dto == null) return null;
        FornecedorModel m = new FornecedorModel();
        m.setId(dto.getId());
        m.setAtivo(dto.isAtivo());
        m.setRazaoSocial(dto.getRazaoSocial());
        m.setCnpj(dto.getCnpj());
        m.setDdi(dto.getDdi());
        m.setDdd(dto.getDdd());
        m.setNumeroFornecedor(dto.getNumeroFornecedor());
        m.setEmail(dto.getEmail());
        m.setEnderecoFornecedor(dto.getEnderecoFornecedor());
        m.setBairroFornecedor(dto.getBairroFornecedor());
        m.setCidadeFornecedor(dto.getCidadeFornecedor());
        m.setEstadoFornecedor(dto.getEstadoFornecedor());
        m.setCepFornecedor(dto.getCepFornecedor());
        return m;
    }
}
