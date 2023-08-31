package me.don1ns.learnlink.dto;

import me.don1ns.learnlink.model.Student;
import me.don1ns.learnlink.model.Teacher;

import java.util.Objects;
import java.util.Set;

public class CourseDTO {
    private Long id;
    private String title;
    private Set<Long> studentsId;
    private Long teacherId;

    public CourseDTO() {
    }

    public CourseDTO(Long id, String title, Set<Long> studentsId, Long teacherId) {
        this.id = id;
        this.title = title;
        this.studentsId = studentsId;
        this.teacherId = teacherId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Long> getStudentsId() {
        return studentsId;
    }

    public void setStudentsId(Set<Long> studentsId) {
        this.studentsId = studentsId;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    @Override
    public String toString() {
        return "CourseDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", studentsId=" + studentsId +
                ", teacherId=" + teacherId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseDTO courseDTO = (CourseDTO) o;
        return Objects.equals(id, courseDTO.id) && Objects.equals(title, courseDTO.title) && Objects.equals(studentsId, courseDTO.studentsId) && Objects.equals(teacherId, courseDTO.teacherId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, studentsId, teacherId);
    }
}
