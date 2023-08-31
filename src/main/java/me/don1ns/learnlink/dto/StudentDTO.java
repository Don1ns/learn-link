package me.don1ns.learnlink.dto;

import me.don1ns.learnlink.model.Course;

import java.util.Objects;
import java.util.Set;

public class StudentDTO {
    private Long id;
    private String fullName;
    private Set<Long> coursesId;

    public StudentDTO() {
    }

    public StudentDTO(Long id, String fullName, Set<Long> coursesId) {
        this.id = id;
        this.fullName = fullName;
        this.coursesId = coursesId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Set<Long> getCoursesId() {
        return coursesId;
    }

    public void setCoursesId(Set<Long> coursesId) {
        this.coursesId = coursesId;
    }

    @Override
    public String toString() {
        return "StudentDTO{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", coursesId=" + coursesId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentDTO that = (StudentDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(fullName, that.fullName) && Objects.equals(coursesId, that.coursesId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, coursesId);
    }
}
