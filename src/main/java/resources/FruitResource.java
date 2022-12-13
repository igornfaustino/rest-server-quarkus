package resources;

import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import domain.entities.Fruit;
import domain.repositories.FruitRepository;
import repositories.fake.FruitFakeRepository;

@Path("/fruits")
public class FruitResource {
    FruitRepository repository;

    public FruitResource() {
        repository = new FruitFakeRepository();
        repository.add(new Fruit("Morango", "Fruta doce"));
        repository.add(new Fruit("Pera", "Fruta doce"));
    }

    @GET
    public Set<Fruit> list() {
        return repository.getAll();
    }

    @POST
    public Fruit add(Fruit fruit) {
        return repository.add(fruit);
    }
}
