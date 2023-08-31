package me.don1ns.learnlink.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.don1ns.learnlink.dao.StudentDAO;
import me.don1ns.learnlink.dto.StudentDTO;
import me.don1ns.learnlink.mapper.StudentMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StudentService {
    private final StudentDAO studentDAO;
    private final StudentMapper studentMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public StudentService(StudentDAO studentDAO, StudentMapper studentMapper) {
        this.studentDAO = studentDAO;
        this.studentMapper = studentMapper;
    }

    public Optional<String> handleGetRequest(String requestPath) throws JsonProcessingException, SQLException {
        String[] pathArray = requestPath.split("/");
        if (pathArray.length > 0) {
            Long id = Long.parseLong(pathArray[1]);
            if (studentDAO.getById(id).isPresent()) {
                StudentDTO studentDTO = studentMapper.toStudentDTO(studentDAO.getById(id).get());
                return Optional.of(objectMapper.writeValueAsString(studentDTO));
            }
        } else {
            List<StudentDTO> studentDTOS = studentDAO.getAll().stream()
                    .map(studentMapper::toStudentDTO)
                    .collect(Collectors.toList());
            return Optional.of(objectMapper.writeValueAsString(studentDTOS));
        }
        return Optional.empty();
    }

    public void handlePostRequest(String bodyParams) throws JsonProcessingException, SQLException {
        StudentDTO studentDTO = objectMapper.readValue(bodyParams, StudentDTO.class);
        studentDAO.create(studentMapper.toStudent(studentDTO));
    }

    public void handlePutRequest(String requestPath, String bodyParams) throws JsonProcessingException, SQLException {
        String[] pathArray = requestPath.split("/");
        Long id = Long.parseLong(pathArray[1]);
        StudentDTO studentDto = objectMapper.readValue(bodyParams, StudentDTO.class);
        studentDto.setId(id);
        studentDAO.update(studentMapper.toStudent(studentDto));
    }

    public void handleDeleteRequest(String requestPath) throws SQLException {
        String[] pathArray = requestPath.split("/");
        Long id = Long.parseLong(pathArray[1]);
        studentDAO.deleteById(id);
    }
}
