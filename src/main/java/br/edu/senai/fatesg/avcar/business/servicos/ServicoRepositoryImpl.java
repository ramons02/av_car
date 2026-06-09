package br.edu.senai.fatesg.avcar.business.servicos;

import br.edu.senai.fatesg.avcar.core.repositories.AbstractRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository("businessServicoRepository")
public class ServicoRepositoryImpl extends AbstractRepository<ServicoModel> implements IServicoRepository {

    private static final String SQL_JOIN = """
        SELECT s.idservico, s.nomeservico, s.descricaoservico, s.valorservico,
               s.tempoestimado, s.garantiadias, s.ativo
        FROM servico s
        """;

    private final RowMapper<ServicoModel> mapper = (rs, rowNum) -> {
        ServicoModel m = new ServicoModel();
        m.setId(rs.getLong("idservico"));
        m.setNomeServico(rs.getString("nomeservico"));
        m.setDescricaoServico(rs.getString("descricaoservico"));
        m.setValorServico(rs.getDouble("valorservico"));
        m.setGarantiaDias(rs.getInt("garantiadias"));
        m.setTempoEstimado(rs.getString("tempoestimado"));
        m.setAtivo(rs.getBoolean("ativo"));
        return m;
    };

    public ServicoRepositoryImpl(JdbcTemplate jdbc) {
        super(jdbc);
    }

    @Override
    protected String getTableName() { return "servico"; }

    @Override
    protected RowMapper<ServicoModel> getRowMapper() { return mapper; }

    @Override
    public Optional<ServicoModel> buscarPorId(Long id) {
        List<ServicoModel> result = jdbc.query(SQL_JOIN + " WHERE s.idservico = ?", mapper, id);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public List<ServicoModel> listarTodos() {
        return jdbc.query(SQL_JOIN + " WHERE s.ativo = true ORDER BY s.nomeservico", mapper);
    }

    @Override
    public List<ServicoModel> listarTodosIncluindoInativos() {
        return jdbc.query(SQL_JOIN + " ORDER BY s.nomeservico", mapper);
    }

    @Override
    public ServicoModel salvar(ServicoModel model) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO servico (nomeservico, descricaoservico, valorservico, garantiadias, tempoestimado) VALUES (?,?,?,?,?)",
                new String[]{"idservico"});
            ps.setString(1, model.getNomeServico());
            ps.setString(2, model.getDescricaoServico());
            ps.setDouble(3, model.getValorServico());
            ps.setInt(4, model.getGarantiaDias());
            ps.setString(5, model.getTempoEstimado());
            return ps;
        }, keyHolder);
        model.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return model;
    }

    @Override
    public void atualizar(ServicoModel model) {
        jdbc.update("""
            UPDATE servico SET nomeservico=?, descricaoservico=?, valorservico=?, garantiadias=?, tempoestimado=?
            WHERE idservico=?
            """, model.getNomeServico(), model.getDescricaoServico(), model.getValorServico(),
            model.getGarantiaDias(), model.getTempoEstimado(), model.getId());
    }

    @Override
    public void deletar(Long id) {
        jdbc.update("UPDATE servico SET ativo = false WHERE idservico = ?", id);
    }

    @Override
    public void toggleStatus(Long id) {
        jdbc.update("UPDATE servico SET ativo = NOT ativo WHERE idservico = ?", id);
    }

    @Override
    public List<ServicoModel> buscarPorNome(String nome) {
        return jdbc.query(SQL_JOIN + " WHERE s.nomeservico ILIKE ? ORDER BY s.nomeservico", mapper, "%" + nome + "%");
    }
}
