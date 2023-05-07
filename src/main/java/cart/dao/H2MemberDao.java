package cart.dao;

import cart.entity.MemberEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class H2MemberDao implements MemberDao {
    private static final RowMapper<MemberEntity> memberEntityRowMapper = (resultSet, rowNum) ->
            new MemberEntity(
                    resultSet.getLong("id"),
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    resultSet.getString("name"),
                    resultSet.getString("phone"),
                    resultSet.getTimestamp("created_at"),
                    resultSet.getTimestamp("updated_at")
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public H2MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<MemberEntity> save(MemberEntity member) {
        Map<String, Object> parameterSource = new HashMap<>();

        parameterSource.put("name", member.getName());
        parameterSource.put("email", member.getEmail());
        parameterSource.put("password", member.getPassword());
        parameterSource.put("phone", member.getPhone());
        parameterSource.put("created_at", Timestamp.valueOf(LocalDateTime.now()));

        long id = simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
        return findById(id);
    }

    @Override
    public MemberEntity update(MemberEntity entity) {
        String sql = "UPDATE member SET email = ?, password = ?, name = ?, phone = ?, updated_at = ? WHERE id = ?";
        jdbcTemplate.update(sql, entity.getEmail(), entity.getPassword(), entity.getName(), entity.getPhone(), entity.getUpdatedAt(), entity.getId());
        return entity;
    }

    @Override
    public Optional<MemberEntity> findById(Long id) {
        String sql = "SELECT * FROM member WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, memberEntityRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<MemberEntity> findAll() {
        String sql = "SELECT * FROM member";
        return jdbcTemplate.query(sql, memberEntityRowMapper);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM member WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<MemberEntity> findByEmail(String email) {
        String sql = "SELECT * FROM member WHERE email = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, memberEntityRowMapper, email));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
