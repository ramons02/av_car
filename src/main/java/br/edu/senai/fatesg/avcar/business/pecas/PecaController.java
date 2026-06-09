package br.edu.senai.fatesg.avcar.business.pecas;

import br.edu.senai.fatesg.avcar.core.controllers.GenericController;
import br.edu.senai.fatesg.avcar.core.services.GenericService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/pecas")
public class PecaController extends GenericController<PecaDTO> {

    private final PecaService service;

    public PecaController(PecaService service) {
        this.service = service;
    }

    @Override
    protected GenericService<?, PecaDTO, ?> getService() {
        return service;
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<PecaDTO>> buscarPorCodigo(@RequestParam("codigo") long codigo) {
        return ResponseEntity.ok(service.buscarPorCodigo(codigo));
    }

    @GetMapping("/estoque-baixo")
    public ResponseEntity<List<PecaDTO>> estoqueBaixo(@RequestParam(value = "min", defaultValue = "5") int min) {
        return ResponseEntity.ok(service.buscarEstoqueBaixo(min));
    }

    @PostMapping
    public ResponseEntity<PecaDTO> salvar(@RequestBody PecaRequest req) {
        PecaModel model = new PecaModel();
        model.setCodigoNacional(req.codigoNacional());
        model.setCodigoInterno(req.codigoInterno());
        model.setNome(req.nome());
        model.setDescricao(req.descricao());
        model.setFabricante(req.fabricante());
        model.setCategoria(req.categoria());
        model.setPrecoCusto(req.precoCusto());
        model.setPrecoVenda(req.precoVenda());
        model.setQuantidadeEstoque(req.quantidadeEstoque());
        model.setGarantiaPeca(req.garantiaPeca());
        model.setDataCompraPeca(req.dataCompraPeca());
        model.setFornecedorId(req.fornecedorId());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(model));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PecaDTO> atualizar(@PathVariable Long id, @RequestBody PecaRequest req) {
        PecaModel model = new PecaModel();
        model.setId(id);
        model.setCodigoNacional(req.codigoNacional());
        model.setCodigoInterno(req.codigoInterno());
        model.setNome(req.nome());
        model.setDescricao(req.descricao());
        model.setFabricante(req.fabricante());
        model.setCategoria(req.categoria());
        model.setPrecoCusto(req.precoCusto());
        model.setPrecoVenda(req.precoVenda());
        model.setQuantidadeEstoque(req.quantidadeEstoque());
        model.setGarantiaPeca(req.garantiaPeca());
        model.setDataCompraPeca(req.dataCompraPeca());
        model.setFornecedorId(req.fornecedorId());
        service.atualizar(model);
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    public record PecaRequest(long codigoNacional, String codigoInterno, String nome, String descricao,
                              String fabricante, String categoria, double precoCusto, double precoVenda,
                              int quantidadeEstoque, int garantiaPeca, LocalDate dataCompraPeca,
                              Long fornecedorId) {}
}
