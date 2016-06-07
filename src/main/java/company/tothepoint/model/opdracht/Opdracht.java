package company.tothepoint.model.opdracht;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

public class Opdracht {
    @Id
    private String id;

    private String locatie;
    private String tarief;
    private String accountManager;
    private LocalDate startDatum;
    private String klant;
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
