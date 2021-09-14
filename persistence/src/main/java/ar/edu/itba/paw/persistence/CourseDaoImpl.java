package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.CourseDao;
import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class CourseDaoImpl implements CourseDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private static final RowMapper<Course> COURSE_ROW_MAPPER = (rs, rowNum) ->
        new Course.Builder()
            .withCourseId(rs.getInt("courseId"))
            .withYear(rs.getInt("year"))
            .withQuarter(rs.getInt("quarter"))
            .withBoard(rs.getString("board"))
            .withSubject(new Subject(rs.getInt("subjectId"), rs.getString("code"),
                    rs.getString("subjectName")))
            .build();
    private enum ROLES { STUDENT(1), HELPER(2), TEACHER(3);
        private final int id;
        ROLES(int id) {this.id = id;}
        public int getValue() { return id; }
    };

    @Autowired
    public CourseDaoImpl(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("courses").usingGeneratedKeyColumns("courseid");
    }

    @Override
    public Course create(Integer year, Integer quarter, String board, Integer subjectId, String subjectName,
                         String subjectCode) {
        final Map<String, Object> args = new HashMap<>();
        args.put("quarter", quarter);
        args.put("board", board);
        args.put("year", year);
        args.put("subjectId", subjectId);
        final int courseId = jdbcInsert.executeAndReturnKey(args).intValue();
        return new Course.Builder()
                .withCourseId(courseId)
                .withYear(year)
                .withQuarter(quarter)
                .withBoard(board)
                .withSubject(new Subject(subjectId, subjectCode, subjectName))
                .build();
    }

    @Override
    public boolean update(Integer id, Course course) {
        return jdbcTemplate.update("UPDATE courses " +
                "SET subjectId = ?," +
                "year = ?," +
                "quarter = ?," +
                "board = ? " +
                "WHERE courseId = ?;", new Object[]{course.getSubject().getSubjectId(), course.getYear(), course.getQuarter(), course.getBoard(), id}) == 1;

    }

    @Override
    public boolean delete(Integer id) {
        return jdbcTemplate.update("DELETE FROM courses WHERE courseId = ?", new Object[]{id}) == 1;
    }

    @Override
    public List<Course> list() {
        return new ArrayList<>(jdbcTemplate.query("SELECT * FROM courses NATURAL JOIN subjects", COURSE_ROW_MAPPER));
    }

    @Override
    public Optional<Course> getById(Integer id) {
        return jdbcTemplate.query("SELECT * FROM courses NATURAL JOIN subjects WHERE courseId = ?", new Object[]{id}, COURSE_ROW_MAPPER).stream().findFirst();
    }

    private static final ResultSetExtractor<Map<User, Role>> MAP_RESULT_SET_EXTRACTOR = (rs -> {
        Map<User, Role> result = new HashMap<>();
        while(rs.next()) {
            User user = new User.Builder()
                    .withUserId(rs.getInt("userId"))
                    .withFileNumber(rs.getInt("fileNumber"))
                    .withName(rs.getString("name"))
                    .withSurname(rs.getString("surname"))
                    .withUsername(rs.getString("username"))
                    .withEmail(rs.getString("email"))
                    .withPassword(rs.getString("password"))
                    .isAdmin(rs.getBoolean("isAdmin"))
                    .build();
            Role role = new Role(rs.getInt("roleId"), rs.getString("roleName"));
            result.put(user, role);
        }
        return result;
    });

    @Override
    public Map<User, Role> getTeachers(Integer courseId) {
        return jdbcTemplate.query("SELECT * FROM users NATURAL JOIN user_to_course NATURAL JOIN roles WHERE " +
                "courseId = ? AND roleId BETWEEN ? AND ?", new Object[]{courseId, ROLES.HELPER.getValue(), ROLES.TEACHER.getValue()},
                MAP_RESULT_SET_EXTRACTOR);
    }

    private static final RowMapper<Integer> BELONGS_MAPPER = (rs, rowNum) -> rs.getInt("userId");

    @Override
    public boolean belongs(Integer userId, Integer courseId) {
        return jdbcTemplate.query("SELECT * FROM user_to_course WHERE courseId = ? AND userId = ?",
                new Object[]{courseId, userId}, BELONGS_MAPPER).stream().findFirst().isPresent();
    }

}