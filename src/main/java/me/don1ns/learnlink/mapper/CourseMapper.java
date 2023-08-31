package me.don1ns.learnlink.mapper;

import me.don1ns.learnlink.dao.StudentDAO;
import me.don1ns.learnlink.dao.TeacherDAO;
import me.don1ns.learnlink.dto.CourseDTO;
import me.don1ns.learnlink.model.Course;
import me.don1ns.learnlink.model.Student;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class CourseMapper {
    private final TeacherDAO teacherDAO;
    private final StudentDAO studentDAO;

    public CourseMapper(TeacherDAO teacherDAO, StudentDAO studentDAO) {
        this.teacherDAO = teacherDAO;
        this.studentDAO = studentDAO;
    }

    public CourseDTO toCourseDTO(Course course) {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setId(course.getId());
        courseDTO.setTitle(course.getTitle());
        courseDTO.setTeacherId(course.getTeacher().getId());
        Set<Long> studentsId = course.getStudents().stream()
                .map(Student::getId)
                .collect(Collectors.toSet());
        courseDTO.setStudentsId(studentsId);
        return courseDTO;
    }

    public Course toCourse(CourseDTO courseDTO) throws SQLException {
        Course course = new Course();
        course.setId(courseDTO.getId());
        course.setTitle(courseDTO.getTitle());
        if (teacherDAO.getById(courseDTO.getTeacherId()).isPresent()) {
            course.setTeacher(teacherDAO.getById(courseDTO.getTeacherId()).get());
        }
        Set<Student> students = new HashSet<>();
        Set<Long> studentsIdFromDTO = courseDTO.getStudentsId();

        if (studentsIdFromDTO != null && !studentsIdFromDTO.isEmpty()) {
            for (Long studentId : studentsIdFromDTO) {
                if (studentDAO.getById(studentId).isPresent()) {
                    Student studentFromDB = studentDAO.getById(studentId).get();
                    students.add(studentFromDB);
                }
            }
        }
        course.setStudents(students);
        return course;
    }
}
