package company.tothepoint.repository;

import company.tothepoint.model.bestelbon.Bestelbon;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BestelbonRepository extends MongoRepository<Bestelbon, String> {
    //List<Bestelbon> findByProjectCode(String projectCode);
    List<Bestelbon> findByAkkoordId(String akkoordId);

}
