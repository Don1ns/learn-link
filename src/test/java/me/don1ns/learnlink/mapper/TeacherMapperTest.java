package me.don1ns.learnlink.mapper;

import me.don1ns.learnlink.dao.CourseDAO;
import me.don1ns.learnlink.dto.StudentDTO;
import me.don1ns.learnlink.dto.TeacherDTO;
import me.don1ns.learnlink.model.Course;
import me.don1ns.learnlink.model.Student;
import me.don1ns.learnlink.model.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TeacherMapperTest {
    private TeacherMapper teacherMapper;
    private CourseDAO courseDAO;

    @BeforeEach
    public void setUp() {
        courseDAO = mock(CourseDAO.class);
        teacherMapper = new TeacherMapper(courseDAO);
    }

    @Test
    void testToTeacherDTO() throws SQLException {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFullName("Ivan Ivanov");
        teacher.setFaculty("Biology");

        Set<Course> courses = new HashSet<>();
        Course course1 = new Course();
        course1.setId(1L);
        courses.add(course1);

        Course course2 = new Course();
        course2.setId(2L);
        courses.add(course2);

        teacher.setCourses(courses);



        TeacherDTO teacherDTO = teacherMapper.toTeacherDTO(teacher);

        assertEquals(1L, teacherDTO.getId());
        assertEquals("Ivan Ivanov", teacherDTO.getFullName());
        assertEquals("Biology", teacherDTO.getFaculty());
        assertEquals(Set.of(1L, 2L), teacherDTO.getCoursesId());
    }
    @Test
    void testTotTeacher() throws SQLException {
        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setId(1L);
        teacherDTO.setFullName("Ivan Ivanov");
        teacherDTO.setFaculty("Biology");
        teacherDTO.setCoursesId(Set.of(1L, 2L));

        Course course1 = new Course();
        course1.setId(1L);

        Course course2 = new Course();
        course2.setId(2L);

        when(courseDAO.getById(1L)).thenReturn(Optional.of(course1));
        when(courseDAO.getById(2L)).thenReturn(Optional.of(course2));

        Teacher teacher = teacherMapper.toTeacher(teacherDTO);

        assertEquals(1L, teacher.getId());
        assertEquals("Ivan Ivanov", teacher.getFullName());
        assertEquals("Biology", teacher.getFaculty());
        assertEquals(Set.of(course1, course2), teacher.getCourses());
    }
}