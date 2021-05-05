package com.ipiecoles.java.java350.model.repository;

import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.Entreprise;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
public class EmployeRepositoryTest {

    @Autowired
    private EmployeRepository employeRepository;

    @AfterEach
    @BeforeEach
    public void cleanUp(){
        employeRepository.deleteAll();
    }

    @Test
    void testFindLastMatriculeEmployeM12345() {
        //Given
        Employe e = employeRepository.save(new Employe(
                "Doe", "John", "M12345",
                LocalDate.now(), Entreprise.SALAIRE_BASE, Entreprise.PERFORMANCE_BASE, 1.0
        ));

        //When
        String lastMatricule = employeRepository.findLastMatricule();

        //Then
        Assertions.assertThat(lastMatricule).isEqualTo("12345");
    }

    //3 Employés avec matricules différents
    @Test
    void testFindLastMatricule3Employes(){
        //Given
        employeRepository.save(new Employe("Doe", "John", "C11032",
                LocalDate.now(), Entreprise.SALAIRE_BASE, Entreprise.PERFORMANCE_BASE, 1.0));
        employeRepository.save(new Employe("Doe", "Jane", "M12345",
                LocalDate.now(), Entreprise.SALAIRE_BASE, Entreprise.PERFORMANCE_BASE, 1.0));
        employeRepository.save(new Employe("Doe", "Jim", "T12000",
                LocalDate.now(), Entreprise.SALAIRE_BASE, Entreprise.PERFORMANCE_BASE, 1.0));
        //When
        String lastMatricule = employeRepository.findLastMatricule();
        //Then
        Assertions.assertThat(lastMatricule).isEqualTo("12345");
    }

    @Test
    public void testFindLastMatricule0Employe(){
        //Given
        //When
        String lastMatricule = employeRepository.findLastMatricule();

        //Then
        Assertions.assertThat(lastMatricule).isNull();
    }
}
