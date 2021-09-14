package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private static final RowMapper<User> USER_ROW_MAPPER = (rs, rowNum) ->
        new User.Builder()
            .withUserId(rs.getInt("userId"))
            .withFileNumber(rs.getInt("fileNumber"))
            .withName(rs.getString("name"))
            .withSurname(rs.getString("surname"))
            .withUsername(rs.getString("username"))
            .withEmail(rs.getString("email"))
            .withPassword(rs.getString("password"))
            .isAdmin(rs.getBoolean("isAdmin"))
            .build();

    private static final RowMapper<Role> ROLE_ROW_MAPPER = (rs, rowNum) -> {
        return new Role(rs.getInt("roleId"), rs.getString("roleName"));
    };

    @Autowired
    public UserDaoImpl(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("users").usingGeneratedKeyColumns("userid");
    }

    @Override
    public User create(Integer fileNumber, String name, String surname, String username, String email, String password,
                       boolean isAdmin) {
        final Map<String, Object> args = new HashMap<>();
        args.put("fileNumber", fileNumber);
        args.put("name",name);
        args.put("surname", surname);
        args.put("username", username);
        args.put("email", email);
        args.put("password", password);
        args.put("isAdmin", isAdmin);
        final int userId = jdbcInsert.executeAndReturnKey(args).intValue();
        return new User.Builder()
                .withUserId(userId)
                .withFileNumber(fileNumber)
                .withName(name)
                .withSurname(surname)
                .withUsername(username)
                .withEmail(email)
                .withPassword(password)
                .isAdmin(isAdmin)
                .build();
    }

    @Override
    public boolean update(int userId, User user) {
        return jdbcTemplate.update("UPDATE users " +
                "SET fileNumber = ?," +
                "name = ?," +
                "surname = ?," +
                "username = ?, " +
                "email = ?, " +
                "password = ? ," +
                "isAdmin = ? " +
                "WHERE userId = ?;", new Object[]{user.getFileNumber(), user.getName(), user.getSurname(),
                    user.getUsername(), user.getEmail(), user.getPassword(), user.isAdmin(), userId}) == 1;
    }

    @Override
    public boolean delete(int userId) {
        return jdbcTemplate.update("DELETE FROM users WHERE userId = ?", new Object[]{userId}) == 1;
    }

    @Override
    public Role getRole(int userId, int courseId) {
        return jdbcTemplate.query("SELECT * FROM user_to_course NATURAL JOIN roles WHERE userId = ? AND courseId = ?",
                new Object[]{userId, courseId}, ROLE_ROW_MAPPER).get(0);
    }

    @Override
    public Optional<User> findById(int userId) {
        return jdbcTemplate.query("SELECT * FROM users WHERE userId = ?",
                new Object[]{userId}, USER_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jdbcTemplate.query("SELECT * FROM users WHERE username = ?",
                new Object[]{username}, USER_ROW_MAPPER).stream().findFirst();
    }
}
