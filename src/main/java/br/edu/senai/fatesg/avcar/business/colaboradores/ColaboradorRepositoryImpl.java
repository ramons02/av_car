package br.edu.senai.fatesg.avcar.business.colaboradores;

import br.edu.senai.fatesg.avcar.core.repositories.AbstractRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Types;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository("businessColaboradorRepository")
public class ColaboradorRepositoryImpl extends AbstractRepository<ColaboradorModel> implements IColaboradorRepository {

    private static final String SELECT_SQL = """
        SELECT col.idcolaborador, col.matricula, col.cpf, col.dataadmissao, col.datademissao,
               col.observacoes, col.salario, col.idpessoa, col.ativo,
               p.nome, p.ddi1, p.ddd1, p.numerotelefone1, p.email
        FROM colaborador col
        JOIN pessoa p ON col.idpessoa = p.idpessoa
        """;

    private final RowMapper<ColaboradorModel> mapper = (rs, rowNum) -> {
        ColaboradorModel m = new ColaboradorModel();
        m.setId(rs.getLong("idcolaborador"));
        m.setMatricula(rs.getString("matricula"));
        m.setCpf(rs.getString("cpf"));
        m.setAtivo(rs.getBoolean("ativo"));
        m.setIdPessoa(rs.getLong("idpessoa"));
        m.setNome(rs.getString("nome"));
        m.setDdi1(rs.getString("ddi1"));
        m.setDdd1(rs.getString("ddd1"));
        m.setNumerotelefone1(rs.getString("numerotelefone1"));
        m.setEmail(rs.getString("email"));
        Date dataAdmissao = rs.getDate("dataadmissao");
        if (dataAdmissao != null) m.setDataAdmissao(dataAdmissao.toLocalDate());
        Date dataDemissao = rs.getDate("datademissao");
        if (dataDemissao != null) m.setDataDemissao(dataDemissao.toLocalDate());
        double salario = rs.getDouble("salario");
        if (!rs.wasNull()) m.setSalario(salario);
        m.setObservacoes(rs.getString("observacoes"));
        return m;
    };

    public ColaboradorRepositoryImpl(JdbcTemplate jdbc) {
        super(jdbc);
    }

    @Override
    protected String getTableName() { return "colaborador"; }

    @Override
    protected RowMapper<ColaboradorModel> getRowMapper() { return mapper; }

    private List<FuncaoDTO> buscarFuncoesPorColaborador(Long colaboradorId) {
        return jdbc.query("""
            SELECT f.idfuncao, f.especialidade, f.comissao, f.funcaocolaborador
            FROM funcao f
            JOIN colaborador_funcao cf ON cf.idfuncao = f.idfuncao
            WHERE cf.idcolaborador = ?
            ORDER BY f.funcaocolaborador
            """, (rs, i) -> new FuncaoDTO(
                rs.getLong("idfuncao"),
                rs.getString("especialidade"),
                rs.getDouble("comissao"),
                rs.getString("funcaocolaborador")
            ), colaboradorId);
    }

    @Override
    public Optional<ColaboradorModel> buscarPorId(Long id) {
        List<ColaboradorModel> result = jdbc.query(SELECT_SQL + " WHERE col.idcolaborador = ?", mapper, id);
        result.forEach(m -> {
            List<FuncaoDTO> funcoes = buscarFuncoesPorColaborador(m.getId());
            m.setFuncaoNomes(funcoes.stream().map(FuncaoDTO::getFuncaoColaborador).toList());
        });
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public List<ColaboradorModel> listarTodos() {
        List<ColaboradorModel> lista = jdbc.query(SELECT_SQL + " WHERE col.ativo = true ORDER BY p.nome", mapper);
        for (ColaboradorModel m : lista) {
            List<FuncaoDTO> funcoes = buscarFuncoesPorColaborador(m.getId());
            m.setFuncaoNomes(funcoes.stream().map(FuncaoDTO::getFuncaoColaborador).toList());
        }
        return lista;
    }

    @Override
    public List<ColaboradorModel> listarTodosIncluindoInativos() {
        List<ColaboradorModel> lista = jdbc.query(SELECT_SQL + " ORDER BY p.nome", mapper);
        for (ColaboradorModel m : lista) {
            List<FuncaoDTO> funcoes = buscarFuncoesPorColaborador(m.getId());
            m.setFuncaoNomes(funcoes.stream().map(FuncaoDTO::getFuncaoColaborador).toList());
        }
        return lista;
    }

    @Override
    public ColaboradorModel salvar(ColaboradorModel model) {
        KeyHolder pessoaKh = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            var ps = connection.prepareStatement("""
                INSERT INTO pessoa (nome, ddi1, ddd1, numerotelefone1, email, endereco, bairro, cidade, estado, cep)
                VALUES (?,?,?,?,?,?,?,?,?,?)
                """, new String[]{"idpessoa"});
            ps.setString(1, model.getNome());
            ps.setString(2, model.getDdi1() != null ? model.getDdi1() : "55");
            ps.setString(3, model.getDdd1() != null ? model.getDdd1() : "");
            ps.setString(4, model.getNumerotelefone1() != null ? model.getNumerotelefone1() : "");
            if (model.getEmail() != null && !model.getEmail().isEmpty()) {
                ps.setString(5, model.getEmail());
            } else {
                ps.setNull(5, Types.VARCHAR);
            }
            ps.setString(6, "");
            ps.setString(7, "");
            ps.setString(8, "");
            ps.setString(9, "");
            ps.setInt(10, 0);
            return ps;
        }, pessoaKh);
        Long idPessoa = pessoaKh.getKey().longValue();
        model.setIdPessoa(idPessoa);

