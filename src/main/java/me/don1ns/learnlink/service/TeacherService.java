package me.don1ns.learnlink.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.don1ns.learnlink.dao.TeacherDAO;
import me.don1ns.learnlink.dto.TeacherDTO;
import me.don1ns.learnlink.mapper.TeacherMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TeacherService {
    private final TeacherDAO teacherDAO;
    private final TeacherMapper teacherMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TeacherService(TeacherDAO teacherDAO, TeacherMapper teacherMapper) {
        this.teacherDAO = teacherDAO;
        this.teacherMapper = teacherMapper;
    }

    public Optional<String> handleGetRequest(String requestPath) throws JsonProcessingException, SQLException {
        String[] pathArray = requestPath.split("/");
        if (pathArray.length > 0) {
            Long id = Long.parseLong(pathArray[1]);
            if (teacherDAO.getById(id).isPresent()) {
                TeacherDTO teacherDTO = teacherMapper.toTeacherDTO(teacherDAO.getById(id).get());
                return Optional.of(objectMapper.writeValueAsString(teacherDTO));
            }
        } else {
            List<TeacherDTO> teacherDTOS = teacherDAO.getAll().stream()
                    .map(teacherMapper::toTeacherDTO)
                    .collect(Collectors.toList());
            return Optional.of(objectMapper.writeValueAsString(teacherDTOS));
        }
        return Optional.empty();
    }

    public void handlePostRequest(String bodyParams) throws JsonProcessingException, SQLException {
        TeacherDTO teacherDTO = objectMapper.readValue(bodyParams, TeacherDTO.class);
        teacherDAO.create(teacherMapper.toTeacher(teacherDTO));
    }

    public void handlePutRequest(String requestPath, String bodyParams) throws JsonProcessingException, SQLException {
        String[] pathArray = requestPath.split("/");
        Long id = Long.parseLong(pathArray[1]);
        TeacherDTO teacherDto = objectMapper.readValue(bodyParams, TeacherDTO.class);
        teacherDto.setId(id);
        teacherDAO.update(teacherMapper.toTeacher(teacherDto));
    }

    public void handleDeleteRequest(String requestPath) throws SQLException {
        String[] pathArray = requestPath.split("/");
        Long id = Long.parseLong(pathArray[1]);
        teacherDAO.deleteById(id);
    }
}
