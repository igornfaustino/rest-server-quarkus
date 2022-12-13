package resources;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

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

    @GET
    @Path("/{id}")
    public Optional<Fruit> getById(@PathParam("id") UUID id) {
        return repository.getById(id);
    }

    @POST
    public Fruit add(Fruit fruit) {
        return repository.add(fruit);
    }
}
