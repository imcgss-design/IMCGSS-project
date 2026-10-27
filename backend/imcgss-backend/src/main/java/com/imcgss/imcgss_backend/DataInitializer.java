package com.imcgss.imcgss_backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           StudentRepository studentRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedUsersIfEmpty();
        seedStudentsIfEmpty();
    }

    private void seedUsersIfEmpty() {
        if (userRepository.count() > 0) {
            log.info("Users table already contains data, skipping seeding.");
            return;
        }

        log.info("Users table is empty. Seeding initial accounts for Render deployment...");

        // 1. Administrator
        User admin = new User();
        admin.setUserId("admin");
        admin.setFullName("System Administrator");
        admin.setEmail("admin@imcgss.edu");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole("ADMIN");
        admin.setDepartment("CSE");
        admin.setStatus("ACTIVE");
        userRepository.save(admin);

        // 2. Faculty
        User faculty = new User();
        faculty.setUserId("faculty");
        faculty.setFullName("Faculty Member");
        faculty.setEmail("faculty@imcgss.edu");
        faculty.setPassword(passwordEncoder.encode("faculty123"));
        faculty.setRole("FACULTY");
        faculty.setDepartment("CSE");
        faculty.setStatus("ACTIVE");
        userRepository.save(faculty);

        // 3. Coordinator
        User coord = new User();
        coord.setUserId("coordinator");
        coord.setFullName("Academic Coordinator");
        coord.setEmail("coordinator@imcgss.edu");
        coord.setPassword(passwordEncoder.encode("coord123"));
        coord.setRole("COORDINATOR");
        coord.setDepartment("CSE");
        coord.setStatus("ACTIVE");
        userRepository.save(coord);

        // 4. HOD
        User hod = new User();
        hod.setUserId("hod");
        hod.setFullName("Head of Department");
        hod.setEmail("hod@imcgss.edu");
        hod.setPassword(passwordEncoder.encode("hod123"));
        hod.setRole("HOD");
        hod.setDepartment("CSE");
        hod.setStatus("ACTIVE");
        userRepository.save(hod);

        // 5. Student
        User studentUser = new User();
        studentUser.setUserId("CS036");
        studentUser.setFullName("Diya C K");
        studentUser.setEmail("diya@imcgss.edu");
        studentUser.setPassword(passwordEncoder.encode("student123"));
        studentUser.setRole("STUDENT");
        studentUser.setDepartment("CSE");
        studentUser.setStatus("ACTIVE");
        userRepository.save(studentUser);

        log.info("Successfully seeded 5 default user accounts (admin, faculty, coordinator, hod, student).");
    }

    private void seedStudentsIfEmpty() {
        if (studentRepository.count() > 0) {
            log.info("Students table already contains data, skipping seeding.");
            return;
        }

        log.info("Students table is empty. Seeding sample students...");

        studentRepository.save(new Student("CS036", "Diya C K", "Computer Science", "S5", "2023-2027", "Active"));
        studentRepository.save(new Student("CS042", "Fathima Ameena", "Computer Science", "S5", "2023-2027", "Active"));
        studentRepository.save(new Student("CS014", "Aneena Febin", "Computer Science", "S5", "2023-2027", "Active"));
        studentRepository.save(new Student("CS055", "Rahul Varma", "Computer Science", "S5", "2023-2027", "Active"));

        log.info("Successfully seeded 4 sample student records.");
    }
}
