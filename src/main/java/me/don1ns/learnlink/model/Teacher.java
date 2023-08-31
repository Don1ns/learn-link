package me.don1ns.learnlink.model;

import java.util.Objects;
import java.util.Set;

public class Teacher {
    private Long id;
    private String fullName;
    private String faculty;
    private Set<Course> courses;

    public Teacher() {
    }

    public Teacher(Long id, String fullName, String faculty, Set<Course> courses) {
        this.id = id;
        this.fullName = fullName;
        this.faculty = faculty;
        this.courses = courses;
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

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", faculty='" + faculty + '\'' +
                ", courses=" + courses +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return Objects.equals(id, teacher.id) && Objects.equals(fullName, teacher.fullName) && Objects.equals(faculty, teacher.faculty) && Objects.equals(courses, teacher.courses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, faculty, courses);
    }
}
