package me.don1ns.learnlink.servlet;

import me.don1ns.learnlink.dao.CourseDAO;
import me.don1ns.learnlink.dao.StudentDAO;
import me.don1ns.learnlink.dao.TeacherDAO;
import me.don1ns.learnlink.db.DBConnect;
import me.don1ns.learnlink.mapper.CourseMapper;
import me.don1ns.learnlink.mapper.StudentMapper;
import me.don1ns.learnlink.mapper.TeacherMapper;
import me.don1ns.learnlink.service.CourseService;
import me.don1ns.learnlink.service.StudentService;
import me.don1ns.learnlink.service.TeacherService;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;

@WebListener
public class ContextListener implements ServletContextListener {
    private CourseDAO courseDAO;
    private StudentDAO studentDAO;
    private TeacherDAO teacherDAO;
    private CourseMapper courseMapper;
    private StudentMapper studentMapper;
    private TeacherMapper teacherMapper;
    private CourseService courseService;
    private StudentService studentService;
    private TeacherService teacherService;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        final ServletContext servletContext = servletContextEvent.getServletContext();
        Connection connection = new DBConnect().getConnection();
        courseDAO = new CourseDAO(connection);
        studentDAO = new StudentDAO(connection);
        teacherDAO = new TeacherDAO(connection);
        courseMapper = new CourseMapper(teacherDAO, studentDAO);
        studentMapper = new StudentMapper(courseDAO);
        teacherMapper = new TeacherMapper(courseDAO);
        courseService = new CourseService(courseDAO, courseMapper);
        studentService = new StudentService(studentDAO, studentMapper);
        teacherService = new TeacherService(teacherDAO, teacherMapper);

        servletContext.setAttribute("courseService", courseService);
        servletContext.setAttribute("studentService", studentService);
        servletContext.setAttribute("teacherService", teacherService);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
