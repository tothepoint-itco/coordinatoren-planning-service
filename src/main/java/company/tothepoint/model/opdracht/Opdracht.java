package company.tothepoint.model.opdracht;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class Opdracht {
    @Id
    private String id;

    @NotNull(message = "opdracht.error.locatie.notnull")
    @Size(min = 1, message = "opdracht.error.locatie.notnull")
    private String locatie;

    private String tarief;

    @NotNull(message = "opdracht.error.accountmanager.notnull")
    @Size(min = 1, message = "opdracht.error.accountmanager.notnull")
    private String accountManager;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate startDatum;

    @NotNull(message = "opdracht.error.klant.notnull")
    @Size(min = 1, message = "opdracht.error.klant.notnull")
    private String klant;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate deadline;
    private String info;

    public Opdracht() {
    }

    public Opdracht(String locatie, String tarief, String accountManager, LocalDate startDatum, String klant, LocalDate deadline, String info) {
        this.locatie = locatie;
        this.tarief = tarief;
        this.accountManager = accountManager;
        this.startDatum = startDatum;
        this.klant = klant;
        this.deadline = deadline;
        this.info = info;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocatie() {
        return locatie;
    }

    public void setLocatie(String locatie) {
        this.locatie = locatie;
    }

    public String getTarief() {
        return tarief;
    }

    public void setTarief(String tarief) {
        this.tarief = tarief;
    }

    public String getAccountManager() {
        return accountManager;
    }

    public void setAccountManager(String accountManager) {
        this.accountManager = accountManager;
    }

    @JsonFormat(pattern = "dd/MM/yyyy")
    public LocalDate getStartDatum() {
        return startDatum;
    }

    @JsonFormat(pattern = "d/M/yyyy")
    public void setStartDatum(LocalDate startDatum) {
        this.startDatum = startDatum;
    }

    public String getKlant() {
        return klant;
    }

    public void setKlant(String klant) {
        this.klant = klant;
    }

    @JsonFormat(pattern = "dd/MM/yyyy")
    public LocalDate getDeadline() {
        return deadline;
    }

    @JsonFormat(pattern = "d/M/yyyy")
    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
