package me.don1ns.learnlink.dto;

import me.don1ns.learnlink.model.Course;

import java.util.Objects;
import java.util.Set;

public class TeacherDTO {
    private Long id;
    private String fullName;
    private String faculty;
    private Set<Long> coursesId;

    public TeacherDTO() {
    }

    public TeacherDTO(Long id, String fullName, String faculty, Set<Long> coursesId) {
        this.id = id;
        this.fullName = fullName;
        this.faculty = faculty;
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

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public Set<Long> getCoursesId() {
        return coursesId;
    }

    public void setCoursesId(Set<Long> coursesId) {
        this.coursesId = coursesId;
    }

    @Override
    public String toString() {
        return "TeacherDTO{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", faculty='" + faculty + '\'' +
                ", coursesId=" + coursesId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeacherDTO that = (TeacherDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(fullName, that.fullName) && Objects.equals(faculty, that.faculty) && Objects.equals(coursesId, that.coursesId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, faculty, coursesId);
    }
}
