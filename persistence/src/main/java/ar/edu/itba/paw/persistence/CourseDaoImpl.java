package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.CourseDao;
import ar.edu.itba.paw.models.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class CourseDaoImpl implements CourseDao {
    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private static  final RowMapper<Course> ROW_MAPPER = (rs,rowNum) -> new Course(rs.getLong("subjectId"),rs.getInt("year"),rs.getString("code"),rs.getInt("quarter"),rs.getString("board"),rs.getString("name"));

    @Autowired
    public CourseDaoImpl(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("courses");

    }

    @Override
    public boolean create(Course course) {
        final Map<String,Object> args = new HashMap<>();
        args.put("subjectId",course.getSubjectId());
        args.put("name",course.getName());
        args.put("code",course.getCode());
        args.put("quarter", course.getQuarter());
        args.put("board",course.getBoard());
        args.put("year",course.getYear());

        final Number rowsAffected = jdbcInsert.execute(args);

        return rowsAffected.intValue() >0;
    }

    @Override
    public boolean update(int id, Course course) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public List<Course> list() {
        // Only for testing, replace with proper db implementation
        return new ArrayList<>(jdbcTemplate.query("SELECT * FROM courses",ROW_MAPPER));
    }

    @Override
    public Optional<Course> getById(int id) {
        // Only for testing, replace with proper db implementation
        return jdbcTemplate.query("SELECT * FROM courses",ROW_MAPPER).stream().findFirst();
    }
}