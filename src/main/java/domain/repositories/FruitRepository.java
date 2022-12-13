package domain.repositories;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import domain.entities.Fruit;

public interface FruitRepository {
    Set<Fruit> getAll();

    Optional<Fruit> getById(UUID id);

    Fruit add(Fruit fruit);

    Boolean delete(UUID id);
}
