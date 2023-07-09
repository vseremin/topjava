package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        JdbcValidator.valid(user);

        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else {
            if (namedParameterJdbcTemplate.update("""
                       UPDATE users SET name=:name, email=:email, password=:password, 
                       registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                    """, parameterSource) == 0) {
                return null;
            }
            namedParameterJdbcTemplate.update("DELETE FROM user_role WHERE user_id =:id", parameterSource);
        }
        if (!user.getRoles().isEmpty()) {
            List<Object[]> args = new ArrayList<>();
            for (Role role : user.getRoles()) {
                args.add(new Object[]{user.getId(), role.name()});
            }
            jdbcTemplate.batchUpdate("INSERT INTO user_role (user_id, role) VALUES (?, ?)", args);
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        return addRoles(DataAccessUtils.singleResult(
                jdbcTemplate.query("SELECT * FROM users WHERE id = ?", ROW_MAPPER, id)));
    }

    @Override
    public User getByEmail(@Valid String email) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        return addRoles(DataAccessUtils.singleResult(users));
    }

    @Override
    public List<User> getAll() {
        Map<Integer, List<Role>> roles = new HashMap<>();
        jdbcTemplate.query("SELECT * FROM user_role", rs -> {
            while (rs.next()) {
                Role role = Role.valueOf(rs.getString("role"));
                Integer key = rs.getInt("user_id");
                roles.computeIfAbsent(key, k -> {
                    List<Role> r = new ArrayList<>();
                            r.add(role);
                    return r;
                });
                roles.computeIfPresent(key, (k, v) -> {
                   if (!v.contains(role))
                       v.add(role);
                        return v;
                });
            }
            return null;
        });
        List<User> users = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
        for (User user : users) {
            user.setRoles(roles.get(user.getId()));
        }
        return users;
    }

    private User addRoles(User user) {
        if (user == null) return null;
        List<Role> roles = new ArrayList<>();
        jdbcTemplate.query("SELECT * FROM user_role WHERE user_id = ?", new Object[]{user.getId()}, rs -> {
            do {
                roles.add(Role.valueOf(rs.getString("role")));
            } while (rs.next());
        });
        user.setRoles(roles);
        return user;
    }
}
