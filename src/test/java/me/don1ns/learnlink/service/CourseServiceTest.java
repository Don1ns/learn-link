package me.don1ns.learnlink.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import me.don1ns.learnlink.dao.CourseDAO;
import me.don1ns.learnlink.dto.CourseDTO;
import me.don1ns.learnlink.mapper.CourseMapper;
import me.don1ns.learnlink.model.Course;
import me.don1ns.learnlink.model.Student;
import me.don1ns.learnlink.model.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.*;

import static org.mockito.Mockito.*;

class CourseServiceTest {
    private CourseDAO courseDAO;
    private CourseMapper courseMapper;
    private CourseService courseService;

    @BeforeEach
    public void setUp() {
        courseDAO = mock(CourseDAO.class);
        courseMapper = mock(CourseMapper.class);
        courseService = new CourseService(courseDAO, courseMapper);
    }

    @Test
    void testHandleGetRequest() throws SQLException, JsonProcessingException {
        Course course = new Course();
        course.setId(1L);
        course.setTitle("Test Course");

        Teacher teacher = new Teacher();
        teacher.setId(1L);
        course.setTeacher(teacher);

        Student student1 = new Student();
        student1.setId(1L);
        Student student2 = new Student();
        student2.setId(2L);
        Set<Student> students = new HashSet<>();
        students.add(student1);
        students.add(student2);
        course.setStudents(students);

        Course course1 = new Course();
        course1.setId(2L);
        course1.setTitle("Test Course 1");

        Teacher teacher1 = new Teacher();
        teacher1.setId(2L);
        course1.setTeacher(teacher1);

        Student student3 = new Student();
        student3.setId(3L);
        Student student4 = new Student();
        student4.setId(4L);
        Set<Student> students1 = new HashSet<>();
        students1.add(student3);
        students1.add(student4);
        course1.setStudents(students1);

        List<Course> courses = new ArrayList<>();
        courses.add(course);
        courses.add(course1);

        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setId(1L);
        courseDTO.setTitle("Test Course");
        courseDTO.setTeacherId(1L);
        courseDTO.setStudentsId(Set.of(1L, 2L));

        CourseDTO courseDTO1 = new CourseDTO();
        courseDTO1.setId(2L);
        courseDTO1.setTitle("Test Course");
        courseDTO1.setTeacherId(2L);
        courseDTO1.setStudentsId(Set.of(3L, 4L));

        when(courseDAO.getById(1L)).thenReturn(Optional.of(course));
        when(courseMapper.toCourseDTO(course)).thenReturn(courseDTO);
        when(courseMapper.toCourseDTO(course1)).thenReturn(courseDTO1);
        when(courseDAO.getAll()).thenReturn(courses);

        courseService.handleGetRequest("/").get();
        courseService.handleGetRequest("/1").get();

        verify(courseMapper, times(2)).toCourseDTO(course);
        verify(courseMapper, times(1)).toCourseDTO(course1);
        verify(courseDAO, times(2)).getById(1L);
        verify(courseDAO, times(1)).getAll();
    }

    @Test
    void TestHandlePostRequest() throws JsonProcessingException, SQLException {
        String bodyParams = "{\"title\": \"Management\", \"studentsId\": [1, 2], \"teacherId\": 1}";

        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setTitle("Management");
        courseDTO.setTeacherId(1L);
        courseDTO.setStudentsId(Set.of(1L, 2L));

        Course course = new Course();
        course.setId(1L);
        course.setTitle("Managment");

        Teacher teacher = new Teacher();
        teacher.setId(1L);
        course.setTeacher(teacher);

        Student student1 = new Student();
        student1.setId(1L);
        Student student2 = new Student();
        student2.setId(2L);
        Set<Student> students = new HashSet<>();
        students.add(student1);
        students.add(student2);
        course.setStudents(students);

        when(courseMapper.toCourse(courseDTO)).thenReturn(course);

        courseService.handlePostRequest(bodyParams);

        verify(courseDAO, times(1)).create(course);
    }

    @Test
    void TestHandlePutRequest() throws SQLException, JsonProcessingException {
        String bodyParams = "{\"title\": \"Management\", \"studentsId\": [1, 2], \"teacherId\": 1}";

        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setId(1L);
        courseDTO.setTitle("Management");
        courseDTO.setTeacherId(1L);
        courseDTO.setStudentsId(Set.of(1L, 2L));

        Course course = new Course();
        course.setId(1L);
        course.setTitle("Managment");

        Teacher teacher = new Teacher();
        teacher.setId(1L);
        course.setTeacher(teacher);

        Student student1 = new Student();
        student1.setId(1L);
        Student student2 = new Student();
        student2.setId(2L);
        Set<Student> students = new HashSet<>();
        students.add(student1);
        students.add(student2);
        course.setStudents(students);

        when(courseMapper.toCourse(courseDTO)).thenReturn(course);

        courseService.handlePutRequest("/1", bodyParams);

        verify(courseDAO, times(1)).update(course);
    }

    @Test
    void testHandleDeleteRequest() throws SQLException {
        courseService.handleDeleteRequest("/1");
        verify(courseDAO, times(1)).deleteById(1L);
    }
}