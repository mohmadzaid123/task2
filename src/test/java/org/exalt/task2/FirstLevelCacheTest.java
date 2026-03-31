package org.exalt.task2;


import org.exalt.task2.entity.Doctor;
import org.exalt.task2.repository.DoctorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class FirstLevelCacheTest {

    @Autowired
    private DoctorRepository doctorRepository;

    @Test
    @Transactional
    void firstLevelCache_shouldIssueOnlyOneSqlQuery() {
        // Call findById twice within same transaction
        // Check console — only ONE SQL SELECT should appear
        Doctor first  = doctorRepository.findById(1L).orElse(null);
        Doctor second = doctorRepository.findById(1L).orElse(null);

        // Both should be the exact same object from cache
        assert first == second;

        System.out.println("Check console — only ONE select query should have fired");
    }
}