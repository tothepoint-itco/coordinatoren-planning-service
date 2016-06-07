package company.tothepoint.model.bestelbon;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

public class Bestelbon {
    @Id
    private String id;

    private String projectCode;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate startDatum;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate eindDatum;

    public Bestelbon() {
    }

    public Bestelbon(String projectCode, LocalDate startDatum, LocalDate eindDatum) {
        this.projectCode = projectCode;
        this.startDatum = startDatum;
        this.eindDatum = eindDatum;
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
