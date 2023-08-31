package me.don1ns.learnlink.dao;

import me.don1ns.learnlink.model.Course;
import me.don1ns.learnlink.model.Student;
import me.don1ns.learnlink.model.Teacher;

import java.sql.*;
import java.util.*;

public class CourseDAO extends BaseDAO<Course> {
    public CourseDAO(Connection connection) {
        super(connection);
    }

    @Override
    public void create(Course entity) throws SQLException {
        String query = "INSERT INTO courses (title, teacher_id) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getTitle());
            statement.setLong(2, entity.getTeacher().getId());
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setId(generatedKeys.getLong(1));
            }
        }

        // Добавляем курс в каждого студента
        Set<Student> students = entity.getStudents();
        if (students != null && !students.isEmpty()) {
            for (Student student : students) {
                addStudentToCourse(student.getId(), entity.getId());
            }
        }
    }


    @Override
    public List<Course> getAll() throws SQLException {
        List<Course> courses = new ArrayList<>();
        String query = "SELECT * FROM courses";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Course course = new Course();
                course.setId(resultSet.getLong("id"));
                course.setTitle(resultSet.getString("title"));
                course.setStudents(getStudentsForCourse(course.getId()));
                course.setTeacher(getTeacherForCourse(course.getId()));
                courses.add(course);
            }
        }
        return courses;
    }

    @Override
    public Optional<Course> getById(Long id) throws SQLException {

        String query = "SELECT * FROM courses WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Course course = new Course();
                course.setId(resultSet.getLong("id"));
                course.setTitle(resultSet.getString("title"));
                course.setStudents(getStudentsForCourse(course.getId()));
                course.setTeacher(getTeacherForCourse(course.getId()));
                return Optional.of(course);
            }
        }
        return Optional.empty();
    }

    @Override
    public void update(Course entity) throws SQLException {
        String query = "DELETE FROM course_student WHERE course_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, entity.getId());
            statement.executeUpdate();
        }
        String query1 = "UPDATE courses SET title = ?, teacher_id = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query1)) {
            statement.setString(1, entity.getTitle());
            statement.setLong(2, entity.getTeacher().getId());
            statement.setLong(3, entity.getId());
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setId(generatedKeys.getLong(1));
            }
        }

        // Добавляем курс в каждого студента
        Set<Student> students = entity.getStudents();
        if (students != null && !students.isEmpty()) {
            for (Student student : students) {
                addStudentToCourse(student.getId(), entity.getId());
            }
        }
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        String query = "DELETE FROM course_student WHERE course_id = ?";
        String query1 = "DELETE FROM courses WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
        try (PreparedStatement deleteStatement = connection.prepareStatement(query1)) {
            deleteStatement.setLong(1, id);
            deleteStatement.execute();
        }

    }

    private void addStudentToCourse(Long studentId, Long courseId) throws SQLException {
        String query = "INSERT INTO course_student (student_id, course_id) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, studentId);
            statement.setLong(2, courseId);
            statement.executeUpdate();
        }
    }

    private Set<Student> getStudentsForCourse(Long courseId) throws SQLException {
        String query = "SELECT s.id, s.fullname FROM students s " +
                "JOIN course_student cs ON s.id = cs.student_id " +
                "WHERE cs.course_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, courseId);
            ResultSet resultSet = statement.executeQuery();
            Set<Student> students = new HashSet<>();
            while (resultSet.next()) {
                Student student = new Student();
                student.setId(resultSet.getLong("id"));
                student.setFullName(resultSet.getString("fullname"));
                students.add(student);
            }
            return students;
        }
    }

    private Teacher getTeacherForCourse(Long courseId) throws SQLException {
        String query = "SELECT t.id, t.fullname, t.faculty FROM teachers t " +
                "JOIN courses c ON t.id = c.teacher_id " +
                "WHERE c.id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, courseId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Teacher teacher = new Teacher();
                teacher.setId(resultSet.getLong("id"));
                teacher.setFullName(resultSet.getString("fullname"));
                teacher.setFaculty(resultSet.getString("faculty"));
                return teacher;
            }
        }
        return null;
    }
}
