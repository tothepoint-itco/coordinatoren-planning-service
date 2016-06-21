package company.tothepoint.controller;

import company.tothepoint.model.akkoord.Akkoord;
import company.tothepoint.model.akkoord.AkkoordAggregate;
import company.tothepoint.model.bestelbon.Bestelbon;
import company.tothepoint.model.consultant.*;
import company.tothepoint.model.opdracht.Opdracht;
import company.tothepoint.repository.AkkoordRepository;
import company.tothepoint.repository.BestelbonRepository;
import company.tothepoint.repository.ConsultantRepository;
import company.tothepoint.repository.OpdrachtRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/consultants")
public class ConsultantController {
    private static final Logger LOG = LoggerFactory.getLogger(ConsultantController.class);
    @Autowired
    private ConsultantRepository consultantRepository;

    @Autowired
    private AkkoordRepository akkoordRepository;

    @Autowired
    private OpdrachtRepository opdrachtRepository;

    @Autowired
    private BestelbonRepository bestelbonRepository;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Consultant>> getAllConsultants() {
        LOG.debug("GET /consultants getAllConsultants() called!");

        List<Consultant> orderedConsultants = consultantRepository.findAll().stream()
                .sorted((o1, o2) -> o1.getFamilieNaam().compareTo(o2.getFamilieNaam()))
                .sorted(((o1, o2) -> o1.getVoorNaam().compareTo(o2.getVoorNaam())))
                .collect(Collectors.toList());

        return new ResponseEntity<>(orderedConsultants, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<Consultant> getConsultant(@PathVariable("id") String id) {
        LOG.debug("GET /consultants/" + id + " getConsultant(" + id + ") called!");
        Optional<Consultant> consultantOption = Optional.ofNullable(consultantRepository.findOne(id));

        return consultantOption.map(consultant ->
                new ResponseEntity<>(consultant, HttpStatus.OK)
        ).orElse(
                new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }

    @RequestMapping(method = RequestMethod.GET, value = "/aggregated")
    public ResponseEntity<List<ConsultantAggregate>> getAllAggregatedConsultants() {
        List<ConsultantAggregate> aggregatedConsultants = consultantRepository.findAll().stream()
                .sorted((o1, o2) -> o1.getFamilieNaam().compareTo(o2.getFamilieNaam()))
                .sorted(((o1, o2) -> o1.getVoorNaam().compareTo(o2.getVoorNaam())))
                .map(consultant -> {
                    return aggregateConsultant(consultant.getId());
                }).filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        return new ResponseEntity<List<ConsultantAggregate>>(aggregatedConsultants, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/aggregated/{id}")
    public ResponseEntity<ConsultantAggregate> getAggregatedConsultant(@PathVariable("id") String id) {
        Optional<Consultant> consultantOption = Optional.ofNullable(consultantRepository.findOne(id));
        return consultantOption.flatMap(consultant -> {
            return aggregateConsultant(consultant.getId()).map(aggregatedConsultant -> {
                return new ResponseEntity<>(aggregatedConsultant, HttpStatus.OK);
            });
        }).orElse(
                new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }

    private Optional<ConsultantAggregate> aggregateConsultant(String consultantId) {
        Optional<Consultant> consultantOption = Optional.ofNullable(consultantRepository.findOne(consultantId));

        return consultantOption.map(consultant -> {
            List<Akkoord> akkoorden = akkoordRepository.findByConsultantId(consultantId);

            List<AkkoordAggregate> akkoordAggregates = akkoorden.stream().map(akkoord -> {
                return aggregateAkkoord(akkoord.getId());
            }).filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());

            return new ConsultantAggregate(consultant, akkoordAggregates);
        });
    }

    private Optional<AkkoordAggregate> aggregateAkkoord(String akkoordId) {
        Optional<Akkoord> akkoordOption = Optional.ofNullable(akkoordRepository.findOne(akkoordId));
        return akkoordOption.flatMap(akkoord -> {
            Optional<Opdracht> opdrachtOption = Optional.ofNullable(opdrachtRepository.findOne(akkoord.getOpdrachtId()));
            return opdrachtOption.map(opdracht -> {
                //Changed to findByAkkoordId
                List<Bestelbon> bestelbonnen = bestelbonRepository.findByAkkoordId(akkoord.getId());
                return new AkkoordAggregate(akkoord, opdracht, bestelbonnen);
            });
        });
    }
}
