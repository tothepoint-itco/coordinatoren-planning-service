package company.tothepoint.controller;

import company.tothepoint.model.akkoord.Akkoord;
import company.tothepoint.model.consultant.Consultant;
import company.tothepoint.model.opdracht.Opdracht;
import company.tothepoint.repository.AkkoordRepository;
import company.tothepoint.repository.ConsultantRepository;
import company.tothepoint.repository.OpdrachtRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class AkkoordControllerTest {
    private static final Logger LOG = LoggerFactory.getLogger(AkkoordControllerTest.class);

    @InjectMocks
    private AkkoordController akkoordController;

    @Mock
    private AkkoordRepository akkoordRepository;

    @Mock
    private OpdrachtRepository opdrachtRepository;

    @Mock
    private ConsultantRepository consultantRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void createAkkoord() {
        final String PROJ_CODE = "proj-code";
        final String CONS_ID = "cons-id";
        final String OPDR_ID = "opdr-id";
        final String BEZETTING = "100";
        DateTimeFormatter oldFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        final LocalDate START_DATE = LocalDate.parse("29-08-1993", oldFormat);
        final LocalDate END_DATE = LocalDate.parse("29-08-2016", oldFormat);

        Akkoord akkoordToCreate = new Akkoord(PROJ_CODE, OPDR_ID, CONS_ID, BEZETTING, START_DATE, END_DATE);

        Akkoord akkoordToReturn = new Akkoord(PROJ_CODE, OPDR_ID, CONS_ID, BEZETTING, START_DATE, END_DATE);
        akkoordToReturn.setId("some-id");

        when(consultantRepository.findOne(CONS_ID)).thenReturn(new Consultant());
        when(opdrachtRepository.findOne(OPDR_ID)).thenReturn(new Opdracht());
        when(akkoordRepository.save(akkoordToCreate)).thenReturn(akkoordToReturn);
        when(akkoordRepository.findByOpdrachtId(OPDR_ID)).thenReturn(Collections.emptyList());

        ResponseEntity<Akkoord> expectedResult = new ResponseEntity<Akkoord>(akkoordToReturn, HttpStatus.CREATED);
        ResponseEntity<Akkoord> actualResult = akkoordController.createAkkoord(akkoordToCreate);

        verify(consultantRepository, times(1)).findOne(CONS_ID);
        verify(opdrachtRepository, times(1)).findOne(OPDR_ID);
        verify(akkoordRepository, times(1)).save(akkoordToCreate);

        assertThat(actualResult, new ReflectionEquals(expectedResult, "body"));
        assertThat(actualResult.getBody(), new ReflectionEquals(akkoordToReturn));
    }

    @Test
    public void createAkkoordStartDateAfterEndDate() {
        final String PROJ_CODE = "proj-code";
        final String CONS_ID = "cons-id";
        final String OPDR_ID = "opdr-id";
        final String BEZETTING = "100";
        final LocalDate START_DATE = LocalDate.of(1993,8,29);
        final LocalDate END_DATE = LocalDate.of(2016,8,29);

        Akkoord akkoordToCreate = new Akkoord(PROJ_CODE, OPDR_ID, CONS_ID, BEZETTING, END_DATE, START_DATE);

        when(consultantRepository.findOne(CONS_ID)).thenReturn(new Consultant());
        when(opdrachtRepository.findOne(OPDR_ID)).thenReturn(new Opdracht());
        when(akkoordRepository.findByOpdrachtId(OPDR_ID)).thenReturn(Collections.emptyList());

        ResponseEntity<Akkoord> expectedResult = new ResponseEntity<Akkoord>(HttpStatus.BAD_REQUEST);
        ResponseEntity<Akkoord> actualResult = akkoordController.createAkkoord(akkoordToCreate);

        verifyZeroInteractions(consultantRepository);
        verifyZeroInteractions(opdrachtRepository);
        verifyZeroInteractions(akkoordRepository);

        assertThat(actualResult, new ReflectionEquals(expectedResult));
    }

    @Test
    public void createAkkoordForUnexistingConsultant() {
        final String PROJ_CODE = "proj-code";
        final String CONS_ID = "cons-id";
        final String OPDR_ID = "opdr-id";
        final String BEZETTING = "100";
        final LocalDate START_DATE = LocalDate.of(1993,8,29);
        final LocalDate END_DATE = LocalDate.of(2016,8,29);

        Akkoord akkoordToCreate = new Akkoord(PROJ_CODE, OPDR_ID, CONS_ID, BEZETTING, START_DATE, END_DATE);

        when(consultantRepository.findOne(CONS_ID)).thenReturn(null);
        when(opdrachtRepository.findOne(OPDR_ID)).thenReturn(new Opdracht());

        ResponseEntity<Akkoord> expectedResult = new ResponseEntity<Akkoord>(HttpStatus.BAD_REQUEST);
        ResponseEntity<Akkoord> actualResult = akkoordController.createAkkoord(akkoordToCreate);

        verify(consultantRepository, times(1)).findOne(CONS_ID);
        verify(opdrachtRepository, times(1)).findOne(OPDR_ID);
        verifyZeroInteractions(akkoordRepository);

        assertThat(actualResult, new ReflectionEquals(expectedResult));
    }

    @Test
    public void createAkkoordForUnexistingOpdracht() {
        final String PROJ_CODE = "proj-code";
        final String CONS_ID = "cons-id";
        final String OPDR_ID = "opdr-id";
        final String BEZETTING = "100";
        final LocalDate START_DATE = LocalDate.of(1993,8,29);
        final LocalDate END_DATE = LocalDate.of(2016,8,29);

        Akkoord akkoordToCreate = new Akkoord(PROJ_CODE, OPDR_ID, CONS_ID, BEZETTING, START_DATE, END_DATE);

        when(consultantRepository.findOne(CONS_ID)).thenReturn(new Consultant());
        when(opdrachtRepository.findOne(OPDR_ID)).thenReturn(null);

        ResponseEntity<Akkoord> expectedResult = new ResponseEntity<Akkoord>(HttpStatus.BAD_REQUEST);
        ResponseEntity<Akkoord> actualResult = akkoordController.createAkkoord(akkoordToCreate);

        verify(consultantRepository, times(1)).findOne(CONS_ID);
        verify(opdrachtRepository, times(1)).findOne(OPDR_ID);
        verifyZeroInteractions(akkoordRepository);

        assertThat(actualResult, new ReflectionEquals(expectedResult));
    }

    @Test
    public void createAkkoordStartDateBeforeExistingAkkoord() {
        final String PROJ_CODE = "proj-code";
        final String CONS_ID = "cons-id";
        final String OPDR_ID = "opdr-id";
        final String BEZETTING = "100";
        final LocalDate START_DATE = LocalDate.of(1993,8,29);
        final LocalDate END_DATE = LocalDate.of(2016,8,29);

        Akkoord existingAkkoord = new Akkoord(PROJ_CODE, OPDR_ID, CONS_ID, BEZETTING, START_DATE, END_DATE);

        when(akkoordRepository.findByOpdrachtId(OPDR_ID)).thenReturn(Collections.singletonList(existingAkkoord));
        when(consultantRepository.findOne(CONS_ID)).thenReturn(new Consultant());
        when(opdrachtRepository.findOne(OPDR_ID)).thenReturn(new Opdracht());

        Akkoord akkoordToCreate = new Akkoord(PROJ_CODE, OPDR_ID, CONS_ID, BEZETTING, LocalDate.of(2016,7,20), LocalDate.of(2017,1,1));

        ResponseEntity<Akkoord> expectedResult = new ResponseEntity<Akkoord>(HttpStatus.BAD_REQUEST);
        ResponseEntity<Akkoord> actualResult = akkoordController.createAkkoord(akkoordToCreate);

        verify(akkoordRepository, never()).save(any(Akkoord.class));
        assertThat(actualResult, new ReflectionEquals(expectedResult));
    }

    @Test
    public void createAkkoordEndDateAfterExistingAkkoord() {
        final String PROJ_CODE = "proj-code";
        final String CONS_ID = "cons-id";
        final String OPDR_ID = "opdr-id";
        final String BEZETTING = "100";
        final LocalDate START_DATE = LocalDate.of(1993,8,29);
        final LocalDate END_DATE = LocalDate.of(2016,8,29);

        Akkoord existingAkkoord = new Akkoord(PROJ_CODE, OPDR_ID, CONS_ID, BEZETTING, START_DATE, END_DATE);

        when(akkoordRepository.findByOpdrachtId(OPDR_ID)).thenReturn(Collections.singletonList(existingAkkoord));
        when(consultantRepository.findOne(CONS_ID)).thenReturn(new Consultant());
        when(opdrachtRepository.findOne(OPDR_ID)).thenReturn(new Opdracht());

        Akkoord akkoordToCreate = new Akkoord(PROJ_CODE, OPDR_ID, CONS_ID, BEZETTING, LocalDate.of(1990,7,20), LocalDate.of(1994,1,1));

        ResponseEntity<Akkoord> expectedResult = new ResponseEntity<Akkoord>(HttpStatus.BAD_REQUEST);
        ResponseEntity<Akkoord> actualResult = akkoordController.createAkkoord(akkoordToCreate);

        verify(akkoordRepository, never()).save(any(Akkoord.class));
        assertThat(actualResult, new ReflectionEquals(expectedResult));
    }

    @Test
    public void createAkkoordDateWithinExistingDates() {
        final String PROJ_CODE = "proj-code";
        final String CONS_ID = "cons-id";
        final String OPDR_ID = "opdr-id";
        final String BEZETTING = "100";

        Akkoord existingAkkoord1 = new Akkoord(PROJ_CODE, OPDR_ID, CONS_ID, BEZETTING, LocalDate.of(2000, 1, 1), LocalDate.of(2000, 3, 1));
        Akkoord existingAkkoord2 = new Akkoord(PROJ_CODE, OPDR_ID, CONS_ID, BEZETTING, LocalDate.of(2000, 5, 1), LocalDate.of(2000, 8, 1));


        when(akkoordRepository.findByOpdrachtId(OPDR_ID)).thenReturn(Arrays.asList(existingAkkoord1, existingAkkoord2));
        when(consultantRepository.findOne(CONS_ID)).thenReturn(new Consultant());
        when(opdrachtRepository.findOne(OPDR_ID)).thenReturn(new Opdracht());

        Akkoord akkoordToCreate = new Akkoord(PROJ_CODE, OPDR_ID, CONS_ID, BEZETTING, LocalDate.of(2000, 2, 1), LocalDate.of(2000, 4, 1));

        ResponseEntity<Akkoord> expectedResult = new ResponseEntity<Akkoord>(HttpStatus.BAD_REQUEST);
        ResponseEntity<Akkoord> actualResult = akkoordController.createAkkoord(akkoordToCreate);

        verify(akkoordRepository, never()).save(any(Akkoord.class));
        assertThat(actualResult, new ReflectionEquals(expectedResult));
    }


    @Test
    public void updateAkkoord() {
        final String PROJ_CODE = "proj-code";
        final String CONS_ID = "cons-id";
        final String OPDR_ID = "opdr-id";
        final String BEZETTING = "100";

        Akkoord existingAkkoord1 = new Akkoord(PROJ_CODE, OPDR_ID, CONS_ID, BEZETTING, LocalDate.of(2000, 1, 1), LocalDate.of(2000, 3, 1));
        existingAkkoord1.setId("akk-1");
        Akkoord existingAkkoord2 = new Akkoord(PROJ_CODE, OPDR_ID, CONS_ID, BEZETTING, LocalDate.of(2000, 5, 1), LocalDate.of(2000, 8, 1));
        existingAkkoord2.setId("akk-2");


        when(akkoordRepository.findOne("akk-1")).thenReturn(existingAkkoord1);
        when(akkoordRepository.findByOpdrachtId(OPDR_ID)).thenReturn(Arrays.asList(existingAkkoord1, existingAkkoord2));
        when(consultantRepository.findOne(CONS_ID)).thenReturn(new Consultant());
        when(opdrachtRepository.findOne(OPDR_ID)).thenReturn(new Opdracht());

        Akkoord updatedAkkoord = new Akkoord(PROJ_CODE, OPDR_ID, CONS_ID, BEZETTING, LocalDate.of(2000, 2, 1), LocalDate.of(2000, 4, 1));
        updatedAkkoord.setId("akk-1");

        when(akkoordRepository.save(updatedAkkoord)).thenReturn(updatedAkkoord);


        ResponseEntity<Akkoord> expectedResult = new ResponseEntity<Akkoord>(updatedAkkoord, HttpStatus.OK);
        ResponseEntity<Akkoord> actualResult = akkoordController.updateAkkoord("akk-1", updatedAkkoord);

        verify(akkoordRepository, times(1)).save(any(Akkoord.class));
        assertThat(actualResult, new ReflectionEquals(expectedResult, "body"));
        assertThat(actualResult.getBody(), new ReflectionEquals(expectedResult.getBody()));
    }


    @Test
    public void updateAkkoordEndDateBeforeStartDate() {
        final String PROJ_CODE = "proj-code";
        final String CONS_ID = "cons-id";
        final String OPDR_ID = "opdr-id";
        final String BEZETTING = "100";

        Akkoord existingAkkoord = new Akkoord(PROJ_CODE, OPDR_ID, CONS_ID, BEZETTING, LocalDate.of(2000, 1, 1), LocalDate.of(2000, 3, 1));
        existingAkkoord.setId("akk-id");


        when(akkoordRepository.findOne("akk-id")).thenReturn(existingAkkoord);
        when(akkoordRepository.findByOpdrachtId(OPDR_ID)).thenReturn(Collections.singletonList(existingAkkoord));
        when(consultantRepository.findOne(CONS_ID)).thenReturn(new Consultant());
        when(opdrachtRepository.findOne(OPDR_ID)).thenReturn(new Opdracht());

        Akkoord updatedAkkoord = new Akkoord(PROJ_CODE, OPDR_ID, CONS_ID, BEZETTING, LocalDate.of(2000, 3, 1), LocalDate.of(2000, 1, 1));
        updatedAkkoord.setId("akk-id");

        when(akkoordRepository.save(updatedAkkoord)).thenReturn(updatedAkkoord);


        ResponseEntity<Akkoord> expectedResult = new ResponseEntity<Akkoord>(HttpStatus.BAD_REQUEST);
        ResponseEntity<Akkoord> actualResult = akkoordController.updateAkkoord("akk-id", updatedAkkoord);

        verify(akkoordRepository, never()).save(any(Akkoord.class));
        assertThat(actualResult, new ReflectionEquals(expectedResult));
    }

    @Test
    public void updateAkkoordWithDateOverlap() {
        final String PROJ_CODE = "proj-code";
        final String CONS_ID = "cons-id";
        final String OPDR_ID = "opdr-id";
        final String BEZETTING = "100";

        Akkoord existingAkkoord1 = new Akkoord(PROJ_CODE, OPDR_ID, CONS_ID, BEZETTING, LocalDate.of(2000, 1, 1), LocalDate.of(2000, 3, 1));
        existingAkkoord1.setId("akk-1");
        Akkoord existingAkkoord2 = new Akkoord(PROJ_CODE, OPDR_ID, CONS_ID, BEZETTING, LocalDate.of(2000, 5, 1), LocalDate.of(2000, 8, 1));
        existingAkkoord2.setId("akk-2");


        when(akkoordRepository.findOne("akk-1")).thenReturn(existingAkkoord1);
        when(akkoordRepository.findByOpdrachtId(OPDR_ID)).thenReturn(Arrays.asList(existingAkkoord1, existingAkkoord2));
        when(consultantRepository.findOne(CONS_ID)).thenReturn(new Consultant());
        when(opdrachtRepository.findOne(OPDR_ID)).thenReturn(new Opdracht());

        Akkoord updatedAkkoord = new Akkoord(PROJ_CODE, OPDR_ID, CONS_ID, BEZETTING, LocalDate.of(2000, 7, 1), LocalDate.of(2000, 10, 1));


        ResponseEntity<Akkoord> expectedResult = new ResponseEntity<Akkoord>(HttpStatus.BAD_REQUEST);
        ResponseEntity<Akkoord> actualResult = akkoordController.updateAkkoord("akk-1", updatedAkkoord);

        verify(akkoordRepository, never()).save(any(Akkoord.class));
        assertThat(actualResult, new ReflectionEquals(expectedResult));
    }




}
