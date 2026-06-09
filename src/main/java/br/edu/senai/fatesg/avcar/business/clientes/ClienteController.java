package br.edu.senai.fatesg.avcar.business.clientes;

import br.edu.senai.fatesg.avcar.core.controllers.GenericController;
import br.edu.senai.fatesg.avcar.core.services.GenericService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController extends GenericController<ClienteDTO> {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @Override
    protected GenericService<?, ClienteDTO, ?> getService() {
        return service;
    }

    @PostMapping
    public ResponseEntity<ClienteDTO> criar(@RequestBody ClienteModel model) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(model));
    }

    @PostMapping("/pf")
    public ResponseEntity<ClienteDTO> criarPF(@RequestBody CriarPFRequest req) {
        ClienteModel model = new ClienteModel();
        model.setTipo("PF");
        model.setNome(req.nome());
        model.setEndereco(req.endereco());
        model.setBairro(req.bairro());
        model.setCidade(req.cidade());
        model.setEstado(req.estado());
        model.setCep(req.cep());
        model.setTelefone(req.telefone());
        model.setEmail(req.email());
        model.setCpf(req.cpf());
        model.setRg(req.rg());
        model.setDataNascimento(req.dataNascimento());
        model.setObservacoes(req.observacoes());
        model.setAtivo(true);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(model));
    }

    @PostMapping("/pj")
    public ResponseEntity<ClienteDTO> criarPJ(@RequestBody CriarPJRequest req) {
        ClienteModel model = new ClienteModel();
        model.setTipo("PJ");
        model.setNome(req.nome());
        model.setEndereco(req.endereco());
        model.setBairro(req.bairro());
        model.setCidade(req.cidade());
        model.setEstado(req.estado());
        model.setCep(req.cep());
        model.setTelefone(req.telefone());
        model.setEmail(req.email());
        model.setCnpj(req.cnpj());
        model.setInscricaoEstadual(req.inscricaoEstadual());
        model.setRazaoSocial(req.razaoSocial() != null && !req.razaoSocial().isBlank()
            ? req.razaoSocial() : req.nome());
        model.setObservacoes(req.observacoes());
        model.setAtivo(true);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(model));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> atualizar(@PathVariable Long id, @RequestBody AtualizarClienteRequest req) {
        return ResponseEntity.ok(service.atualizarPorRequest(id, req));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ClienteDTO>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(service.buscarPorNome(nome));
    }

    public record CriarPFRequest(String nome, String endereco, String bairro, String cidade,
                                 String estado, String cep, String telefone, String email,
                                 String cpf, String rg, LocalDate dataNascimento,
                                 String observacoes) {}

    public record CriarPJRequest(String nome, String endereco, String bairro, String cidade,
                                 String estado, String cep, String telefone, String email,
                                 String cnpj, String inscricaoEstadual, String razaoSocial,
                                 String observacoes) {}

    public record AtualizarClienteRequest(String nome, String endereco, String bairro,
                                          String cidade, String estado, String cep,
                                          String telefone, String email,
                                          String documento, String cpf, String cnpj,
                                          String inscricaoEstadual, String rg,
                                          String razaoSocial, String observacoes) {}
}
