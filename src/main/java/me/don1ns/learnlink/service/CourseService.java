package me.don1ns.learnlink.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.don1ns.learnlink.dao.CourseDAO;
import me.don1ns.learnlink.dto.CourseDTO;
import me.don1ns.learnlink.mapper.CourseMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CourseService {
    private final CourseDAO courseDAO;
    private final CourseMapper courseMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CourseService(CourseDAO courseDAO, CourseMapper courseMapper) {
        this.courseDAO = courseDAO;
        this.courseMapper = courseMapper;
    }
    public Optional<String> handleGetRequest(String requestPath) throws JsonProcessingException, SQLException {
        String[] pathArray = requestPath.split("/");
        if(pathArray.length > 0) {
            Long id = Long.parseLong(pathArray[1]);
            if(courseDAO.getById(id).isPresent()) {
                CourseDTO courseDTO = courseMapper.toCourseDTO(courseDAO.getById(id).get());
                return Optional.of(objectMapper.writeValueAsString(courseDTO));
            }
        } else {
            List<CourseDTO> courseDTOS = courseDAO.getAll().stream()
                    .map(courseMapper::toCourseDTO)
                    .collect(Collectors.toList());
            return Optional.of(objectMapper.writeValueAsString(courseDTOS));
        }
        return Optional.empty();
    }
    public void handlePostRequest(String bodyParams) throws JsonProcessingException, SQLException {
        CourseDTO courseDTO = objectMapper.readValue(bodyParams, CourseDTO.class);
        courseDAO.create(courseMapper.toCourse(courseDTO));
    }
    public void handlePutRequest(String requestPath, String bodyParams) throws JsonProcessingException, SQLException {
        String[] pathArray = requestPath.split("/");
        Long id = Long.parseLong(pathArray[1]);
        CourseDTO courseDto = objectMapper.readValue(bodyParams, CourseDTO.class);
        courseDto.setId(id);
        courseDAO.update(courseMapper.toCourse(courseDto));
    }
    public void handleDeleteRequest(String requestPath) throws SQLException {
        String[] pathArray = requestPath.split("/");
        Long id = Long.parseLong(pathArray[1]);
        courseDAO.deleteById(id);
    }
}
