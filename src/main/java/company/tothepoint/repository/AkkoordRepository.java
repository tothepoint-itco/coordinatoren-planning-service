package company.tothepoint.repository;

import company.tothepoint.model.akkoord.Akkoord;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AkkoordRepository extends MongoRepository<Akkoord, String> {
    List<Akkoord> findByConsultantId(String consultantId);
    List<Akkoord> findByOpdrachtId(String opdrachtId);
}