        boolean gerarMatricula = model.getMatricula() == null || model.getMatricula().isBlank();
        KeyHolder kh = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            var ps = connection.prepareStatement("""
                INSERT INTO colaborador (matricula, cpf, dataadmissao, salario, observacoes, idpessoa)
                VALUES (?,?,?,?,?,?)
                """, new String[]{"idcolaborador"});
            ps.setString(1, gerarMatricula ? "TEMP_" + System.nanoTime() : model.getMatricula());
            ps.setString(2, model.getCpf());
            LocalDate dt = model.getDataAdmissao();
            if (dt != null) ps.setDate(3, Date.valueOf(dt));
            else ps.setNull(3, Types.DATE);
            Double sal = model.getSalario();
            if (sal != null) ps.setDouble(4, sal);
            else ps.setNull(4, Types.DECIMAL);
            String obs = model.getObservacoes();
            if (obs != null) ps.setString(5, obs);
            else ps.setNull(5, Types.VARCHAR);
            ps.setLong(6, idPessoa);
            return ps;
        }, kh);
        model.setId(kh.getKey().longValue());
        if (gerarMatricula) {
            model.setMatricula("COL" + model.getId());
            jdbc.update("UPDATE colaborador SET matricula = ? WHERE idcolaborador = ?",
                model.getMatricula(), model.getId());
        }
        return model;
    }

    @Override
    public void atualizar(ColaboradorModel model) {
        jdbc.update("""
            UPDATE pessoa SET nome=?, ddi1=?, ddd1=?, numerotelefone1=?, email=?
            WHERE idpessoa=?
            """,
            model.getNome(), model.getDdi1(), model.getDdd1(), model.getNumerotelefone1(), model.getEmail(),
            model.getIdPessoa());
        jdbc.update("""
            UPDATE colaborador SET matricula=?, cpf=?, dataadmissao=?, datademissao=?, salario=?, observacoes=?
            WHERE idcolaborador=?
            """,
            model.getMatricula(), model.getCpf(),
            model.getDataAdmissao() != null ? Date.valueOf(model.getDataAdmissao()) : null,
            model.getDataDemissao() != null ? Date.valueOf(model.getDataDemissao()) : null,
            model.getSalario(), model.getObservacoes(), model.getId());
    }

    @Override
    public void deletar(Long id) {
        jdbc.update("DELETE FROM colaborador WHERE idcolaborador = ?", id);
    }

    @Override
    public void toggleStatus(Long id) {
        jdbc.update("UPDATE colaborador SET ativo = NOT ativo WHERE idcolaborador = ?", id);
    }

    @Override
    public List<ColaboradorModel> buscarPorNome(String nome) {
        List<ColaboradorModel> lista = jdbc.query(
            SELECT_SQL + " WHERE p.nome ILIKE ? ORDER BY p.nome", mapper, "%" + nome + "%");
        for (ColaboradorModel m : lista) {
            List<FuncaoDTO> funcoes = buscarFuncoesPorColaborador(m.getId());
            m.setFuncaoNomes(funcoes.stream().map(FuncaoDTO::getFuncaoColaborador).toList());
        }
        return lista;
    }

    @Override
    public void salvarFuncoes(Long colaboradorId, List<Long> funcaoIds) {
        jdbc.update("DELETE FROM colaborador_funcao WHERE idcolaborador = ?", colaboradorId);
        if (funcaoIds != null) {
            for (Long fid : funcaoIds) {
                jdbc.update("INSERT INTO colaborador_funcao (idcolaborador, idfuncao) VALUES (?,?)",
                    colaboradorId, fid);
            }
        }
    }

    @Override
    public List<FuncaoDTO> listarFuncoes() {
        return jdbc.query(
            "SELECT idfuncao, especialidade, comissao, funcaocolaborador FROM funcao ORDER BY funcaocolaborador",
            (rs, i) -> new FuncaoDTO(
                rs.getLong("idfuncao"),
                rs.getString("especialidade"),
                rs.getDouble("comissao"),
                rs.getString("funcaocolaborador")
            ));
    }
}
