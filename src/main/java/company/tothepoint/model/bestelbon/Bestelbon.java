package company.tothepoint.model.bestelbon;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

public class Bestelbon {
    @Id
    private String id;
    private String akkoordId;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate startDatum;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate eindDatum;

    public Bestelbon() {
    }

    public Bestelbon(LocalDate startDatum, LocalDate eindDatum,  String akkoordId) {
        this.startDatum = startDatum;
        this.eindDatum = eindDatum;
        this.akkoordId = akkoordId;
    }

    public String getAkkoordId() {
        return akkoordId;
    }
    public void setAkkoordId(String akkoordId) {
        this.akkoordId = akkoordId;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
