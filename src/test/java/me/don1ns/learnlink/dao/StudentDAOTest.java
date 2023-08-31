package me.don1ns.learnlink.dao;

import me.don1ns.learnlink.model.Course;
import me.don1ns.learnlink.model.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class StudentDAOTest extends PostgreSQLContainer<StudentDAOTest> {
    private static PostgreSQLContainer<?> container;
    private static Connection connection;
    private static StudentDAO studentDAO;

    @BeforeEach
    public void setup() throws SQLException, IOException {
        container = new PostgreSQLContainer<>("postgres:14")
                .withDatabaseName("learnlink")
                .withUsername("postgres")
                .withPassword("postgres")
                .withInitScript("test-tables.sql");

        container.start();

        connection = DriverManager.getConnection(container.getJdbcUrl(), container.getUsername(), container.getPassword());
        studentDAO = new StudentDAO(connection);
    }

    @AfterEach
    public void teardown() throws SQLException {
        connection.close();
        container.stop();
    }

    @Test
    void testCreateAndGetById() throws SQLException {
        Student student = new Student();
        student.setFullName("Andrey Ivanov");

        Set<Course> courses = new HashSet<>();
        Course course1 = new Course();
        course1.setId(1L);
        courses.add(course1);

        Course course2 = new Course();
        course2.setId(2L);
        courses.add(course2);

        student.setCourses(courses);

        studentDAO.create(student);

        // Проверяем, что студент был успешно создан
        Optional<Student> createdStudentOptional = studentDAO.getById(student.getId());
        assertTrue(createdStudentOptional.isPresent());
        Student createdStudent = createdStudentOptional.get();
        assertEquals("Andrey Ivanov", createdStudent.getFullName());
        assertEquals(courses.size(), createdStudent.getCourses().size());
    }

    @Test
    void testGetAllAndDelete() throws SQLException {
        assertEquals(3, studentDAO.getAll().size());
        studentDAO.deleteById(3L);
        assertEquals(2, studentDAO.getAll().size());
    }

    @Test
    void testUpdate() throws SQLException {
        Student student = new Student();
        student.setId(1L);
        student.setFullName("Andrey Ivanov");

        Set<Course> courses = new HashSet<>();
        Course course1 = new Course();
        course1.setId(1L);
        courses.add(course1);

        Course course2 = new Course();
        course2.setId(2L);
        courses.add(course2);

        student.setCourses(courses);

        studentDAO.update(student);

        // Проверяем, что студент был успешно обновлен
        Optional<Student> updatedStudentOptional = studentDAO.getById(student.getId());
        assertTrue(updatedStudentOptional.isPresent());
        Student updatedStudent = updatedStudentOptional.get();
        assertEquals("Andrey Ivanov", updatedStudent.getFullName());
        assertEquals(courses.size(), updatedStudent.getCourses().size());
    }
}