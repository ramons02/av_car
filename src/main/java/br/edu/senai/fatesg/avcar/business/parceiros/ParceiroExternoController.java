package br.edu.senai.fatesg.avcar.business.parceiros;

import br.edu.senai.fatesg.avcar.core.controllers.GenericController;
import br.edu.senai.fatesg.avcar.core.services.GenericService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parceiros")
public class ParceiroExternoController extends GenericController<ParceiroDTO> {

    private final ParceiroService service;

    public ParceiroExternoController(ParceiroService service) {
        this.service = service;
    }

    @Override
    protected GenericService<?, ParceiroDTO, ?> getService() {
        return service;
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ParceiroDTO>> buscarPorNome(@RequestParam("nome") String nome) {
        return ResponseEntity.ok(service.buscarPorNome(nome));
    }

    @PostMapping
    public ResponseEntity<ParceiroDTO> salvar(@RequestBody ParceiroRequest req) {
        ParceiroModel model = new ParceiroModel();
        model.setNome(req.nome());
        model.setCnpj(req.cnpj());
        model.setTipoServico(req.tipoServico());
        model.setTelefone(req.telefone());
        model.setEmail(req.email());
        model.setAtivo(req.ativo());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(model));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParceiroDTO> atualizar(@PathVariable Long id, @RequestBody ParceiroRequest req) {
        ParceiroModel model = new ParceiroModel();
        model.setId(id);
        model.setNome(req.nome());
        model.setCnpj(req.cnpj());
        model.setTipoServico(req.tipoServico());
        model.setTelefone(req.telefone());
        model.setEmail(req.email());
        model.setAtivo(req.ativo());
        service.atualizar(model);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    public record ParceiroRequest(String nome, String cnpj, String tipoServico,
                                   String telefone, String email, boolean ativo) {}
}
