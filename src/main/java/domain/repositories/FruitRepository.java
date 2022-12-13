package domain.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import domain.entities.Fruit;
import io.smallrye.mutiny.Uni;

public interface FruitRepository {
    Uni<List<Fruit>> getAll();

    Uni<Optional<Fruit>> getById(UUID id);

    Uni<Fruit> addFruit(Fruit fruit);

    Uni<Boolean> deleteFruit(UUID id);
}
