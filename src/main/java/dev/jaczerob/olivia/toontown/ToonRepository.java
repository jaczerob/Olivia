package dev.jaczerob.olivia.toontown;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ToonRepository extends MongoRepository<ToonHQToon, Integer> {

}
