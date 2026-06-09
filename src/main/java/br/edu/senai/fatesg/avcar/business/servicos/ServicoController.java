package br.edu.senai.fatesg.avcar.business.servicos;

import br.edu.senai.fatesg.avcar.core.controllers.GenericController;
import br.edu.senai.fatesg.avcar.core.services.GenericService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicos")
public class ServicoController extends GenericController<ServicoDTO> {

    private final ServicoService service;

    public ServicoController(ServicoService service) {
        this.service = service;
    }

    @Override
    protected GenericService<?, ServicoDTO, ?> getService() {
        return service;
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ServicoDTO>> buscarPorNome(@RequestParam("nome") String nome) {
        return ResponseEntity.ok(service.buscarPorNome(nome));
    }

    @PostMapping
    public ResponseEntity<ServicoDTO> salvar(@RequestBody ServicoRequest req) {
        ServicoModel model = new ServicoModel();
        model.setNomeServico(req.nomeServico());
        model.setDescricaoServico(req.descricaoServico());
        model.setValorServico(req.valorServico());
        model.setGarantiaDias(req.garantiaDias());
        model.setTempoEstimado(req.tempoEstimado());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(model));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicoDTO> atualizar(@PathVariable Long id, @RequestBody ServicoRequest req) {
        ServicoModel model = new ServicoModel();
        model.setId(id);
        model.setNomeServico(req.nomeServico());
        model.setDescricaoServico(req.descricaoServico());
        model.setValorServico(req.valorServico());
        model.setGarantiaDias(req.garantiaDias());
        model.setTempoEstimado(req.tempoEstimado());
        service.atualizar(model);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    public record ServicoRequest(String nomeServico, String descricaoServico, double valorServico,
                                  int garantiaDias, String tempoEstimado) {}
}
