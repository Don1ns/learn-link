package me.don1ns.learnlink.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import me.don1ns.learnlink.dao.StudentDAO;
import me.don1ns.learnlink.dto.StudentDTO;
import me.don1ns.learnlink.mapper.StudentMapper;
import me.don1ns.learnlink.model.Course;
import me.don1ns.learnlink.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class StudentServiceTest {
    private StudentDAO studentDAO;
    private StudentMapper studentMapper;
    private StudentService studentService;

    @BeforeEach
    public void setUp() {
        studentDAO = mock(StudentDAO.class);
        studentMapper = mock(StudentMapper.class);
        studentService = new StudentService(studentDAO, studentMapper);
    }

    @Test
    void testHandleGetRequest() throws SQLException, JsonProcessingException {
        Student student = new Student();
        student.setId(1L);
        student.setFullName("Ivan Ivanov");

        Set<Course> courses = new HashSet<>();
        Course course1 = new Course();
        course1.setId(1L);
        courses.add(course1);

        Course course2 = new Course();
        course2.setId(2L);
        courses.add(course2);

        student.setCourses(courses);

        Student student1 = new Student();
        student1.setId(2L);
        student1.setFullName("Andrey Ivanov");

        Set<Course> courses1 = new HashSet<>();
        Course course3 = new Course();
        course3.setId(3L);
        courses.add(course3);

        Course course4 = new Course();
        course4.setId(4L);
        courses.add(course4);

        student1.setCourses(courses);

        List<Student> students = new ArrayList<>();
        students.add(student);
        students.add(student1);

        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(1L);
        studentDTO.setFullName("Ivan Ivanov");
        studentDTO.setCoursesId(Set.of(1L, 2L));

        StudentDTO studentDTO1 = new StudentDTO();
        studentDTO1.setId(2L);
        studentDTO1.setFullName("Ivan Ivanov");
        studentDTO1.setCoursesId(Set.of(3L, 4L));

        when(studentDAO.getById(1L)).thenReturn(Optional.of(student));
        when(studentMapper.toStudentDTO(student)).thenReturn(studentDTO);
        when(studentMapper.toStudentDTO(student1)).thenReturn(studentDTO1);
        when(studentDAO.getAll()).thenReturn(students);

        studentService.handleGetRequest("/").get();
        studentService.handleGetRequest("/1").get();

        verify(studentMapper, times(2)).toStudentDTO(student);
        verify(studentMapper, times(1)).toStudentDTO(student1);
        verify(studentDAO, times(2)).getById(1L);
        verify(studentDAO, times(1)).getAll();
    }

    @Test
    void TestHandlePostRequest() throws JsonProcessingException, SQLException {
        String bodyParams = "{\"fullName\": \"Ivan Ivanov\", \"coursesId\": [1, 2]}";

        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setFullName("Ivan Ivanov");
        studentDTO.setCoursesId(Set.of(1L, 2L));

        Student student = new Student();
        student.setId(1L);
        student.setFullName("Ivan Ivanov");

        Set<Course> courses = new HashSet<>();
        Course course1 = new Course();
        course1.setId(1L);
        courses.add(course1);

        Course course2 = new Course();
        course2.setId(2L);
        courses.add(course2);

        student.setCourses(courses);

        when(studentMapper.toStudent(studentDTO)).thenReturn(student);

        studentService.handlePostRequest(bodyParams);

        verify(studentDAO, times(1)).create(student);
    }

    @Test
    void TestHandlePutRequest() throws SQLException, JsonProcessingException {
        String bodyParams = "{\"fullName\": \"Ivan Ivanov\", \"coursesId\": [1, 2]}";

        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(1L);
        studentDTO.setFullName("Ivan Ivanov");
        studentDTO.setCoursesId(Set.of(1L, 2L));

        Student student = new Student();
        student.setId(1L);
        student.setFullName("Ivan Ivanov");

        Set<Course> courses = new HashSet<>();
        Course course1 = new Course();
        course1.setId(1L);
        courses.add(course1);

        Course course2 = new Course();
        course2.setId(2L);
        courses.add(course2);

        student.setCourses(courses);

        when(studentMapper.toStudent(studentDTO)).thenReturn(student);

        studentService.handlePutRequest("/1", bodyParams);

        verify(studentDAO, times(1)).update(student);
    }

    @Test
    void testHandleDeleteRequest() throws SQLException {
        studentService.handleDeleteRequest("/1");
        verify(studentDAO, times(1)).deleteById(1L);
    }
}