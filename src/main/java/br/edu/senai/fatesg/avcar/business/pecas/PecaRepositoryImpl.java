package br.edu.senai.fatesg.avcar.business.pecas;

import br.edu.senai.fatesg.avcar.core.repositories.AbstractRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository("businessPecaRepository")
public class PecaRepositoryImpl extends AbstractRepository<PecaModel> implements IPecaRepository {

    private final String SQL_JOIN = """
        SELECT p.*, fp.idfornecedor as fornecedor_id, f.razaosocial as fornecedor_nome
        FROM pecas p
        LEFT JOIN fornecedor_pecas fp ON p.idpecas = fp.idpecas
        LEFT JOIN fornecedor f ON fp.idfornecedor = f.idfornecedor
        """;

    private final RowMapper<PecaModel> mapper = (rs, rowNum) -> {
        PecaModel m = new PecaModel();
        m.setId(rs.getLong("idpecas"));
        m.setCodigoNacional(rs.getLong("codigonacional"));
        m.setCodigoInterno(rs.getString("codigointernopeca"));
        m.setNome(rs.getString("nomepeca"));
        m.setDescricao(rs.getString("descricaopeca"));
        m.setFabricante(rs.getString("fabricantepeca"));
        m.setCategoria(rs.getString("categoriapeca"));
        m.setPrecoCusto(rs.getDouble("valorcustopeca"));
        m.setPrecoVenda(rs.getDouble("valorvendapeca"));
        m.setQuantidadeEstoque(rs.getInt("quantidadeestoque"));
        m.setGarantiaPeca(rs.getInt("garantiapeca"));
        LocalDate dc = rs.getObject("datacomprapeca", LocalDate.class);
        if (dc != null) m.setDataCompraPeca(dc);
        long fornecedorId = rs.getLong("fornecedor_id");
        if (!rs.wasNull()) {
            m.setFornecedorId(fornecedorId);
            m.setFornecedorNome(rs.getString("fornecedor_nome"));
        }
        m.setAtivo(rs.getBoolean("ativo"));
        return m;
    };

    public PecaRepositoryImpl(JdbcTemplate jdbc) {
        super(jdbc);
    }

    @Override
    protected String getTableName() { return "pecas"; }

    @Override
    protected RowMapper<PecaModel> getRowMapper() { return mapper; }

    @Override
    public Optional<PecaModel> buscarPorId(Long id) {
        List<PecaModel> result = jdbc.query(SQL_JOIN + " WHERE p.idpecas = ?", mapper, id);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public List<PecaModel> listarTodos() {
        return jdbc.query(SQL_JOIN + " WHERE p.ativo = true ORDER BY p.nomepeca", mapper);
    }

    @Override
    public List<PecaModel> listarTodosIncluindoInativos() {
        return jdbc.query(SQL_JOIN + " ORDER BY p.nomepeca", mapper);
    }

    @Override
    public PecaModel salvar(PecaModel model) {
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            var ps = connection.prepareStatement("""
                INSERT INTO pecas (codigonacional, codigointernopeca, nomepeca, descricaopeca,
                                  fabricantepeca, categoriapeca, valorcustopeca, valorvendapeca,
                                  quantidadeestoque, datacomprapeca, garantiapeca)
                VALUES (?,?,?,?,?,?,?,?,?,?,?)
                """, new String[]{"idpecas"});
            ps.setLong(1, model.getCodigoNacional());
            ps.setString(2, model.getCodigoInterno());
            ps.setString(3, model.getNome());
            ps.setString(4, model.getDescricao());
            ps.setString(5, model.getFabricante());
            ps.setString(6, model.getCategoria());
            ps.setDouble(7, model.getPrecoCusto());
            ps.setDouble(8, model.getPrecoVenda());
            ps.setInt(9, model.getQuantidadeEstoque());
            if (model.getDataCompraPeca() != null) ps.setObject(10, model.getDataCompraPeca());
            else ps.setNull(10, java.sql.Types.DATE);
            ps.setInt(11, model.getGarantiaPeca());
            return ps;
        }, kh);
        model.setId(kh.getKey().longValue());
        if (model.getFornecedorId() != null) {
            jdbc.update("INSERT INTO fornecedor_pecas (idfornecedor, idpecas) VALUES (?, ?)",
                model.getFornecedorId(), model.getId());
        }
        return model;
    }

    @Override
    public void atualizar(PecaModel model) {
        jdbc.update("""
            UPDATE pecas SET codigonacional=?, codigointernopeca=?, nomepeca=?, descricaopeca=?,
                            fabricantepeca=?, categoriapeca=?, valorcustopeca=?, valorvendapeca=?,
                            quantidadeestoque=?, datacomprapeca=?, garantiapeca=?
            WHERE idpecas=?
            """, model.getCodigoNacional(), model.getCodigoInterno(), model.getNome(),
            model.getDescricao(), model.getFabricante(), model.getCategoria(),
            model.getPrecoCusto(), model.getPrecoVenda(), model.getQuantidadeEstoque(),
            model.getDataCompraPeca(), model.getGarantiaPeca(), model.getId());
        jdbc.update("DELETE FROM fornecedor_pecas WHERE idpecas = ?", model.getId());
        if (model.getFornecedorId() != null) {
            jdbc.update("INSERT INTO fornecedor_pecas (idfornecedor, idpecas) VALUES (?, ?)",
                model.getFornecedorId(), model.getId());
        }
    }

    @Override
    public void deletar(Long id) {
        jdbc.update("DELETE FROM fornecedor_pecas WHERE idpecas = ?", id);
        jdbc.update("DELETE FROM pecas WHERE idpecas = ?", id);
    }

    @Override
    public void toggleStatus(Long id) {
        jdbc.update("UPDATE pecas SET ativo = NOT ativo WHERE idpecas = ?", id);
    }

    @Override
    public List<PecaModel> buscarPorCodigoNacional(long codigo) {
        return jdbc.query(SQL_JOIN + " WHERE p.codigonacional = ? ORDER BY p.nomepeca", mapper, codigo);
    }

    @Override
    public List<PecaModel> buscarPorFornecedor(Long fornecedorId) {
        return jdbc.query(SQL_JOIN + " WHERE fp.idfornecedor = ? ORDER BY p.nomepeca", mapper, fornecedorId);
    }

    @Override
    public List<PecaModel> buscarEstoqueBaixo(int quantidadeMinima) {
        return jdbc.query(SQL_JOIN + " WHERE p.quantidadeestoque <= ? ORDER BY p.quantidadeestoque", mapper, quantidadeMinima);
    }
}
