package me.don1ns.learnlink.mapper;

import me.don1ns.learnlink.dao.CourseDAO;
import me.don1ns.learnlink.dto.TeacherDTO;
import me.don1ns.learnlink.model.Course;
import me.don1ns.learnlink.model.Teacher;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class TeacherMapper {
    private final CourseDAO courseDAO;

    public TeacherMapper(CourseDAO courseDAO) {
        this.courseDAO = courseDAO;
    }

    public TeacherDTO toTeacherDTO(Teacher teacher) {
        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setId(teacher.getId());
        teacherDTO.setFaculty(teacher.getFaculty());
        teacherDTO.setFullName(teacher.getFullName());
        Set<Long> coursesId = teacher.getCourses().stream()
                .map(Course::getId)
                .collect(Collectors.toSet());
        teacherDTO.setCoursesId(coursesId);
        return teacherDTO;
    }

    public Teacher toTeacher(TeacherDTO teacherDTO) throws SQLException {
        Teacher teacher = new Teacher();
        teacher.setId(teacherDTO.getId());
        teacher.setFaculty(teacherDTO.getFaculty());
        teacher.setFullName(teacherDTO.getFullName());

        Set<Course> courses = new HashSet<>();
        Set<Long> coursesIdFromDTO = teacherDTO.getCoursesId();

        if (coursesIdFromDTO != null && !coursesIdFromDTO.isEmpty()) {
            for (Long courseId : coursesIdFromDTO) {
                if (courseDAO.getById(courseId).isPresent()) {
                    Course courseFromDB = courseDAO.getById(courseId).get();
                    courses.add(courseFromDB);
                }
            }
        }
        teacher.setCourses(courses);
        return teacher;
    }
}
