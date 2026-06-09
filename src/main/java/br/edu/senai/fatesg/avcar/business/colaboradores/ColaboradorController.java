package br.edu.senai.fatesg.avcar.business.colaboradores;

import br.edu.senai.fatesg.avcar.core.controllers.GenericController;
import br.edu.senai.fatesg.avcar.core.services.GenericService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/colaboradores")
public class ColaboradorController extends GenericController<ColaboradorDTO> {

    private final ColaboradorService service;

    public ColaboradorController(ColaboradorService service) {
        this.service = service;
    }

    @Override
    protected GenericService<?, ColaboradorDTO, ?> getService() {
        return service;
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ColaboradorDTO>> buscarPorNome(@RequestParam("nome") String nome) {
        return ResponseEntity.ok(service.buscarPorNome(nome));
    }

    @PostMapping
    public ResponseEntity<ColaboradorDTO> salvar(@RequestBody ColaboradorRequest req) {
        ColaboradorModel model = new ColaboradorModel();
        model.setNome(req.nome());
        model.setCpf(req.cpf());
        model.setDdi1(req.ddi1());
        model.setDdd1(req.ddd1());
        model.setNumerotelefone1(req.numerotelefone1());
        model.setEmail(req.email());
        model.setFuncaoIds(req.funcaoIds());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(model));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ColaboradorDTO> atualizar(@PathVariable Long id, @RequestBody ColaboradorRequest req) {
        ColaboradorModel model = new ColaboradorModel();
        model.setId(id);
        model.setNome(req.nome());
        model.setCpf(req.cpf());
        model.setDdi1(req.ddi1());
        model.setDdd1(req.ddd1());
        model.setNumerotelefone1(req.numerotelefone1());
        model.setEmail(req.email());
        model.setFuncaoIds(req.funcaoIds());
        service.atualizar(model);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/funcoes")
    public ResponseEntity<List<FuncaoDTO>> listarFuncoes() {
        return ResponseEntity.ok(service.listarFuncoes());
    }

    public record ColaboradorRequest(String nome, String cpf, String ddi1, String ddd1,
                                     String numerotelefone1, String email, List<Long> funcaoIds) {}
}
