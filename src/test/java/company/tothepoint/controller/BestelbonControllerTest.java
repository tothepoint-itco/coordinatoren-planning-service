package company.tothepoint.controller;

import company.tothepoint.model.bestelbon.Bestelbon;
import company.tothepoint.repository.BestelbonRepository;
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
import java.util.Arrays;
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;
public class BestelbonControllerTest {

    @InjectMocks
    private BestelbonController bestelbonController;


    @Mock
    private BestelbonRepository bestelbonRepository;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void createBestelbonTest() {
        String projectCode = "projectCode";
        LocalDate startDatum = LocalDate.of(1993, 8, 29);
        LocalDate eindDatum = LocalDate.of(2016, 8, 29);

        Bestelbon bestelbonToCreate = new Bestelbon(projectCode, startDatum, eindDatum);

        Bestelbon bestelbonToReturn = new Bestelbon(projectCode, startDatum, eindDatum);
        bestelbonToReturn.setId("some-id");

        when(bestelbonRepository.save(bestelbonToCreate)).thenReturn(bestelbonToReturn);

        ResponseEntity<Bestelbon> expectedResult = new ResponseEntity<Bestelbon>(bestelbonToReturn, HttpStatus.CREATED);
        ResponseEntity<Bestelbon> actualResult = bestelbonController.createBestelbon(bestelbonToCreate);

        verify(bestelbonRepository, times(1)).save(bestelbonToCreate);
        assertThat(actualResult, new ReflectionEquals(expectedResult, "body"));
        assertThat(actualResult.getBody(), new ReflectionEquals(bestelbonToReturn));
    }

    @Test
    public void startDateShouldBeforeEndDateOnCreation() {
        String projectCode = "projectCode";
        LocalDate eindDatum = LocalDate.of(1993, 8, 29);
        LocalDate startDatum = LocalDate.of(2016, 8, 29);

        Bestelbon bestelbonToCreate = new Bestelbon(projectCode, startDatum, eindDatum);

        ResponseEntity<Bestelbon> expectedResult = new ResponseEntity<Bestelbon>(HttpStatus.BAD_REQUEST);
        ResponseEntity<Bestelbon> actualResult = bestelbonController.createBestelbon(bestelbonToCreate);

        verify(bestelbonRepository, never()).save(bestelbonToCreate);
        assertThat(actualResult, new ReflectionEquals(expectedResult));
    }

}
