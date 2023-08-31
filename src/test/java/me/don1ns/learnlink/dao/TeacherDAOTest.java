package me.don1ns.learnlink.dao;

import me.don1ns.learnlink.model.Course;
import me.don1ns.learnlink.model.Student;
import me.don1ns.learnlink.model.Teacher;
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

class TeacherDAOTest extends PostgreSQLContainer<TeacherDAOTest>{
    private static PostgreSQLContainer<?> container;
    private static Connection connection;
    private static TeacherDAO teacherDAO;

    @BeforeEach
    public void setup() throws SQLException, IOException {
        container = new PostgreSQLContainer<>("postgres:14")
                .withDatabaseName("learnlink")
                .withUsername("postgres")
                .withPassword("postgres")
                .withInitScript("test-tables.sql");

        container.start();

        connection = DriverManager.getConnection(container.getJdbcUrl(), container.getUsername(), container.getPassword());
        teacherDAO = new TeacherDAO(connection);
    }

    @AfterEach
    public void teardown() throws SQLException {
        connection.close();
        container.stop();
    }

    @Test
    void testCreateAndGetById() throws SQLException {
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

        teacherDAO.create(teacher);

        // Проверяем, что курс был успешно создан
        Optional<Teacher> createdTeacherOptional = teacherDAO.getById(teacher.getId());
        assertTrue(createdTeacherOptional.isPresent());
        Teacher createdTeacher = createdTeacherOptional.get();
        assertEquals("Arsen Molotov", createdTeacher.getFullName());
        assertEquals("Economics", createdTeacher.getFaculty());
        assertEquals(courses.size(), createdTeacher.getCourses().size());
    }
    @Test
    void testGetAllAndDelete() throws SQLException {
        assertEquals(2, teacherDAO.getAll().size());
        teacherDAO.deleteById(2L);
        assertEquals(1, teacherDAO.getAll().size());
    }

    @Test
    void testUpdate() throws SQLException {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
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

        teacherDAO.update(teacher);

        // Проверяем, что курс был успешно создан
        Optional<Teacher> createdTeacherOptional = teacherDAO.getById(teacher.getId());
        assertTrue(createdTeacherOptional.isPresent());
        Teacher createdTeacher = createdTeacherOptional.get();
        assertEquals("Arsen Molotov", createdTeacher.getFullName());
        assertEquals("Economics", createdTeacher.getFaculty());
        assertEquals(courses.size(), createdTeacher.getCourses().size());
    }
}