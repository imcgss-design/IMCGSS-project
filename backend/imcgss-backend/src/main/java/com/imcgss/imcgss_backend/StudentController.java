package com.imcgss.imcgss_backend;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Student addStudent(@RequestBody Student student) {
        if (student.getRollNo() == null || student.getRollNo().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Roll number is required");
        }

        if (studentRepository.existsById(student.getRollNo())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Roll number already exists");
        }

        return studentRepository.save(student);
    }

    @GetMapping
    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    @GetMapping("/{rollNo}")
    public Student getStudent(@PathVariable String rollNo) {
        return studentRepository.findById(rollNo)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Student not found"));
    }

    @PutMapping("/{rollNo}")
    public Student updateStudent(@PathVariable String rollNo, @RequestBody Student student) {
        if (!studentRepository.existsById(rollNo)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found");
        }
        student.setRollNo(rollNo);
        return studentRepository.save(student);
    }

    @DeleteMapping("/{rollNo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable String rollNo) {
        if (!studentRepository.existsById(rollNo)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Student not found");
        }

        studentRepository.deleteById(rollNo);
    }
}
