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

    @Test
    void testAvgPerformanceWhereMatriculeStartsWithNoEmployee() {
        //Given

        //When
        Double avgPerf = employeRepository.avgPerformanceWhereMatriculeStartsWith("C");

        //Then
        Assertions.assertThat(avgPerf).isNull();
    }

    @Test
    void testAvgPerformanceWhereMatriculeStartsWithOnlyOneEmployee() {
        //Given
        employeRepository.save(new Employe("nomTechnicien", "prenomTechnicien","T12345", LocalDate.now().minusYears(2),
                Entreprise.SALAIRE_BASE,1,1.0));

        //When
        Double avgPerf = employeRepository.avgPerformanceWhereMatriculeStartsWith("T");

        //Then
        // Résultat doit correspondre à l'unique employé enregistré donc 1
        Assertions.assertThat(avgPerf).isEqualTo(1);
    }

    @Test
    void testAvgPerformanceWhereMatriculeStartsWithSameTypeOfEmployee() {
        //Given

        employeRepository.save(new Employe("nomA", "prenomA", "C12345", LocalDate.now().minusYears(5),
                Entreprise.SALAIRE_BASE,3,1.0));
        employeRepository.save(new Employe("nomB", "prenomB", "C23456", LocalDate.now().minusYears(5),
                Entreprise.SALAIRE_BASE,0,1.0));

        //When
        Double avgPerf = employeRepository.avgPerformanceWhereMatriculeStartsWith("C");

        //Then
        // Résultat attendu : (3 + 0) / 2 = 1.5
        Assertions.assertThat(avgPerf).isEqualTo(1.5);
    }

    @Test
    void testAvgPerformanceWhereMatriculeStartsWithDifferentTypeOfEmployee() {
        //Given
        employeRepository.save(new Employe("nomCommercial", "prenomCommercial", "C12345", LocalDate.now().minusYears(5),
                Entreprise.SALAIRE_BASE,1,1.0));
        employeRepository.save(new Employe("nomManager", "prenomManager", "M12345", LocalDate.now().minusYears(5),
                Entreprise.SALAIRE_BASE,3,1.0));
        employeRepository.save(new Employe("nomTechnicien", "prenomTechnicien", "T12345", LocalDate.now().minusYears(5),
                Entreprise.SALAIRE_BASE,3,1.0));
        employeRepository.save(new Employe("nomManager", "prenomManager", "M23456", LocalDate.now().minusYears(5),
                Entreprise.SALAIRE_BASE,1,1.0));


        //When
        Double avgPerf = employeRepository.avgPerformanceWhereMatriculeStartsWith("M");

        //Then
        // Résultat : On ne prend en compte que les Manager donc (3 + 1) / 2 = 2
        Assertions.assertThat(avgPerf).isEqualTo(2);
    }
}
