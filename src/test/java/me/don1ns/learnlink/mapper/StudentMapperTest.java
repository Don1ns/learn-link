package me.don1ns.learnlink.mapper;

import me.don1ns.learnlink.dao.CourseDAO;
import me.don1ns.learnlink.dto.StudentDTO;
import me.don1ns.learnlink.model.Course;
import me.don1ns.learnlink.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StudentMapperTest {
    private StudentMapper studentMapper;
    private CourseDAO courseDAO;

    @BeforeEach
    public void setUp() {
        courseDAO = mock(CourseDAO.class);
        studentMapper = new StudentMapper(courseDAO);
    }

    @Test
    void testToStudentDTO() throws SQLException {
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


        StudentDTO studentDTO = studentMapper.toStudentDTO(student);

        assertEquals(1L, studentDTO.getId());
        assertEquals("Ivan Ivanov", studentDTO.getFullName());
        assertEquals(Set.of(1L, 2L), studentDTO.getCoursesId());
    }

    @Test
    void testToStudent() throws SQLException {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(1L);
        studentDTO.setFullName("Ivan Ivanov");
        studentDTO.setCoursesId(Set.of(1L, 2L));

        Course course1 = new Course();
        course1.setId(1L);

        Course course2 = new Course();
        course2.setId(2L);

        when(courseDAO.getById(1L)).thenReturn(Optional.of(course1));
        when(courseDAO.getById(2L)).thenReturn(Optional.of(course2));

        Student student = studentMapper.toStudent(studentDTO);

        assertEquals(1L, student.getId());
        assertEquals("Ivan Ivanov", student.getFullName());
        assertEquals(Set.of(course1, course2), student.getCourses());
    }
}