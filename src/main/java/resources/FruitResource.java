package resources;

import java.util.Optional;
import java.util.UUID;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

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
    public Response list() {
        return Response.ok(repository.getAll()).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") UUID id) {
        Optional<Fruit> fruit = repository.getById(id);
        if (fruit.isEmpty())
            return Response.noContent().status(404).build();
        return Response.ok(fruit.get()).build();
    }

    @POST
    public Response add(Fruit fruit) {
        return Response.ok(repository.add(fruit)).status(201).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") UUID id) {
        return Response.ok(repository.delete(id)).build();
    }
}
