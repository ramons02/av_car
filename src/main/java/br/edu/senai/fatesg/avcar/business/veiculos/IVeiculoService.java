package br.edu.senai.fatesg.avcar.business.veiculos;

import br.edu.senai.fatesg.avcar.core.services.IGenericService;

import java.util.List;

public interface IVeiculoService extends IGenericService<VeiculoDTO> {
    List<VeiculoDTO> buscarPorPlaca(String placa);
    List<VeiculoDTO> buscarPorCliente(Long clienteId);
    VeiculoDTO salvar(VeiculoModel model);
    void atualizar(VeiculoModel model);
    List<MarcaDTO> listarMarcas();
    List<ModeloDTO> listarModelosPorMarca(Long marcaId);
}
