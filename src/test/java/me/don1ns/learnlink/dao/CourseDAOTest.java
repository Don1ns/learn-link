package me.don1ns.learnlink.dao;

import me.don1ns.learnlink.model.Course;
import me.don1ns.learnlink.model.Student;
import me.don1ns.learnlink.model.Teacher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CourseDAOTest extends PostgreSQLContainer<CourseDAOTest> {
    private static PostgreSQLContainer<?> container;
    private static Connection connection;
    private static CourseDAO courseDAO;

    @BeforeEach
    public void setup() throws SQLException, IOException {
        container = new PostgreSQLContainer<>("postgres:14")
                .withDatabaseName("learnlink")
                .withUsername("postgres")
                .withPassword("postgres")
                .withInitScript("test-tables.sql");

        container.start();

        connection = DriverManager.getConnection(container.getJdbcUrl(), container.getUsername(), container.getPassword());
        courseDAO = new CourseDAO(connection);
    }

    @AfterEach
    public void teardown() throws SQLException {
        connection.close();
        container.stop();
    }

    @Test
    void testCreateAndGetById() throws SQLException {
        Course course = new Course();
        course.setTitle("Test Course");

        Teacher teacher = new Teacher();
        teacher.setId(1L);

        course.setTeacher(teacher);

        Set<Student> students = new HashSet<>();
        Student student1 = new Student();
        student1.setId(1L);
        students.add(student1);

        Student student2 = new Student();
        student2.setId(2L);
        students.add(student2);

        course.setStudents(students);

        courseDAO.create(course);

        // Проверяем, что курс был успешно создан
        Optional<Course> createdCourseOptional = courseDAO.getById(course.getId());
        assertTrue(createdCourseOptional.isPresent());
        Course createdCourse = createdCourseOptional.get();
        assertEquals("Test Course", createdCourse.getTitle());
        assertEquals(teacher.getId(), createdCourse.getTeacher().getId());
        assertEquals(students.size(), createdCourse.getStudents().size());
    }

    @Test
    void testGetAllAndDelete() throws SQLException {
        assertEquals(3, courseDAO.getAll().size());
        courseDAO.deleteById(3L);
        assertEquals(2, courseDAO.getAll().size());
    }

    @Test
    void testUpdate() throws SQLException {
        Course course = new Course();
        course.setId(1L);
        course.setTitle("Test Course");


        Teacher teacher = new Teacher();
        teacher.setId(1L);

        course.setTeacher(teacher);

        Set<Student> students = new HashSet<>();
        Student student1 = new Student();
        student1.setId(1L);
        students.add(student1);

        Student student2 = new Student();
        student2.setId(2L);
        students.add(student2);

        course.setStudents(students);
        courseDAO.update(course);

        // Проверяем, что курс был успешно обновлен
        Optional<Course> updatedCourseOptional = courseDAO.getById(course.getId());
        assertTrue(updatedCourseOptional.isPresent());
        Course updatedCourse = updatedCourseOptional.get();
        assertEquals("Test Course", updatedCourse.getTitle());
        assertEquals(teacher.getId(), updatedCourse.getTeacher().getId());
        assertEquals(students.size(), updatedCourse.getStudents().size());
    }
}