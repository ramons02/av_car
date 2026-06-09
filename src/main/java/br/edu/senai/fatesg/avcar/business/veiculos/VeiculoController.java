package br.edu.senai.fatesg.avcar.business.veiculos;

import br.edu.senai.fatesg.avcar.core.controllers.GenericController;
import br.edu.senai.fatesg.avcar.core.services.GenericService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/veiculos")
public class VeiculoController extends GenericController<VeiculoDTO> {

    private final VeiculoService service;

    public VeiculoController(VeiculoService service) {
        this.service = service;
    }

    @Override
    protected GenericService<?, VeiculoDTO, ?> getService() {
        return service;
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<VeiculoDTO>> buscarPorPlaca(@RequestParam("placa") String placa) {
        return ResponseEntity.ok(service.buscarPorPlaca(placa));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<VeiculoDTO>> buscarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(service.buscarPorCliente(clienteId));
    }

    @PostMapping
    public ResponseEntity<VeiculoDTO> salvar(@RequestBody VeiculoRequest req) {
        VeiculoModel model = new VeiculoModel();
        model.setPlaca(req.placa());
        model.setChassi(req.chassi());
        model.setAnoFabricacao(req.anoFabricacao());
        model.setAnoModelo(req.anoModelo());
        model.setCor(req.cor());
        model.setQuilometragem(req.quilometragem());
        model.setAcessorios(req.acessorios());
        model.setModeloId(req.modeloId());
        model.setClienteId(req.clienteId());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(model));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VeiculoDTO> atualizar(@PathVariable Long id, @RequestBody VeiculoRequest req) {
        VeiculoModel model = new VeiculoModel();
        model.setId(id);
        model.setPlaca(req.placa());
        model.setChassi(req.chassi());
        model.setAnoFabricacao(req.anoFabricacao());
        model.setAnoModelo(req.anoModelo());
        model.setCor(req.cor());
        model.setQuilometragem(req.quilometragem());
        model.setAcessorios(req.acessorios());
        model.setModeloId(req.modeloId());
        model.setClienteId(req.clienteId());
        service.atualizar(model);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/marcas")
    public ResponseEntity<List<MarcaDTO>> listarMarcas() {
        return ResponseEntity.ok(service.listarMarcas());
    }

    @GetMapping("/marcas/{marcaId}/modelos")
    public ResponseEntity<List<ModeloDTO>> listarModelos(@PathVariable Long marcaId) {
        return ResponseEntity.ok(service.listarModelosPorMarca(marcaId));
    }

    public record VeiculoRequest(String placa, String chassi, int anoFabricacao,
                                 int anoModelo, String cor, int quilometragem,
                                 String acessorios, Long modeloId, Long clienteId) {}
}
