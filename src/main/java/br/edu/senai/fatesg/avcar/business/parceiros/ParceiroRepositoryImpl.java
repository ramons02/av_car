package br.edu.senai.fatesg.avcar.business.parceiros;

import br.edu.senai.fatesg.avcar.core.repositories.AbstractRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("businessParceiroRepository")
public class ParceiroRepositoryImpl extends AbstractRepository<ParceiroModel> implements IParceiroRepository {

    private final RowMapper<ParceiroModel> mapper = (rs, rowNum) -> {
        ParceiroModel m = new ParceiroModel();
        m.setId(rs.getLong("id"));
        m.setNome(rs.getString("nome"));
        m.setCnpj(rs.getString("cnpj"));
        m.setTipoServico(rs.getString("tipo_servico"));
        m.setTelefone(rs.getString("telefone"));
        m.setEmail(rs.getString("email"));
        m.setAtivo(rs.getBoolean("ativo"));
        return m;
    };

    public ParceiroRepositoryImpl(JdbcTemplate jdbc) {
        super(jdbc);
    }

    @Override
    protected String getTableName() { return "parceiro_externo"; }

    @Override
    protected RowMapper<ParceiroModel> getRowMapper() { return mapper; }

    @Override
    public Optional<ParceiroModel> buscarPorId(Long id) {
        List<ParceiroModel> result = jdbc.query(getSelectSql() + " WHERE id = ?", mapper, id);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public List<ParceiroModel> listarTodos() {
        return jdbc.query(getSelectSql() + " WHERE ativo = true ORDER BY nome", mapper);
    }

    @Override
    public List<ParceiroModel> listarTodosIncluindoInativos() {
        return jdbc.query(getSelectSql() + " ORDER BY nome", mapper);
    }

    @Override
    public ParceiroModel salvar(ParceiroModel model) {
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            var ps = connection.prepareStatement(
                "INSERT INTO parceiro_externo (nome, cnpj, tipo_servico, telefone, email, ativo) VALUES (?,?,?,?,?,?)",
                new String[]{"id"});
            ps.setString(1, model.getNome());
            ps.setString(2, model.getCnpj());
            ps.setString(3, model.getTipoServico());
            ps.setString(4, model.getTelefone());
            ps.setString(5, model.getEmail());
            ps.setBoolean(6, model.isAtivo());
            return ps;
        }, kh);
        model.setId(kh.getKey().longValue());
        return model;
    }

    @Override
    public void atualizar(ParceiroModel model) {
        jdbc.update(
            "UPDATE parceiro_externo SET nome=?, cnpj=?, tipo_servico=?, telefone=?, email=?, ativo=? WHERE id=?",
            model.getNome(), model.getCnpj(), model.getTipoServico(), model.getTelefone(),
            model.getEmail(), model.isAtivo(), model.getId());
    }

    @Override
    public void deletar(Long id) {
        jdbc.update("UPDATE parceiro_externo SET ativo = false WHERE id = ?", id);
    }

    @Override
    public void toggleStatus(Long id) {
        jdbc.update("UPDATE parceiro_externo SET ativo = NOT ativo WHERE id = ?", id);
    }

    @Override
    public List<ParceiroModel> buscarPorNome(String nome) {
        return jdbc.query(getSelectSql() + " WHERE nome ILIKE ? AND ativo = true ORDER BY nome",
            mapper, "%" + nome + "%");
    }
}
