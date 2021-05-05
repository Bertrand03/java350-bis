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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class EmployeServiceTest {


    @InjectMocks
    private EmployeService employeService;

    @Mock
    private EmployeRepository employeRepository;

    @AfterEach
    @BeforeEach
    public void cleanUp() {
        employeRepository.deleteAll();
    }

    @Test
    void testEmbaucheEmployePleinTempsManagerIngenieur() throws Exception {
        //Given
        String nom = "Jean";
        String prenom = "Aurore";
        Poste poste = Poste.MANAGER;
        NiveauEtude niveauEtude = NiveauEtude.INGENIEUR;
        Double tempsPartiel = 1d;
        //Ajouter les mocks...
        Mockito.when(employeRepository.findLastMatricule()).thenReturn("12345");
        Mockito.when(employeRepository.findByMatricule("M12346")).thenReturn(null);
        Mockito.when(employeRepository.save(Mockito.any(Employe.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());
        //When
        employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);
        //Then
        //Employe employe = employeRepository.findByMatricule("M00001");

        // Avec les ArgumentCaptor qui permettent de sauvegarder un paramètre d'une méthode
        // Ici on récupère l'argument "employe" (qui contient l'objet Employe) de la méthode save() de employeRepository
        ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
        // Pas obligé de mettre le times à 1 si on ne le met pas
        Mockito.verify(employeRepository, Mockito.times(1)).save(employeArgumentCaptor.capture());
        Employe employe = employeArgumentCaptor.getValue();

        Assertions.assertThat(employe).isNotNull();
        Assertions.assertThat(employe.getNom()).isEqualTo(nom);
        Assertions.assertThat(employe.getPrenom()).isEqualTo(prenom);
        Assertions.assertThat(employe.getDateEmbauche()).isEqualTo(LocalDate.now());
        Assertions.assertThat(employe.getMatricule()).isEqualTo("M12346");
        Assertions.assertThat(employe.getTempsPartiel()).isEqualTo(tempsPartiel);
        //1521.22 * 1.6
        Assertions.assertThat(employe.getSalaire()).isEqualTo(2433.95d);

        // Sans les ArgumentCaptor :
        // Avec la technique de Employe employe = employeRepository.findByMatricule("M00001"); dans le when
        // Et à condition de modifier la méthode embaucheEmploye en retournant un employe au lieu de void
        // On peut utiliser employe.
//        Assertions.assertThat(employe).isNotNull();
//        Assertions.assertThat(employe.getNom()).isEqualTo(nom);
//        Assertions.assertThat(employe.getPrenom()).isEqualTo(prenom);
//        Assertions.assertThat(employe.getDateEmbauche()).isEqualTo(LocalDate.now());
//        Assertions.assertThat(employe.getMatricule()).isEqualTo("M12346");
//        Assertions.assertThat(employe.getTempsPartiel()).isEqualTo(tempsPartiel);
//        //1521.22 * 1.6
//        Assertions.assertThat(employe.getSalaire()).isEqualTo(2433.95d);
    }

    @Test
    void testEmbauchePremierEmployeMiTempsManagerIngenieur() throws Exception {
        //Given
        String nom = "Jean";
        String prenom = "Aurore";
        Poste poste = Poste.MANAGER;
        NiveauEtude niveauEtude = NiveauEtude.INGENIEUR;
        Double tempsPartiel = 0.5d;
        //Ajouter les mocks...
        Mockito.when(employeRepository.findLastMatricule()).thenReturn(null);
        Mockito.when(employeRepository.findByMatricule("M00001")).thenReturn(null);
        Mockito.when(employeRepository.save(Mockito.any(Employe.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());
        //When
        employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);
        //Then
        ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository, Mockito.times(1)).save(employeArgumentCaptor.capture());
        Employe employe = employeArgumentCaptor.getValue();

        //Employe employe = employeRepository.findByMatricule("M00001");
        Assertions.assertThat(employe).isNotNull();
        Assertions.assertThat(employe.getNom()).isEqualTo(nom);
        Assertions.assertThat(employe.getPrenom()).isEqualTo(prenom);
        Assertions.assertThat(employe.getDateEmbauche()).isEqualTo(LocalDate.now());
        Assertions.assertThat(employe.getMatricule()).isEqualTo("M00001");
        Assertions.assertThat(employe.getTempsPartiel()).isEqualTo(tempsPartiel);
        //1521.22 * 1.6
        Assertions.assertThat(employe.getSalaire()).isEqualTo(1216.98d);
    }

    @Test
    void testEmbaucheEmployeLimiteMatricule() {
        //Given
        String nom = "Jean";
        String prenom = "Aurore";
        Poste poste = Poste.MANAGER;
        NiveauEtude niveauEtude = NiveauEtude.INGENIEUR;
        Double tempsPartiel = 1d;
        //Ajouter les mocks...
        Mockito.when(employeRepository.findLastMatricule()).thenReturn("99999");
        //When
        // 1ere solution possible en utilisant un try catch pour lever l'exception correspondante
        try {
            employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);
            Assertions.fail("Aurait du planter");
        } catch (Exception e) {
            //Then
            //Vérifie avec un try catch qu'une exception est bien levée, et que c'est la bonne exception
            Assertions.assertThat(e).isInstanceOf(EmployeException.class);
            Assertions.assertThat(e.getMessage()).isEqualTo("Limite des 100000 matricules atteinte !");
        }

        // 2e solution possible en utilisant les assertJ et une fonction lambda
        Assertions.assertThatThrownBy(() -> employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel))
                .isInstanceOf(EmployeException.class)
                .hasMessage("Limite des 100000 matricules atteinte !");
    }

    @Test
    void testEmbaucheEmployeExistant() {
        //Given
        String nom = "Jean";
        String prenom = "Aurore";
        Poste poste = Poste.MANAGER;
        NiveauEtude niveauEtude = NiveauEtude.INGENIEUR;
        Double tempsPartiel = 1d;
        //Employe employe = new Employe("Doe", "John", "M99999".../*Les paramètres pour créer un employé*/);
        //Ajouter les mocks...
        Mockito.when(employeRepository.findLastMatricule()).thenReturn("99998");
        Mockito.when(employeRepository.findByMatricule("M99999")).thenReturn(new Employe());
        //When
        try {
            employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);
            Assertions.fail("Aurait du planter");
        } catch (Exception e) {
            //Then
            //Vérifie qu'une exception est bien levée, et que c'est la bonne exception
            Assertions.assertThat(e).isInstanceOf(EntityExistsException.class);
            Assertions.assertThat(e.getMessage()).isEqualTo("L'employé de matricule M99999 existe déjà en BDD");
        }
        //When/Then
        Assertions.assertThatThrownBy(() -> employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel))
                .isInstanceOf(EntityExistsException.class)
                .hasMessage("L'employé de matricule M99999 existe déjà en BDD");

    }
}

