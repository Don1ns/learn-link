package me.don1ns.learnlink.servlet;

import me.don1ns.learnlink.dto.TeacherDTO;
import me.don1ns.learnlink.service.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class TeacherServletTest {
    private TeacherService teacherService;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private PrintWriter printWriter;
    private BufferedReader reader = mock(BufferedReader.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private TeacherServlet teacherServlet;
    @BeforeEach
    public void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        reader = mock(BufferedReader.class);
        printWriter = mock(PrintWriter.class);
        teacherService = mock(TeacherService.class);
        teacherServlet = new TeacherServlet();
        teacherServlet.setTeacherService(teacherService);
    }
    @Test
    void testDoGet() throws ServletException, IOException, SQLException {
        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setId(1L);
        teacherDTO.setFullName("Arsen Molotov");
        teacherDTO.setFaculty("Economics");
        teacherDTO.setCoursesId(Set.of(1L, 2L));

        // Задаем ожидаемое значение для метода getPathInfo
        when(request.getPathInfo()).thenReturn("/courses/1");

        //Задаем ожидаемое значение для метода handleGetRequest
        when(teacherService.handleGetRequest("/courses/1")).thenReturn(Optional.of(objectMapper.writeValueAsString(teacherDTO)));


        // Задаем ожидаемое значение для метода getWriter
        when(response.getWriter()).thenReturn(printWriter);

        // Вызываем метод сервлета
        teacherServlet.doGet(request, response);

        // Проверяем полученный вывод
        verify(response).setContentType("application/json; charset=UTF-8");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response.getWriter()).write(objectMapper.writeValueAsString(teacherDTO));
    }
    @Test
    void testDoPost() throws ServletException, IOException, SQLException {
        String bodyParams = "{\"fullName\": \"Ivan Ivanov\", \"coursesId\": [1, 2]}";
        when(request.getReader()).thenReturn(reader);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(bodyParams)));
        when(response.getWriter()).thenReturn(printWriter);

        teacherServlet.doPost(request, response);

        verify(teacherService, times(1)).handlePostRequest(bodyParams);
        verify(response, times(1)).setStatus(HttpServletResponse.SC_CREATED);
        verify(response, times(1)).setCharacterEncoding("UTF-8");
        verify(printWriter, times(1)).write("Объект создан!");
    }
    @Test
    void testDoPut() throws ServletException, IOException, SQLException {
        String bodyParams = "{\"fullName\": \"Ivan Ivanov\", \"coursesId\": [1, 2]}";
        String requestPath = "/students/1";
        when(request.getPathInfo()).thenReturn(requestPath);
        when(request.getReader()).thenReturn(reader);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(bodyParams)));
        when(response.getWriter()).thenReturn(printWriter);

        teacherServlet.doPut(request, response);

        verify(teacherService, times(1)).handlePutRequest(requestPath, bodyParams);
        verify(response, times(1)).setStatus(HttpServletResponse.SC_OK);
        verify(response, times(1)).setCharacterEncoding("UTF-8");
        verify(printWriter, times(1)).write("Объект обновлен!");
    }
    @Test
    void testDoDelete() throws ServletException, IOException, SQLException {
        String requestPath = "/students/1";
        when(request.getPathInfo()).thenReturn(requestPath);
        when(response.getWriter()).thenReturn(printWriter);

        teacherServlet.doDelete(request, response);

        verify(teacherService, times(1)).handleDeleteRequest(requestPath);
        verify(response, times(1)).setStatus(HttpServletResponse.SC_OK);
        verify(response, times(1)).setCharacterEncoding("UTF-8");
        verify(printWriter, times(1)).write("Объект удален!");
    }
}