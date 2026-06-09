package br.edu.senai.fatesg.avcar.business.clientes;

import br.edu.senai.fatesg.avcar.core.repositories.AbstractRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository("businessClienteRepository")
public class ClienteRepositoryImpl extends AbstractRepository<ClienteModel> implements IClienteRepository {

    private static final String SQL_SELECT =
        "SELECT c.idcliente, c.statuscliente, c.observacoes, " +
        "CASE WHEN pf.idpessoafisica IS NOT NULL THEN 'PF' ELSE 'PJ' END as tipo, " +
        "pf.idpessoafisica, pf.cpf, pf.rg, pf.datanascimento, " +
        "pj.idpessoajuridica, pj.cnpj, pj.inscricaoestadual, pj.razaosocial, " +
        "p.idpessoa, p.nome, p.ddi1, p.ddd1, p.numerotelefone1, " +
        "p.email, p.endereco, p.bairro, p.cidade, p.estado, p.cep, p.datacadastro " +
        "FROM cliente c " +
        "LEFT JOIN pessoafisica pf ON c.idpessoafisica = pf.idpessoafisica " +
        "LEFT JOIN pessoajuridica pj ON c.idpessoajuridica = pj.idpessoajuridica " +
        "LEFT JOIN pessoa p ON COALESCE(pf.idpessoa, pj.idpessoa) = p.idpessoa";

    private final RowMapper<ClienteModel> clienteMapper = (rs, rowNum) -> {
        ClienteModel m = new ClienteModel();
        m.setId(rs.getLong("idcliente"));
        m.setTipo(rs.getString("tipo"));
        m.setIdPessoa(rs.getLong("idpessoa"));
        m.setNome(rs.getString("nome"));
        m.setEmail(rs.getString("email"));
        m.setEndereco(rs.getString("endereco"));
        m.setBairro(rs.getString("bairro"));
        m.setCidade(rs.getString("cidade"));
        m.setEstado(rs.getString("estado"));
        Object cepObj = rs.getObject("cep");
        m.setCep(cepObj != null ? String.valueOf(cepObj) : null);
        m.setObservacoes(rs.getString("observacoes"));
        String status = rs.getString("statuscliente");
        m.setAtivo("Ativo".equalsIgnoreCase(status));
        Date dt = rs.getDate("datacadastro");
        if (dt != null) m.setDataCadastro(dt.toLocalDate());

        // Telefone: concat ddi + ddd + numero
        String ddi = rs.getString("ddi1");
        String ddd = rs.getString("ddd1");
        String numero = rs.getString("numerotelefone1");
        StringBuilder tel = new StringBuilder();
        if (ddi != null) tel.append(ddi);
        if (ddd != null) tel.append(ddd);
        if (numero != null) tel.append(numero);
        m.setTelefone(tel.length() > 0 ? tel.toString() : null);

        if ("PF".equals(m.getTipo())) {
            m.setIdPessoaFisica(rs.getLong("idpessoafisica"));
            m.setCpf(rs.getString("cpf"));
            m.setRg(rs.getString("rg"));
            Date nasc = rs.getDate("datanascimento");
            if (nasc != null) m.setDataNascimento(nasc.toLocalDate());
        } else {
            m.setIdPessoaJuridica(rs.getLong("idpessoajuridica"));
            m.setCnpj(rs.getString("cnpj"));
            m.setInscricaoEstadual(rs.getString("inscricaoestadual"));
            m.setRazaoSocial(rs.getString("razaosocial"));
        }
        return m;
    };

    public ClienteRepositoryImpl(JdbcTemplate jdbc) {
        super(jdbc);
    }

    @Override
    protected String getTableName() { return "cliente"; }

    @Override
    protected RowMapper<ClienteModel> getRowMapper() { return clienteMapper; }

