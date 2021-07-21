package com.ipiecoles.java.java350.model.service;

import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.model.exception.EmployeException;
import com.ipiecoles.java.java350.model.repository.EmployeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
public class EmployeServiceIntegrationTest {

    @Autowired
    private EmployeRepository employeRepository;

    @Autowired
    private EmployeService employeService;

    @AfterEach
    @BeforeEach
    public void cleanUp(){
        employeRepository.deleteAll();
    }

    @Test
    void testEmbauchePremierEmployePleinTempsManagerIngenieur() throws Exception {
        //Given
        String nom = "Jean";
        String prenom = "Aurore";
        Poste poste = Poste.MANAGER;
        NiveauEtude niveauEtude = NiveauEtude.INGENIEUR;
        Double tempsPartiel = 1d;
        //When
        employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);
        //Then
        Employe employe = employeRepository.findByMatricule("M00001");
        Assertions.assertThat(employe).isNotNull();
        Assertions.assertThat(employe.getNom()).isEqualTo(nom);
        Assertions.assertThat(employe.getPrenom()).isEqualTo(prenom);
        Assertions.assertThat(employe.getDateEmbauche()).isEqualTo(LocalDate.now());
        Assertions.assertThat(employe.getMatricule()).isEqualTo("M00001");
        Assertions.assertThat(employe.getTempsPartiel()).isEqualTo(tempsPartiel);
        //1521.22 * 1.6
        Assertions.assertThat(employe.getSalaire()).isEqualTo(2433.95d);
    }

    @Test
    void testIntegrationCalculPerformanceCommercialCas2() throws EmployeException {
        //Given
        Employe e = new Employe();
        e.setMatricule("C24355");
        e.setPerformance(4);
        employeRepository.save(e);
        Long objectifCa = 42000L;
        Long caTraite = 37800L;


        //When
        employeService.calculPerformanceCommercial(e.getMatricule(), caTraite, objectifCa);
        // ArgumentCaptor pour ne pas à avoir à changer le Type de retour attendu dans la méthode appelée

        //Then
        Employe employe = employeRepository.findByMatricule("C24355");
        Assertions.assertThat(2).isEqualTo(employe.getPerformance());
    }
}
