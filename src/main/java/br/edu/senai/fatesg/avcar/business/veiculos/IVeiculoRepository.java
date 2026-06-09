package br.edu.senai.fatesg.avcar.business.veiculos;

import br.edu.senai.fatesg.avcar.core.repositories.IGenericRepository;

import java.util.List;

public interface IVeiculoRepository extends IGenericRepository<VeiculoModel> {
    List<VeiculoModel> buscarPorPlaca(String placa);
    List<VeiculoModel> buscarPorCliente(Long clienteId);
    List<MarcaDTO> listarMarcas();
    List<ModeloDTO> listarModelosPorMarca(Long marcaId);
}
