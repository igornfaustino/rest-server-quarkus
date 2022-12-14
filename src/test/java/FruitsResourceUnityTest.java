
import io.quarkus.test.junit.QuarkusTest;
import repositories.fake.FruitFakeRepository;
import resources.FruitResource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.entities.Fruit;
import domain.repositories.FruitRepository;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@QuarkusTest
public class FruitsResourceUnityTest {
    FruitResource resource;
    FruitRepository repository;

    @BeforeEach
    void setup() {
        repository = new FruitFakeRepository();
        resource = new FruitResource(repository);
    }

    @Test
    public void shouldReturnAnTheCorrectlyListOfFruits() {
        repository.addFruit(new Fruit("Apple", "Red fruit"));
        repository.addFruit(new Fruit("Orange", "Orange fruit"));

        List<Fruit> result = resource.list().await().atMost(Duration.ofMillis(500));
        Fruit firstFruit = result.get(0);
        Fruit secondFruit = result.get(1);

        Assertions.assertEquals(firstFruit.getName(), "Apple");
        Assertions.assertEquals(firstFruit.getDescription(), "Red fruit");
        Assertions.assertEquals(secondFruit.getName(), "Orange");
        Assertions.assertEquals(secondFruit.getDescription(), "Orange fruit");
    }

    @Test
    public void shouldAddAFruit() {
        Fruit addedFruit = resource.add(new Fruit("Apple", "Red fruit")).await().atMost(Duration.ofMillis(500));

        Optional<Fruit> result = resource.getById(addedFruit.getId()).await().atMost(Duration.ofMillis(300));

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(result.get().getName(), "Apple");
    }

    @Test
    public void shouldDeleteAFruit() {
        Fruit addedFruit = resource.add(new Fruit("Apple", "Red fruit")).await().atMost(Duration.ofMillis(500));

        resource.delete(addedFruit.getId()).await().atMost(Duration.ofMillis(300));

        Optional<Fruit> result = resource.getById(addedFruit.getId()).await().atMost(Duration.ofMillis(300));
        Assertions.assertFalse(result.isPresent());
    }
}