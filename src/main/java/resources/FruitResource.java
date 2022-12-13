package resources;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.faulttolerance.Retry;
import org.jboss.logging.Logger;

import domain.entities.Fruit;
import domain.repositories.FruitRepository;
import io.smallrye.mutiny.Uni;
import repositories.postgres.FruitPostgresRepository;

@Path("/fruits")
public class FruitResource {
    FruitRepository fruitRepository;

    @Inject
    FruitPostgresRepository postgresRepository;
    private AtomicLong counter = new AtomicLong(0);

    private static final Logger LOGGER = Logger.getLogger(FruitResource.class);

    @Inject
    public FruitResource(@Named("fake") FruitRepository fruitRepository) {
        this.fruitRepository = fruitRepository;
        fruitRepository.add(new Fruit("Morango", "Fruta doce"));
        fruitRepository.add(new Fruit("Pera", "Fruta doce"));
    }

    @GET
    public Uni<List<Fruit>> list() {
        LOGGER.info("Listing all fruits");
        return postgresRepository.getAll();
    }

    @GET
    @Path("/can-fail")
    @Retry(maxRetries = 4)
    public Response maybeFaillist() {
        final Long invocationNumber = counter.getAndIncrement();

        maybeFail(String.format("CoffeeResource#coffees() invocation #%d failed", invocationNumber));

        LOGGER.infof("CoffeeResource#coffees() invocation #%d returning successfully", invocationNumber);
        return Response.ok(fruitRepository.getAll()).build();
    }

    private void maybeFail(String failureLogMessage) {
        if (new Random().nextBoolean()) {
            LOGGER.error(failureLogMessage);
            throw new RuntimeException("Resource failure.");
        }
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") UUID id) {
        Optional<Fruit> fruit = fruitRepository.getById(id);
        if (fruit.isEmpty())
            return Response.noContent().status(404).build();
        return Response.ok(fruit.get()).build();
    }

    @POST
    public Uni<Fruit> add(Fruit fruit) {
        return postgresRepository.add(fruit);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") UUID id) {
        return Response.ok(fruitRepository.delete(id)).build();
    }
}
