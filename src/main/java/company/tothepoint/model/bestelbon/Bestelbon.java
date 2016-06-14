package company.tothepoint.model.bestelbon;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

public class Bestelbon {
    @Id
    private String id;

    private String projectCode;
    private String klant;
    private String akkoordId;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate startDatum;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate eindDatum;

    public Bestelbon() {
    }

    public Bestelbon(String projectCode, LocalDate startDatum, LocalDate eindDatum, String klant, String consultantId, String akkoordId) {
        this.projectCode = projectCode;
        this.startDatum = startDatum;
        this.eindDatum = eindDatum;
        this.klant = klant;
        this.akkoordId = akkoordId;
    }

    public String getAkkoordId() {
        return akkoordId;
    }
    public void setAkkoordId(String akkoordId) {
        this.akkoordId = akkoordId;
    }

    public String getKlant() {
        return klant;
    }

    public void setKlant(String klant) {
        this.klant = klant;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public LocalDate getStartDatum() {
        return startDatum;
    }

    public void setStartDatum(LocalDate startDatum) {
        this.startDatum = startDatum;
    }

    public LocalDate getEindDatum() {
        return eindDatum;
    }

    public void setEindDatum(LocalDate eindDatum) {
        this.eindDatum = eindDatum;
    }
}
