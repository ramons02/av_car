package br.edu.senai.fatesg.avcar.business.clientes;

import br.edu.senai.fatesg.avcar.core.helpers.IGenericMapper;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper implements IGenericMapper<ClienteModel, ClienteDTO> {

    @Override
    public ClienteDTO toDto(ClienteModel e) {
        if (e == null) return null;
        ClienteDTO dto = new ClienteDTO();
        dto.setId(e.getId());
        dto.setAtivo(e.isAtivo());
        dto.setNome(e.getNome());
        dto.setTipo(e.getTipo());
        dto.setCpf(e.getCpf());
        dto.setCnpj(e.getCnpj());
        dto.setRg(e.getRg());
        dto.setDataNascimento(e.getDataNascimento());
        dto.setRazaoSocial(e.getRazaoSocial());
        dto.setInscricaoEstadual(e.getInscricaoEstadual());
        dto.setTelefone(e.getTelefone());
        dto.setEmail(e.getEmail());
        dto.setEndereco(e.getEndereco());
        dto.setBairro(e.getBairro());
        dto.setCidade(e.getCidade());
        dto.setEstado(e.getEstado());
        dto.setCep(e.getCep());
        dto.setObservacoes(e.getObservacoes());
        return dto;
    }

    @Override
    public ClienteModel toEntity(ClienteDTO dto) {
        if (dto == null) return null;
        ClienteModel e = new ClienteModel();
        e.setId(dto.getId());
        e.setAtivo(dto.isAtivo());
        e.setNome(dto.getNome());
        e.setTipo(dto.getTipo());
        e.setCpf(dto.getCpf());
        e.setCnpj(dto.getCnpj());
        e.setRg(dto.getRg());
        e.setDataNascimento(dto.getDataNascimento());
        e.setRazaoSocial(dto.getRazaoSocial());
        e.setInscricaoEstadual(dto.getInscricaoEstadual());
        e.setTelefone(dto.getTelefone());
        e.setEmail(dto.getEmail());
        e.setEndereco(dto.getEndereco());
        e.setBairro(dto.getBairro());
        e.setCidade(dto.getCidade());
        e.setEstado(dto.getEstado());
        e.setCep(dto.getCep());
        e.setObservacoes(dto.getObservacoes());
        return e;
    }
}
