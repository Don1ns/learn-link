package me.don1ns.learnlink.mapper;

import me.don1ns.learnlink.dao.CourseDAO;
import me.don1ns.learnlink.dto.CourseDTO;
import me.don1ns.learnlink.dto.StudentDTO;
import me.don1ns.learnlink.model.Course;
import me.don1ns.learnlink.model.Student;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class StudentMapper {
    private final CourseDAO courseDAO;

    public StudentMapper(CourseDAO courseDAO) {
        this.courseDAO = courseDAO;
    }

    public StudentDTO toStudentDTO(Student student) {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(student.getId());
        studentDTO.setFullName(student.getFullName());
        Set<Long> coursesId = student.getCourses().stream()
                .map(Course::getId)
                .collect(Collectors.toSet());
        studentDTO.setCoursesId(coursesId);
        return studentDTO;
    }

    public Student toStudent(StudentDTO studentDTO) throws SQLException {
        Student student = new Student();
        student.setId(studentDTO.getId());
        student.setFullName(studentDTO.getFullName());

        Set<Course> courses = new HashSet<>();
        Set<Long> coursesIdFromDTO = studentDTO.getCoursesId();

        if (coursesIdFromDTO != null && !coursesIdFromDTO.isEmpty()) {
            for (Long courseId : coursesIdFromDTO) {
                if (courseDAO.getById(courseId).isPresent()) {
                    Course courseFromDB = courseDAO.getById(courseId).get();
                    courses.add(courseFromDB);
                }
            }
        }
        student.setCourses(courses);
        return student;
    }
}
