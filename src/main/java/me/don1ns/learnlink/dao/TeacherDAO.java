package me.don1ns.learnlink.dao;

import me.don1ns.learnlink.model.Course;
import me.don1ns.learnlink.model.Teacher;

import java.sql.*;
import java.util.*;

public class TeacherDAO extends BaseDAO<Teacher> {

    public TeacherDAO(Connection connection) {
        super(connection);
    }

    @Override
    public void create(Teacher entity) throws SQLException {
        String query = "INSERT INTO teachers (fullname, faculty) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getFullName());
            statement.setString(2, entity.getFaculty());
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setId(generatedKeys.getLong(1));
            }
        }
        Set<Course> courses = entity.getCourses();
        if (courses != null && !courses.isEmpty()) {
            for (Course course : courses) {
                addTeacherToCourse(entity.getId(), course.getId());
            }
        }

    }

    private void addTeacherToCourse(Long teacherId, Long courseId) throws SQLException {
        String query = "UPDATE courses SET teacher_id = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, teacherId);
            statement.setLong(2, courseId);
            statement.executeUpdate();
        }
    }

    @Override
    public List<Teacher> getAll() throws SQLException {
        List<Teacher> teachers = new ArrayList<>();
        String query = "SELECT * FROM teachers";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Teacher teacher = new Teacher();
                teacher.setId(resultSet.getLong("id"));
                teacher.setFullName(resultSet.getString("fullName"));
                teacher.setFaculty(resultSet.getString("faculty"));
                teacher.setCourses(getCoursesForTeacher(teacher.getId()));
                teachers.add(teacher);
            }
        }
        return teachers;
    }

    @Override
    public Optional<Teacher> getById(Long id) throws SQLException {
        String query = "SELECT * FROM teachers WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Teacher teacher = new Teacher();
                teacher.setId(resultSet.getLong("id"));
                teacher.setFullName(resultSet.getString("fullName"));
                teacher.setFaculty(resultSet.getString("faculty"));
                teacher.setCourses(getCoursesForTeacher(teacher.getId()));
                return Optional.of(teacher);
            }
        }
        return Optional.empty();
    }

    @Override
    public void update(Teacher entity) throws SQLException {
        String query = "UPDATE teachers SET fullName = ?, faculty = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, entity.getFullName());
            statement.setString(2, entity.getFaculty());
            statement.setLong(3, entity.getId());
            statement.executeUpdate();
        }
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        String query = "DELETE FROM teachers WHERE id = ?";
        String query1 = "UPDATE courses SET teacher_id = NULL WHERE teacher_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
        try (PreparedStatement deleteStatement = connection.prepareStatement(query1)) {
            deleteStatement.setLong(1, id);
            deleteStatement.execute();
        }
    }

    public Set<Course> getCoursesForTeacher(Long teacherId) throws SQLException {
        String query = "SELECT c.id, c.title FROM courses c " +
                "WHERE c.teacher_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, teacherId);
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
