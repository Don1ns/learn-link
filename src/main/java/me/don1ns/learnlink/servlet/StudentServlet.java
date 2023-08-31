package me.don1ns.learnlink.servlet;

import me.don1ns.learnlink.service.StudentService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet(name = "StudentServlet", urlPatterns = "/students/*")
public class StudentServlet extends HttpServlet {
    private StudentService studentService;

    @Override
    public void init() throws ServletException {
        final Object studentService = getServletContext().getAttribute("studentService");
        setStudentService((StudentService) studentService);
    }

    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String requestPath = req.getPathInfo();
            Optional<String> getResponse = studentService.handleGetRequest(requestPath);
            if (getResponse.isPresent()) {
                String getResponse1 = getResponse.get();
                resp.setContentType("application/json; charset=UTF-8");
                resp.setStatus(HttpServletResponse.SC_OK);
                PrintWriter printWriter = resp.getWriter();
                printWriter.write(getResponse1);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.setCharacterEncoding("UTF-8");
                resp.getWriter().write("По запросу ничего не найдено!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write("По запросу ничего не найдено!");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String bodyParams = req.getReader().lines().collect(Collectors.joining());
            studentService.handlePostRequest(bodyParams);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write("Объект создан!");
        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write("Не удалось создать объект!");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String requestPath = req.getPathInfo();
            String bodyParams = req.getReader().lines().collect(Collectors.joining());
            studentService.handlePutRequest(requestPath, bodyParams);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write("Объект обновлен!");
        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write("Не удалось обновить объект!");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String requestPath = req.getPathInfo();
            studentService.handleDeleteRequest(requestPath);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write("Объект удален!");
        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write("Не удалось удалить объект!");
        }
    }
}
