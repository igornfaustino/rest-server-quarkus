package repositories.fake;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import domain.entities.Fruit;
import domain.repositories.FruitRepository;

public class FruitFakeRepository implements FruitRepository {
    private Set<Fruit> fruits = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<>()));

    @Override
    public Set<Fruit> getAll() {
        return fruits;
    }

    @Override
    public Fruit add(Fruit fruit) {
        fruits.add(fruit);
        return fruit;
    }

    @Override
    public Optional<Fruit> getById(UUID id) {
        for (Fruit fruit : fruits) {
            if (fruit.getId().equals(id))
                return Optional.of(fruit);
        }
        return Optional.empty();
    }

}
