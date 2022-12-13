package repositories.fake;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Set;

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

}
