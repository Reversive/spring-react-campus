package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.TimetableDao;
import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.Timetable;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:populators/timetable_populator.sql")
@Rollback
@Transactional
public class TimeTableDaoImplTest {

    private final Long COURSE_ID = 1L;
    private final Integer COURSE_YEAR = 2021;
    private final Integer COURSE_QUARTER = 1;
    private final String COURSE_BOARD = "S1";

    private final Long SUBJECT_ID = 1L;
    private final String SUBJECT_CODE = "A22";
    private final String SUBJECT_NAME = "Protos";

    protected final Integer TIME_TABLE_DAY_OF_WEEK = 1;
    protected final LocalTime TIME_TABLE_START_OF_COURSE = LocalTime.of(12, 0);
    protected final LocalTime TIME_TABLE_END_OF_COURSE = LocalTime.of(14, 0);

    @Autowired
    private TimetableDao timetableDao;

    private Course getMockCourse() {
        return new Course.Builder()
                .withCourseId(COURSE_ID)
                .withYear(COURSE_YEAR)
                .withQuarter(COURSE_QUARTER)
                .withBoard(COURSE_BOARD)
                .withSubject(new Subject(SUBJECT_ID, SUBJECT_CODE, SUBJECT_NAME))
                .build();
    }

    @Test
    public void testCreate() {
        final boolean timeTableEntryInsertion = timetableDao.create(getMockCourse(), TIME_TABLE_DAY_OF_WEEK, TIME_TABLE_START_OF_COURSE, TIME_TABLE_END_OF_COURSE);
        assertTrue(timeTableEntryInsertion);
    }

    @Test
    public void testUpdateNoExist() {
        LocalTime startChangedTo = LocalTime.of(16, 0);
        LocalTime durationChangedTo = LocalTime.of(3, 0);
        assertFalse(timetableDao.update(COURSE_ID + 1, TIME_TABLE_DAY_OF_WEEK, startChangedTo, durationChangedTo));
    }


    @Test
    public void testDeleteNoExist() {
        assertFalse(timetableDao.delete(COURSE_ID + 1));
    }


    @Test
    public void testFindByIdNoExist() {
        assertTrue(timetableDao.findById(COURSE_ID + 1).isEmpty());
    }

    @Test
    public void testUpdate(){
        assertTrue(timetableDao.update(COURSE_ID, TIME_TABLE_DAY_OF_WEEK, TIME_TABLE_START_OF_COURSE, LocalTime.of(16, 0)));
    }

    @Test()
    public void testDelete() {
        assertTrue(timetableDao.delete(COURSE_ID));
    }

    @Test
    public void testFindById(){
        List<Timetable> timetable = timetableDao.findById(COURSE_ID);

        assertFalse(timetable.isEmpty());
    }

}
