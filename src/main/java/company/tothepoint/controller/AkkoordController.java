package company.tothepoint.controller;

import company.tothepoint.model.akkoord.*;
import company.tothepoint.model.consultant.Consultant;
import company.tothepoint.model.opdracht.Opdracht;
import company.tothepoint.repository.AkkoordRepository;
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

@RestController
@RequestMapping("/akkoorden")
public class AkkoordController {
    private static final Logger LOG = LoggerFactory.getLogger(AkkoordController.class);
    @Autowired
    private AkkoordRepository akkoordRepository;

    @Autowired
    private ConsultantRepository consultantRepository;

    @Autowired
    private OpdrachtRepository opdrachtRepository;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Akkoord>> getAllAkkoords(@RequestParam(required = false) String consultantId) {
        LOG.debug("GET /akkoorden getAllAkkoords() called!");
        if(consultantId != null){
            Optional<List<Akkoord>> contractOption = Optional.ofNullable(akkoordRepository.findByConsultantId(consultantId));
            return contractOption.map(akkoord ->
                    new ResponseEntity<List<Akkoord>>(akkoord, HttpStatus.OK)
            ).orElse(
                    new ResponseEntity<List<Akkoord>>(HttpStatus.NOT_FOUND)
            );
        }
        else{ return new ResponseEntity<List<Akkoord>>(akkoordRepository.findAll(), HttpStatus.OK);}
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<Akkoord> getAkkoord(@PathVariable("id") String id) {
        LOG.debug("GET /akkoorden/"+id+" getAkkoord("+id+") called!");
        Optional<Akkoord> akkoordOption = Optional.ofNullable(akkoordRepository.findOne(id));

        return akkoordOption.map(akkoord->
                new ResponseEntity<>(akkoord, HttpStatus.OK)
        ).orElse(
                new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Akkoord> createAkkoord(@RequestBody Akkoord akkoord) {
        LOG.debug("POST /akkoorden createAkkoord(..) called!");

        Optional<Consultant> consultantOption = Optional.ofNullable(consultantRepository.findOne(akkoord.getConsultantId()));
        Optional<Opdracht> opdrachtOption = Optional.ofNullable(opdrachtRepository.findOne(akkoord.getOpdrachtId()));

        return consultantOption.flatMap( consultant -> {
            return opdrachtOption.map( opdracht -> {
                return new ResponseEntity<>(akkoordRepository.save(akkoord), HttpStatus.CREATED);
            });
        }).orElse( new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<Akkoord> updateAkkoord(@PathVariable("id") String id, @RequestBody Akkoord akkoord) {
        LOG.debug("PUT /akkoorden/"+id+" updateAkkoord("+id+", ..) called!");
        Optional<Akkoord> existingAkkoord = Optional.ofNullable(akkoordRepository.findOne(id));

        return existingAkkoord.map(bu ->
                {
                    akkoord.setId(id);
                    return new ResponseEntity<>(akkoordRepository.save(akkoord), HttpStatus.OK);
                }
        ).orElse(
                new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<Akkoord> deleteAkkoord(@PathVariable("id") String id) {
        LOG.debug("DELETE /akkoorden/"+id+" deleteAkkoord("+id+") called!");
        if (akkoordRepository.exists(id)) {
            akkoordRepository.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
