package br.edu.senai.fatesg.avcar.business.fornecedores;

import br.edu.senai.fatesg.avcar.core.controllers.GenericController;
import br.edu.senai.fatesg.avcar.core.services.GenericService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fornecedores")
public class FornecedorController extends GenericController<FornecedorDTO> {

    private final FornecedorService service;

    public FornecedorController(FornecedorService service) {
        this.service = service;
    }

    @Override
    protected GenericService<?, FornecedorDTO, ?> getService() {
        return service;
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<FornecedorDTO>> buscarPorNome(@RequestParam("nome") String nome) {
        return ResponseEntity.ok(service.buscarPorNome(nome));
    }

    @PostMapping
    public ResponseEntity<FornecedorDTO> salvar(@RequestBody FornecedorRequest req) {
        FornecedorModel model = new FornecedorModel();
        model.setRazaoSocial(req.razaoSocial());
        model.setCnpj(req.cnpj());
        model.setDdi(req.ddi());
        model.setDdd(req.ddd());
        model.setNumeroFornecedor(req.numeroFornecedor());
        model.setEmail(req.email());
        model.setEnderecoFornecedor(req.enderecoFornecedor());
        model.setBairroFornecedor(req.bairroFornecedor());
        model.setCidadeFornecedor(req.cidadeFornecedor());
        model.setEstadoFornecedor(req.estadoFornecedor());
        model.setCepFornecedor(req.cepFornecedor());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(model));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FornecedorDTO> atualizar(@PathVariable Long id, @RequestBody FornecedorRequest req) {
        FornecedorModel model = new FornecedorModel();
        model.setId(id);
        model.setRazaoSocial(req.razaoSocial());
        model.setCnpj(req.cnpj());
        model.setDdi(req.ddi());
        model.setDdd(req.ddd());
        model.setNumeroFornecedor(req.numeroFornecedor());
        model.setEmail(req.email());
        model.setEnderecoFornecedor(req.enderecoFornecedor());
        model.setBairroFornecedor(req.bairroFornecedor());
        model.setCidadeFornecedor(req.cidadeFornecedor());
        model.setEstadoFornecedor(req.estadoFornecedor());
        model.setCepFornecedor(req.cepFornecedor());
        service.atualizar(model);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    public record FornecedorRequest(String razaoSocial, String cnpj, String ddi, String ddd,
                                    String numeroFornecedor, String email, String enderecoFornecedor,
                                    String bairroFornecedor, String cidadeFornecedor,
                                    String estadoFornecedor, int cepFornecedor) {}
}
