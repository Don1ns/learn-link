package me.don1ns.learnlink.dao;

import me.don1ns.learnlink.model.Course;
import me.don1ns.learnlink.model.Student;

import java.sql.*;
import java.util.*;

public class StudentDAO extends BaseDAO<Student> {
    public StudentDAO(Connection connection) {
        super(connection);
    }

    @Override
    public void create(Student entity) throws SQLException {
        String query = "INSERT INTO students (fullname) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getFullName());
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setId(generatedKeys.getLong(1));
            }
        }

        // Добавляем студента в каждый курс
        Set<Course> courses = entity.getCourses();
        if (courses != null && !courses.isEmpty()) {
            for (Course course : courses) {
                addCourseToStudent(course.getId(), entity.getId());
            }
        }
    }

    @Override
    public List<Student> getAll() throws SQLException {
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM students";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Student student = new Student();
                student.setId(resultSet.getLong("id"));
                student.setFullName(resultSet.getString("fullname"));
                student.setCourses(getCoursesForStudent(student.getId()));
                students.add(student);
            }
        }
        return students;
    }

    @Override
    public Optional<Student> getById(Long id) throws SQLException {
        String query = "SELECT * FROM students WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Student student = new Student();
                student.setId(resultSet.getLong("id"));
                student.setFullName(resultSet.getString("fullname"));
                student.setCourses(getCoursesForStudent(student.getId()));
                return Optional.of(student);
            }
        }
        return Optional.empty();
    }

    @Override
    public void update(Student entity) throws SQLException {
        String query = "DELETE FROM course_student WHERE student_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, entity.getId());
            statement.executeUpdate();
        }
        String query1 = "UPDATE students SET fullName = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query1)) {
            statement.setString(1, entity.getFullName());
            statement.setLong(2, entity.getId());
            statement.executeUpdate();
        }
        Set<Course> courses = entity.getCourses();
        if (courses != null && !courses.isEmpty()) {
            for (Course course : courses) {
                addCourseToStudent(course.getId(), entity.getId());
            }
        }
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        String query = "DELETE FROM course_student WHERE student_id = ?";
        String query1 = "DELETE FROM students WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
        try (PreparedStatement deleteStatement = connection.prepareStatement(query1)) {
            deleteStatement.setLong(1, id);
            deleteStatement.execute();
        }
    }

    private void addCourseToStudent(Long courseId, Long studentId) throws SQLException {
        String query = "INSERT INTO course_student (course_id, student_id) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, courseId);
            statement.setLong(2, studentId);
            statement.executeUpdate();
        }
    }

    private Set<Course> getCoursesForStudent(Long studentId) throws SQLException {
        String query = "SELECT c.id, c.title FROM courses c " +
                "JOIN course_student cs ON c.id = cs.course_id " +
                "WHERE cs.student_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, studentId);
            ResultSet resultSet = statement.executeQuery();
            Set<Course> courses = new HashSet<>();
            while (resultSet.next()) {
                Course course = new Course();
                course.setId(resultSet.getLong("id"));
                course.setTitle(resultSet.getString("title"));
                courses.add(course);
            }
            return courses;
        }
    }

}
