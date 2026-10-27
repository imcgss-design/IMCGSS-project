package com.imcgss.imcgss_backend;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @Column(name = "roll_no", nullable = false, unique = true)
    private String rollNo;

    @Column(name = "student_name", nullable = false)
    private String studentName;

    private String department;
    private String semester;
    private String batch;
    private String status;

    public Student() {
    }

    public Student(String rollNo,
                   String studentName,
                   String department,
                   String semester,
                   String batch,
                   String status) {
        this.rollNo = rollNo;
        this.studentName = studentName;
        this.department = department;
        this.semester = semester;
        this.batch = batch;
        this.status = status;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
