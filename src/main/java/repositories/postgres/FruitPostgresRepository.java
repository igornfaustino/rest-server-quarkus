package repositories.postgres;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import domain.entities.Fruit;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import repositories.postgres.entities.FruitEntity;

@Singleton
@Named("postgres")
public class FruitPostgresRepository implements PanacheRepository<FruitEntity> {

    public Uni<List<Fruit>> getAll() {
        return listAll().map((entities) -> {
            List<Fruit> fruits = new ArrayList<>();
            for (FruitEntity entity : entities) {
                fruits.add(new Fruit(entity.id, entity.name, entity.description));
            }
            return fruits;
        });
    }

    public Uni<Fruit> add(Fruit fruit) {
        FruitEntity entity = new FruitEntity(fruit.getId(), fruit.getName(), fruit.getDescription());
        return persistAndFlush(entity)
                .map(savedFruit -> new Fruit(savedFruit.id, savedFruit.name, savedFruit.description));
    }
}
