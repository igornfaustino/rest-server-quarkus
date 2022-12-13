package domain.repositories;

import java.util.Set;

import domain.entities.Fruit;

public interface FruitRepository {
    Set<Fruit> getAll();

    Fruit add(Fruit fruit);
}
