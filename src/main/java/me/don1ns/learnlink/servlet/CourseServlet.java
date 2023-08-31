package me.don1ns.learnlink.servlet;

import me.don1ns.learnlink.service.CourseService;

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

@WebServlet(name = "CourseServlet", urlPatterns = "/courses/*")
public class CourseServlet extends HttpServlet {
    private CourseService courseService;

    @Override
    public void init() {
        final Object courseService = getServletContext().getAttribute("courseService");
        setCourseService((CourseService) courseService);
    }

    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String requestPath = req.getPathInfo();
            Optional<String> getResponse = courseService.handleGetRequest(requestPath);
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
            courseService.handlePostRequest(bodyParams);
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
            courseService.handlePutRequest(requestPath, bodyParams);
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
            courseService.handleDeleteRequest(requestPath);
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
