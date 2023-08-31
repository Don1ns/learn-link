package me.don1ns.learnlink.mapper;

import me.don1ns.learnlink.dao.StudentDAO;
import me.don1ns.learnlink.dao.TeacherDAO;
import me.don1ns.learnlink.dto.CourseDTO;
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

class CourseMapperTest {

    private TeacherDAO teacherDAO;
    private StudentDAO studentDAO;
    private CourseMapper courseMapper;

    @BeforeEach
    public void setUp() {
        teacherDAO = mock(TeacherDAO.class);
        studentDAO = mock(StudentDAO.class);
        courseMapper = new CourseMapper(teacherDAO, studentDAO);
    }

    @Test
    void testToCourseDTO() throws SQLException {
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

        CourseDTO courseDTO = courseMapper.toCourseDTO(course);

        assertEquals(1L, courseDTO.getId());
        assertEquals("Test Course", courseDTO.getTitle());
        assertEquals(1L, courseDTO.getTeacherId());
        assertEquals(Set.of(1L, 2L), courseDTO.getStudentsId());
    }

    @Test
    void testToCourse() throws SQLException {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setId(1L);
        courseDTO.setTitle("Test Course");
        courseDTO.setTeacherId(1L);
        courseDTO.setStudentsId(Set.of(1L, 2L));

        Teacher teacher = new Teacher();
        teacher.setId(1L);
        Student student1 = new Student();
        student1.setId(1L);
        Student student2 = new Student();
        student2.setId(2L);

        when(teacherDAO.getById(1L)).thenReturn(Optional.of(teacher));
        when(studentDAO.getById(1L)).thenReturn(Optional.of(student1));
        when(studentDAO.getById(2L)).thenReturn(Optional.of(student2));

        Course course = courseMapper.toCourse(courseDTO);

        assertEquals(1L, course.getId());
        assertEquals("Test Course", course.getTitle());
        assertEquals(teacher, course.getTeacher());
        assertEquals(Set.of(student1, student2), course.getStudents());
    }
}