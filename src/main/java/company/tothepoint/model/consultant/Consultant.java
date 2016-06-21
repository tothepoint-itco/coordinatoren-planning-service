package company.tothepoint.model.consultant;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class Consultant {
    @Id
    private String id;

    @NotNull
    @Size(min = 1, max = 32)
    private String voorNaam;

    @NotNull
    @Size(min = 1, max = 127)
    private String familieNaam;

    @NotNull
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate geboorteDatum;

    public Consultant() {
    }

    public Consultant(String voorNaam, String familieNaam, LocalDate geboorteDatum) {
        this.voorNaam = voorNaam;
        this.familieNaam = familieNaam;
        this.geboorteDatum = geboorteDatum;
    }

    public Consultant(String id, String voorNaam, String familieNaam, LocalDate geboorteDatum) {
        this.id = id;
        this.voorNaam = voorNaam;
        this.familieNaam = familieNaam;
        this.geboorteDatum = geboorteDatum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVoorNaam() {
        return voorNaam;
    }

    public void setVoorNaam(String voorNaam) {
        this.voorNaam = voorNaam;
    }

    public String getFamilieNaam() {
        return familieNaam;
    }

    public void setFamilieNaam(String familieNaam) {
        this.familieNaam = familieNaam;
    }

    public LocalDate getGeboorteDatum() {
        return geboorteDatum;
    }

    public void setGeboorteDatum(LocalDate geboorteDatum) {
        this.geboorteDatum = geboorteDatum;
    }
}
