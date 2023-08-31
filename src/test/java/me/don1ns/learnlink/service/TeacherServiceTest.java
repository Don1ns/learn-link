package me.don1ns.learnlink.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import me.don1ns.learnlink.dao.TeacherDAO;
import me.don1ns.learnlink.dto.TeacherDTO;
import me.don1ns.learnlink.mapper.TeacherMapper;
import me.don1ns.learnlink.model.Course;
import me.don1ns.learnlink.model.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class TeacherServiceTest {
    private TeacherDAO teacherDAO;
    private TeacherMapper teacherMapper;
    private TeacherService teacherService;

    @BeforeEach
    public void setUp() {
        teacherDAO = mock(TeacherDAO.class);
        teacherMapper = mock(TeacherMapper.class);
        teacherService = new TeacherService(teacherDAO, teacherMapper);
    }

    @Test
    void testHandleGetRequest() throws SQLException, JsonProcessingException {
        Teacher teacher = new Teacher();
        teacher.setFullName("Arsen Molotov");
        teacher.setFaculty("Economics");

        Set<Course> courses = new HashSet<>();
        Course course1 = new Course();
        course1.setId(1L);
        courses.add(course1);

        Course course2 = new Course();
        course2.setId(2L);
        courses.add(course2);

        teacher.setCourses(courses);

        Teacher teacher1 = new Teacher();
        teacher1.setFullName("Ivan Ivanov");
        teacher1.setFaculty("Biology");

        Set<Course> courses1 = new HashSet<>();
        Course course3 = new Course();
        course3.setId(3L);
        courses1.add(course3);

        Course course4 = new Course();
        course4.setId(4L);
        courses1.add(course4);

        teacher.setCourses(courses1);

        List<Teacher> students = new ArrayList<>();
        students.add(teacher);
        students.add(teacher1);

        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setId(1L);
        teacherDTO.setFullName("Arsen Molotov");
        teacherDTO.setFaculty("Economics");
        teacherDTO.setCoursesId(Set.of(1L, 2L));

        TeacherDTO teacherDTO1 = new TeacherDTO();
        teacherDTO1.setId(1L);
        teacherDTO1.setFullName("Ivan Ivanov");
        teacherDTO1.setFaculty("Biology");
        teacherDTO1.setCoursesId(Set.of(1L, 2L));

        when(teacherDAO.getById(1L)).thenReturn(Optional.of(teacher));
        when(teacherMapper.toTeacherDTO(teacher)).thenReturn(teacherDTO);
        when(teacherMapper.toTeacherDTO(teacher1)).thenReturn(teacherDTO1);
        when(teacherDAO.getAll()).thenReturn(students);

        teacherService.handleGetRequest("/").get();
        teacherService.handleGetRequest("/1").get();

        verify(teacherMapper, times(2)).toTeacherDTO(teacher);
        verify(teacherMapper, times(1)).toTeacherDTO(teacher1);
        verify(teacherDAO, times(2)).getById(1L);
        verify(teacherDAO, times(1)).getAll();
    }

    @Test
    void TestHandlePostRequest() throws JsonProcessingException, SQLException {
        String bodyParams = "{\"fullName\": \"Ivan Ivanov\", \"faculty\": \"Biology\", \"coursesId\": [1, 2]}";

        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setFullName("Ivan Ivanov");
        teacherDTO.setFaculty("Biology");
        teacherDTO.setCoursesId(Set.of(1L, 2L));

        Teacher teacher = new Teacher();
        teacher.setFullName("Ivan Ivanov");
        teacher.setFaculty("Biology");

        Set<Course> courses = new HashSet<>();
        Course course = new Course();
        course.setId(1L);
        courses.add(course);

        Course course1 = new Course();
        course1.setId(2L);
        courses.add(course1);

        teacher.setCourses(courses);

        when(teacherMapper.toTeacher(teacherDTO)).thenReturn(teacher);

        teacherService.handlePostRequest(bodyParams);

        verify(teacherDAO, times(1)).create(teacher);
    }

    @Test
    void TestHandlePutRequest() throws SQLException, JsonProcessingException {
        String bodyParams = "{\"fullName\": \"Ivan Ivanov\", \"faculty\": \"Biology\", \"coursesId\": [1, 2]}";

        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setId(1L);
        teacherDTO.setFullName("Ivan Ivanov");
        teacherDTO.setFaculty("Biology");
        teacherDTO.setCoursesId(Set.of(1L, 2L));

        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFullName("Ivan Ivanov");
        teacher.setFaculty("Biology");

        Set<Course> courses = new HashSet<>();
        Course course = new Course();
        course.setId(1L);
        courses.add(course);

        Course course1 = new Course();
        course1.setId(2L);
        courses.add(course1);

        teacher.setCourses(courses);

        when(teacherMapper.toTeacher(teacherDTO)).thenReturn(teacher);

        teacherService.handlePutRequest("/1", bodyParams);

        verify(teacherDAO, times(1)).update(teacher);
    }

    @Test
    void testHandleDeleteRequest() throws SQLException {
        teacherService.handleDeleteRequest("/1");
        verify(teacherDAO, times(1)).deleteById(1L);
    }

}