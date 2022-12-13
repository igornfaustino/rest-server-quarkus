package repositories.postgres;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Named;
import javax.inject.Singleton;

import domain.entities.Fruit;
import domain.repositories.FruitRepository;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import repositories.postgres.entities.FruitEntity;

@Singleton
@Named("postgres")
public class FruitPostgresRepository implements PanacheRepository<FruitEntity>, FruitRepository {

    public Uni<List<Fruit>> getAll() {
        return listAll().map((entities) -> {
            List<Fruit> fruits = new ArrayList<>();
            for (FruitEntity entity : entities) {
                fruits.add(new Fruit(entity.id, entity.name, entity.description));
            }
            return fruits;
        });
    }

    public Uni<Fruit> addFruit(Fruit fruit) {
        FruitEntity entity = new FruitEntity(fruit.getId(), fruit.getName(), fruit.getDescription());
        return persistAndFlush(entity)
                .map(savedFruit -> new Fruit(savedFruit.id, savedFruit.name, savedFruit.description));
    }

    @Override
    public Uni<Optional<Fruit>> getById(UUID id) {
        return list("id", id).map((enties) -> {
            if (enties.isEmpty())
                return Optional.empty();
            FruitEntity firstFruit = enties.get(0);
            return Optional.of(new Fruit(firstFruit.id, firstFruit.name, firstFruit.description));
        });
    }

    @Override
    public Uni<Boolean> deleteFruit(UUID id) {
        return delete("id", id).map((_param) -> {
            return true;
        });
    }
}
