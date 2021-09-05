package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.Teacher;
import javafx.util.Pair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
public class TeacherDaoImplTest {

    @Autowired
    private DataSource ds;

    @Autowired
    private TeacherDaoImpl teacherDao;

    private JdbcTemplate jdbcTemplate;
    private final int TEACHER_ID = 1;
    private final int INVALID_ID = 999;
    private final String sqlInsertTeacherId = String.format("INSERT INTO teachers VALUES (%d,'test_name','test_surname','test_email','test_username','test_password')", TEACHER_ID);

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "teachers");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "subjects");
    }


    @Test
    public void testCreate() {
        final Teacher teacher = teacherDao.create(new Teacher("name", "surname", "mail", "username", "password"));
        assertEquals("surname", teacher.getSurname());
        assertEquals("mail", teacher.getEmail());
        assertEquals( 1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "teachers"));
    }


    @Test
    public void testDelete() {
        jdbcTemplate.execute(sqlInsertTeacherId);
        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate,"teachers"));
        final boolean isDeleted = teacherDao.delete(TEACHER_ID);
        assertTrue(isDeleted);
        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, "teachers"));
    }


    @Test
    public void testDeleteNoExist() {
        jdbcTemplate.execute(sqlInsertTeacherId);
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "teachers"));
        assertFalse(teacherDao.delete(INVALID_ID));
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "teachers"));
    }

    @Test
    public void testGetById() {
        jdbcTemplate.execute(sqlInsertTeacherId);
        final Optional<Teacher> teacher = teacherDao.getById(TEACHER_ID);
        assertNotNull(teacher);
        assertTrue(teacher.isPresent());
        assertEquals("test_name", teacher.get().getName());
        assertEquals("test_surname", teacher.get().getSurname());
        assertEquals("test_email", teacher.get().getEmail());
        assertEquals("test_username", teacher.get().getUsername());
        assertEquals("test_password", teacher.get().getPassword());
    }

    @Test
    public void testGetByIdNoExist() {
        jdbcTemplate.execute(sqlInsertTeacherId);
        final Optional<Teacher> teacher = teacherDao.getById(INVALID_ID);

        assertNotNull(teacher);
        assertFalse(teacher.isPresent());
    }

    @Test
    public void testList() {
        jdbcTemplate.execute(sqlInsertTeacherId);
        final List<Teacher> list = teacherDao.list();
        assertNotNull(list);
        assertEquals(1, list.size());

    }

    @Test
    public void testEmptyList() {
        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, "teachers"));
        final List<Teacher> list = teacherDao.list();

        assertNotNull(list);
        assertEquals(0, list.size());

    }

    @Test
    public void testUpdate() {
        jdbcTemplate.execute(sqlInsertTeacherId);
        final boolean isUpdated = teacherDao.update(TEACHER_ID,new Teacher("test_update_name","test_update_surname","test_update_email","test_update_username","test_update_password"));
        assertTrue(isUpdated);
        final Optional<Teacher> teacher = teacherDao.getById(TEACHER_ID);
        assertNotNull(teacher);
        assertTrue(teacher.isPresent());
        assertEquals("test_update_name", teacher.get().getName());
        assertEquals("test_update_surname", teacher.get().getSurname());
        assertEquals("test_update_email", teacher.get().getEmail());
        assertEquals("test_update_username", teacher.get().getUsername());
        assertEquals("test_update_password", teacher.get().getPassword());
    }

    @Test
    public void testGetTeacherCourses() {
        int courseId, quarter, year,subjectId;
        courseId = 10;
        quarter = 1;
        year = 2021;
        subjectId=10;

        String insertSubjectWithIdSql = String.format("INSERT INTO subjects  VALUES (%d,'test_code','test_name')", subjectId);
        String insertCourseWithIdSql = String.format("INSERT INTO courses  VALUES (%d,%d,%d,'test_board',%d)", courseId,subjectId, quarter, year);
        String sqlInsertTeacher1Rol = String.format("INSERT INTO coursesroles VALUES (%d,%d,'rol');", TEACHER_ID, courseId);
        jdbcTemplate.execute(insertSubjectWithIdSql);
        jdbcTemplate.execute(insertCourseWithIdSql);
        jdbcTemplate.execute(sqlInsertTeacherId);
        jdbcTemplate.execute(sqlInsertTeacher1Rol);
        List<Pair<Course,String>> list = teacherDao.getTeacherCourses(TEACHER_ID);
        Course c = list.get(0).getKey();

        assertEquals(courseId, c.getCourseId());
        assertEquals(1,list.size());


    }

}