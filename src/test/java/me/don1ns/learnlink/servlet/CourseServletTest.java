package me.don1ns.learnlink.servlet;

import me.don1ns.learnlink.dto.CourseDTO;
import me.don1ns.learnlink.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServletTest {
    private CourseService courseService;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private PrintWriter printWriter;
    private BufferedReader reader = mock(BufferedReader.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private CourseServlet courseServlet;
    @BeforeEach
    public void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        reader = mock(BufferedReader.class);
        printWriter = mock(PrintWriter.class);
        courseService = mock(CourseService.class);
        courseServlet = new CourseServlet();
        courseServlet.setCourseService(courseService);
    }
    @Test
    void testDoGet() throws ServletException, IOException, SQLException {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setId(1L);
        courseDTO.setTitle("Test Course");
        courseDTO.setTeacherId(1L);
        courseDTO.setStudentsId(Set.of(1L, 2L));

        // Задаем ожидаемое значение для метода getPathInfo
        when(request.getPathInfo()).thenReturn("/courses/1");

        //Задаем ожидаемое значение для метода handleGetRequest
        when(courseService.handleGetRequest("/courses/1")).thenReturn(Optional.of(objectMapper.writeValueAsString(courseDTO)));


        // Задаем ожидаемое значение для метода getWriter
        when(response.getWriter()).thenReturn(printWriter);

        // Вызываем метод сервлета
        courseServlet.doGet(request, response);

        // Проверяем полученный вывод
        verify(response).setContentType("application/json; charset=UTF-8");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response.getWriter()).write(objectMapper.writeValueAsString(courseDTO));
    }
    @Test
    void testDoPost() throws ServletException, IOException, SQLException {
        String bodyParams = "{\"title\": \"Management\", \"studentsId\": [1, 2], \"teacherId\": 1}";
        when(request.getReader()).thenReturn(reader);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(bodyParams)));
        when(response.getWriter()).thenReturn(printWriter);

        courseServlet.doPost(request, response);

        verify(courseService, times(1)).handlePostRequest(bodyParams);
        verify(response, times(1)).setStatus(HttpServletResponse.SC_CREATED);
        verify(response, times(1)).setCharacterEncoding("UTF-8");
        verify(printWriter, times(1)).write("Объект создан!");
    }
    @Test
    void testDoPut() throws ServletException, IOException, SQLException {
        String bodyParams = "{\"title\": \"Management\", \"studentsId\": [1, 2], \"teacherId\": 1}";
        String requestPath = "/courses/1";
        when(request.getPathInfo()).thenReturn(requestPath);
        when(request.getReader()).thenReturn(reader);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(bodyParams)));
        when(response.getWriter()).thenReturn(printWriter);

        courseServlet.doPut(request, response);

        verify(courseService, times(1)).handlePutRequest(requestPath, bodyParams);
        verify(response, times(1)).setStatus(HttpServletResponse.SC_OK);
        verify(response, times(1)).setCharacterEncoding("UTF-8");
        verify(printWriter, times(1)).write("Объект обновлен!");
    }
    @Test
    void testDoDelete() throws ServletException, IOException, SQLException {
        String requestPath = "/courses/1";
        when(request.getPathInfo()).thenReturn(requestPath);
        when(response.getWriter()).thenReturn(printWriter);

        courseServlet.doDelete(request, response);

        verify(courseService, times(1)).handleDeleteRequest(requestPath);
        verify(response, times(1)).setStatus(HttpServletResponse.SC_OK);
        verify(response, times(1)).setCharacterEncoding("UTF-8");
        verify(printWriter, times(1)).write("Объект удален!");
    }
}