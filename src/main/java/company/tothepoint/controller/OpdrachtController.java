package company.tothepoint.controller;

import company.tothepoint.model.opdracht.*;
import company.tothepoint.repository.OpdrachtRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/opdrachten")
public class OpdrachtController {
    private static final Logger LOG = LoggerFactory.getLogger(OpdrachtController.class);
    @Autowired
    private OpdrachtRepository opdrachtRepository;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Opdracht>> getAllOpdrachts() {
        LOG.debug("GET /opdrachten getAllOpdrachts() called!");
        return new ResponseEntity<>(opdrachtRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<Opdracht> getOpdracht(@PathVariable("id") String id) {
        LOG.debug("GET /opdrachten/"+id+" getOpdrachtId("+id+") called!");
        Optional<Opdracht> opdrachtOption = Optional.ofNullable(opdrachtRepository.findOne(id));

        return opdrachtOption.map(opdracht->
                new ResponseEntity<>(opdracht, HttpStatus.OK)
        ).orElse(
                new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Opdracht> createOpdracht(@Validated @RequestBody Opdracht opdracht) {
        LOG.debug("POST /opdrachten createOpdracht(..) called!");
        Opdracht createdOpdracht = opdrachtRepository.save(opdracht);
        return new ResponseEntity<>(createdOpdracht, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<Opdracht> updateOpdracht(@PathVariable("id") String id, @Validated @RequestBody Opdracht opdracht) {
        LOG.debug("PUT /opdrachten/"+id+" updateOpdracht("+id+", ..) called!");
        Optional<Opdracht> existingOpdracht = Optional.ofNullable(opdrachtRepository.findOne(id));

        return existingOpdracht.map(bu ->
                {
                    opdracht.setId(id);
                    return new ResponseEntity<>(opdrachtRepository.save(opdracht), HttpStatus.OK);
                }
        ).orElse(
                new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<Opdracht> deleteOpdracht(@PathVariable("id") String id) {
        LOG.debug("DELETE /opdrachten/"+id+" deleteOpdracht("+id+") called!");
        if (opdrachtRepository.exists(id)) {
            opdrachtRepository.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
