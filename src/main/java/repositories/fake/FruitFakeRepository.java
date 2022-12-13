package repositories.fake;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.inject.Named;
import javax.inject.Singleton;

import domain.entities.Fruit;
import domain.repositories.FruitRepository;
import io.smallrye.mutiny.Uni;

@Singleton
@Named("fake")
public class FruitFakeRepository implements FruitRepository {
    private Set<Fruit> fruits = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<>()));

    @Override
    public Uni<List<Fruit>> getAll() {
        return Uni.createFrom().item(new ArrayList<>(fruits));
    }

    @Override
    public Uni<Fruit> addFruit(Fruit fruit) {
        fruits.add(fruit);
        return Uni.createFrom().item(fruit);
    }

    @Override
    public Uni<Optional<Fruit>> getById(UUID id) {
        for (Fruit fruit : fruits) {
            if (fruit.getId().equals(id))
                return Uni.createFrom().item(Optional.of(fruit));
        }
        return Uni.createFrom().item(Optional.empty());
    }

    @Override
    public Uni<Boolean> deleteFruit(UUID id) {
        fruits.removeIf(fruit -> fruit.getId().equals(id));
        return Uni.createFrom().item(true);
    }

}