    @Override
    public Optional<ClienteModel> buscarPorId(Long id) {
        List<ClienteModel> result = jdbc.query(SQL_SELECT + " WHERE c.idcliente = ?", clienteMapper, id);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public List<ClienteModel> listarTodos() {
        return jdbc.query(SQL_SELECT + " WHERE c.statuscliente = 'Ativo' ORDER BY p.nome", clienteMapper);
    }

    @Override
    public List<ClienteModel> listarTodosIncluindoInativos() {
        return jdbc.query(SQL_SELECT + " ORDER BY p.nome", clienteMapper);
    }

    @Override
    public ClienteModel salvar(ClienteModel model) {
        LocalDate hoje = model.getDataCadastro() != null ? model.getDataCadastro() : LocalDate.now();
        model.setDataCadastro(hoje);

        KeyHolder pessoaKh = new GeneratedKeyHolder();
        jdbc.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO pessoa (nome, ddi1, ddd1, numerotelefone1, email, endereco, bairro, cidade, estado, cep, datacadastro) VALUES (?,?,?,?,?,?,?,?,?,?,?)",
                new String[]{"idpessoa"});
            String[] tel = parseTelefone(model.getTelefone());
            ps.setString(1, model.getNome());
            ps.setString(2, tel[0]);
            ps.setString(3, tel[1]);
            ps.setString(4, tel[2]);
            ps.setString(5, model.getEmail() != null ? model.getEmail() : "");
            ps.setString(6, model.getEndereco() != null ? model.getEndereco() : "");
            ps.setString(7, model.getBairro() != null ? model.getBairro() : "");
            ps.setString(8, model.getCidade() != null ? model.getCidade() : "");
            ps.setString(9, model.getEstado() != null ? model.getEstado() : "");
            Integer cep = parseCep(model.getCep());
            ps.setInt(10, cep != null ? cep : 0);
            ps.setDate(11, Date.valueOf(hoje));
            return ps;
        }, pessoaKh);
        Long idPessoa = pessoaKh.getKey().longValue();
        model.setIdPessoa(idPessoa);

        if (model.isPessoaFisica()) {
            KeyHolder pfKh = new GeneratedKeyHolder();
            jdbc.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO pessoafisica (cpf, rg, datanascimento, idpessoa) VALUES (?,?,?,?)",
                    new String[]{"idpessoafisica"});
                ps.setString(1, model.getCpf());
                ps.setString(2, model.getRg());
                ps.setDate(3, model.getDataNascimento() != null ? Date.valueOf(model.getDataNascimento()) : null);
                ps.setLong(4, idPessoa);
                return ps;
            }, pfKh);
            model.setIdPessoaFisica(pfKh.getKey().longValue());

            KeyHolder clienteKh = new GeneratedKeyHolder();
            jdbc.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO cliente (statuscliente, observacoes, idpessoafisica) VALUES (?,?,?)",
                    new String[]{"idcliente"});
                ps.setString(1, model.isAtivo() ? "Ativo" : "Inativo");
                ps.setString(2, model.getObservacoes());
                ps.setLong(3, model.getIdPessoaFisica());
                return ps;
            }, clienteKh);
            model.setId(clienteKh.getKey().longValue());

        } else if (model.isPessoaJuridica()) {
            KeyHolder pjKh = new GeneratedKeyHolder();
            jdbc.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO pessoajuridica (cnpj, inscricaoestadual, razaosocial, idpessoa) VALUES (?,?,?,?)",
                    new String[]{"idpessoajuridica"});
                ps.setString(1, model.getCnpj());
                ps.setString(2, model.getInscricaoEstadual());
                ps.setString(3, model.getRazaoSocial() != null ? model.getRazaoSocial() : model.getNome());
                ps.setLong(4, idPessoa);
                return ps;
            }, pjKh);
            model.setIdPessoaJuridica(pjKh.getKey().longValue());

            KeyHolder clienteKh = new GeneratedKeyHolder();
            jdbc.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO cliente (statuscliente, observacoes, idpessoajuridica) VALUES (?,?,?)",
                    new String[]{"idcliente"});
                ps.setString(1, model.isAtivo() ? "Ativo" : "Inativo");
                ps.setString(2, model.getObservacoes());
                ps.setLong(3, model.getIdPessoaJuridica());
                return ps;
            }, clienteKh);
            model.setId(clienteKh.getKey().longValue());
        }

        return model;
    }

    @Override
    public void atualizar(ClienteModel model) {
        // Busca IDs internos para o update
        ClienteModel atual = buscarPorId(model.getId()).orElse(null);
        if (atual == null) return;

        String[] tel = parseTelefone(model.getTelefone());
        Integer cep = parseCep(model.getCep());

        jdbc.update(
            "UPDATE pessoa SET nome=?, ddi1=?, ddd1=?, numerotelefone1=?, email=?, endereco=?, bairro=?, cidade=?, estado=?, cep=? WHERE idpessoa=?",
            model.getNome(), tel[0], tel[1], tel[2],
            model.getEmail(), model.getEndereco(), model.getBairro(),
            model.getCidade(), model.getEstado(),
            cep != null ? cep : null,
            atual.getIdPessoa());

        if ("PF".equals(atual.getTipo())) {
            jdbc.update(
                "UPDATE pessoafisica SET cpf=?, rg=?, datanascimento=? WHERE idpessoafisica=?",
                model.getCpf(), model.getRg(),
                model.getDataNascimento() != null ? Date.valueOf(model.getDataNascimento()) : null,
                atual.getIdPessoaFisica());
        } else if ("PJ".equals(atual.getTipo())) {
            jdbc.update(
                "UPDATE pessoajuridica SET cnpj=?, inscricaoestadual=?, razaosocial=? WHERE idpessoajuridica=?",
                model.getCnpj(), model.getInscricaoEstadual(), model.getRazaoSocial(),
                atual.getIdPessoaJuridica());
        }

        jdbc.update(
            "UPDATE cliente SET statuscliente=?, observacoes=? WHERE idcliente=?",
            model.isAtivo() ? "Ativo" : "Inativo",
            model.getObservacoes(),
            model.getId());
    }

    @Override
    public void deletar(Long id) {
        jdbc.update("UPDATE cliente SET statuscliente = 'Inativo' WHERE idcliente = ?", id);
    }

    @Override
    public void toggleStatus(Long id) {
        jdbc.update(
            "UPDATE cliente SET statuscliente = CASE WHEN statuscliente = 'Ativo' THEN 'Inativo' ELSE 'Ativo' END WHERE idcliente = ?",
            id);
    }

    @Override
    public List<ClienteModel> buscarPorNome(String nome) {
        return jdbc.query(
            SQL_SELECT + " WHERE p.nome ILIKE ? AND c.statuscliente = 'Ativo' ORDER BY p.nome",
            clienteMapper, "%" + nome + "%");
    }

    private String[] parseTelefone(String telefone) {
        if (telefone == null || telefone.isBlank()) {
            return new String[]{"55", null, null};
        }
        String digits = telefone.replaceAll("\\D", "");
        if (digits.isEmpty()) {
            return new String[]{"55", null, null};
        }
        String ddi = "55";
        String ddd;
        String numero;
        if (digits.length() >= 12 && digits.startsWith("55")) {
            ddi = digits.substring(0, 2);
            ddd = digits.substring(2, 4);
            numero = digits.substring(4);
        } else if (digits.length() >= 10) {
            ddd = digits.substring(0, 2);
            numero = digits.substring(2);
        } else {
            ddd = digits.substring(0, Math.min(2, digits.length()));
            numero = digits.length() > 2 ? digits.substring(2) : null;
        }
        return new String[]{ddi, ddd, numero};
    }

    private Integer parseCep(String cep) {
        if (cep == null || cep.isBlank()) return null;
        String digits = cep.replaceAll("\\D", "");
        if (digits.isEmpty()) return null;
        return Integer.parseInt(digits);
    }
}
