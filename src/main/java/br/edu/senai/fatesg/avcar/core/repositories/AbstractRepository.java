package br.edu.senai.fatesg.avcar.core.repositories;

import br.edu.senai.fatesg.avcar.core.domains.BaseModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Optional;

public abstract class AbstractRepository<T extends BaseModel> implements Repository<T> {

    protected final JdbcTemplate jdbc;

    public AbstractRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    protected abstract String getTableName();
    protected abstract RowMapper<T> getRowMapper();

    protected String getSelectSql() {
        return "SELECT * FROM " + getTableName();
    }

    @Override
    public Optional<T> buscarPorId(Long id) {
        List<T> result = jdbc.query(getSelectSql() + " WHERE id = ?", getRowMapper(), id);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public List<T> listarTodos() {
        return jdbc.query(getSelectSql() + " WHERE ativo = true ORDER BY id", getRowMapper());
    }

    @Override
    public List<T> listarTodosIncluindoInativos() {
        return jdbc.query(getSelectSql() + " ORDER BY id", getRowMapper());
    }

    @Override
    public void deletar(Long id) {
        jdbc.update("UPDATE " + getTableName() + " SET ativo = false WHERE id = ?", id);
    }

    @Override
    public void toggleStatus(Long id) {
        jdbc.update("UPDATE " + getTableName() + " SET ativo = NOT ativo WHERE id = ?", id);
    }
}
