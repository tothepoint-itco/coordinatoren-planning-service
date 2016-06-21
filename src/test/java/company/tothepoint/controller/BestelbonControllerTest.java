package company.tothepoint.controller;

import company.tothepoint.model.akkoord.Akkoord;
import company.tothepoint.model.bestelbon.Bestelbon;
import company.tothepoint.model.consultant.Consultant;
import company.tothepoint.repository.AkkoordRepository;
import company.tothepoint.repository.BestelbonRepository;
import company.tothepoint.repository.ConsultantRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.mockito.internal.util.collections.ArrayUtils;
import org.mockito.internal.util.collections.ListUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;
public class BestelbonControllerTest {

    @InjectMocks
    private BestelbonController bestelbonController;

    @Mock
    private BestelbonRepository bestelbonRepository;

    @Mock
    private ConsultantRepository consultantRepository;
    @Mock
    private AkkoordRepository akkoordRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void createBestelbonTest() {
        LocalDate startDatum = LocalDate.of(1993, 8, 29);
        LocalDate eindDatum = LocalDate.of(2016, 8, 29);
        //String consultantId = "111";
        String akkoordId = "222";

        Bestelbon bestelbonToCreate = new Bestelbon(startDatum, eindDatum, akkoordId);

        Bestelbon bestelbonToReturn = new Bestelbon(startDatum, eindDatum,akkoordId);
        bestelbonToReturn.setId("some-id");

        when(akkoordRepository.findOne("222")).thenReturn(new Akkoord());
        when(bestelbonRepository.save(bestelbonToCreate)).thenReturn(bestelbonToReturn);

        ResponseEntity<Bestelbon> expectedResult = new ResponseEntity<Bestelbon>(bestelbonToReturn, HttpStatus.CREATED);
        ResponseEntity<Bestelbon> actualResult = bestelbonController.createBestelbon(bestelbonToCreate);

        verify(akkoordRepository, times(1)).findOne("222");
        verify(bestelbonRepository, times(1)).save(bestelbonToCreate);
        assertThat(actualResult, new ReflectionEquals(expectedResult, "body"));
        assertThat(actualResult.getBody(), new ReflectionEquals(bestelbonToReturn));
    }
    @Test
    public void createBestelbonWithWrongAkkoordAndConsultantTest() {
        LocalDate startDatum = LocalDate.of(1993, 8, 29);
        LocalDate eindDatum = LocalDate.of(2016, 8, 29);
        //String consultantId = "111";
        String akkoordId = "222";

        Bestelbon bestelbonToCreate = new Bestelbon(startDatum, eindDatum, akkoordId);
        when(akkoordRepository.findOne("222")).thenReturn(null);

        ResponseEntity<Bestelbon> expectedResult = new ResponseEntity<Bestelbon>(HttpStatus.BAD_REQUEST);
        ResponseEntity<Bestelbon> actualResult = bestelbonController.createBestelbon(bestelbonToCreate);

        verify(akkoordRepository, times(1)).findOne("222");
        verify(bestelbonRepository, never()).save(bestelbonToCreate);
        assertThat(actualResult, new ReflectionEquals(expectedResult));
    }

    @Test
    public void startDateShouldBeforeEndDateOnCreation() {
        LocalDate eindDatum = LocalDate.of(1993, 8, 29);
        LocalDate startDatum = LocalDate.of(2016, 8, 29);
//        String consultantId = "0001";
        String akkoordId = "010101";

        Bestelbon bestelbonToCreate = new Bestelbon(startDatum, eindDatum, akkoordId);

        ResponseEntity<Bestelbon> expectedResult = new ResponseEntity<Bestelbon>(HttpStatus.BAD_REQUEST);
        ResponseEntity<Bestelbon> actualResult = bestelbonController.createBestelbon(bestelbonToCreate);

        verify(bestelbonRepository, never()).save(bestelbonToCreate);
        assertThat(actualResult, new ReflectionEquals(expectedResult));
    }

//    @Test
//    public void consultantIdAndAkkoordIdShouldExistOnCreation() {
//        final String PROJ_CODE = "proj-code";
//        final String CONS_ID = "cons-id";
//        final String OPDR_ID = "opdr-id";
//        final String BEZETTING = "100";
//        DateTimeFormatter oldFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//        final LocalDate START_DATE = LocalDate.parse("29-08-1993", oldFormat);
//        final LocalDate END_DATE = LocalDate.parse("29-08-2016", oldFormat);
//
//        consultantRepository.save(new Consultant("Butrint", "Van loock", LocalDate.of(1993, 3, 24)));
//        akkoordRepository.save(new Akkoord(PROJ_CODE, OPDR_ID, CONS_ID, BEZETTING, START_DATE, END_DATE));
//        String projectCode = "projectCode";
//        LocalDate startDatum = LocalDate.of(1993, 8, 29);
//        LocalDate eindDatum = LocalDate.of(2016, 8, 29);
//        String klant = "Kind en Gezin";
//        String consultantId = "11111";
//        String akkoordId = "11111";
//
//
//
//        Bestelbon bestelbonToCreate = new Bestelbon(projectCode, startDatum, eindDatum, klant, consultantId, akkoordId);
//
//        Bestelbon bestelbonToReturn = new Bestelbon(projectCode, startDatum, eindDatum, klant, consultantId, akkoordId);
//        bestelbonToReturn.setId("some-id");
//
//        when(bestelbonRepository.save(bestelbonToCreate)).thenReturn(bestelbonToReturn);
//
//        ResponseEntity<Bestelbon> expectedResult = new ResponseEntity<Bestelbon>(bestelbonToReturn, HttpStatus.BAD_REQUEST);
//        ResponseEntity<Bestelbon> actualResult = bestelbonController.createBestelbon(bestelbonToCreate);
//
//        verify(bestelbonRepository, times(1)).save(bestelbonToCreate);
//        assertThat(actualResult, new ReflectionEquals(expectedResult, "body"));
//        assertThat(actualResult.getBody(), new ReflectionEquals(bestelbonToReturn));
//    }

}
