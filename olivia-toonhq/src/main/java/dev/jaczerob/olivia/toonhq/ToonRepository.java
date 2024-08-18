package dev.jaczerob.olivia.toonhq;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ToonRepository extends MongoRepository<ToonHQToon, Integer> {

}
