package org.springframework.beans.ioc;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于测试的Classroom类，包含对Teacher和Student的引用
 */
public class Classroom {
    private String name;
    private Teacher teacher;
    private List<Student> students = new ArrayList<>();

    public Classroom() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    /**
     * 为了支持单个学生的注入，添加一个能接收单个学生的setter
     */
    public void setStudents(Student student) {
        this.students.add(student);
    }

    @Override
    public String toString() {
        return "Classroom{" +
                "name='" + name + '\'' +
                ", teacher=" + teacher +
                ", students=" + students +
                '}';
    }
}
