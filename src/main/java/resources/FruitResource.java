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

@Path("/fruits")
public class FruitResource {
    FruitRepository fruitRepository;

    private AtomicLong counter = new AtomicLong(0);

    private static final Logger LOGGER = Logger.getLogger(FruitResource.class);

    @Inject
    public FruitResource(@Named("postgres") FruitRepository fruitRepository) throws InterruptedException {
        this.fruitRepository = fruitRepository;
    }

    @GET
    @Retry(maxRetries = 4)
    public Uni<List<Fruit>> list() {
        LOGGER.info("Listing all fruits");
        return fruitRepository.getAll();
    }

    @GET
    @Path("/can-fail")
    @Retry(maxRetries = 4)
    public Uni<List<Fruit>> maybeFaillist() {
        final Long invocationNumber = counter.getAndIncrement();

        maybeFail(String.format("CoffeeResource#coffees() invocation #%d failed", invocationNumber));

        LOGGER.infof("CoffeeResource#coffees() invocation #%d returning successfully", invocationNumber);
        return fruitRepository.getAll();
    }

    private void maybeFail(String failureLogMessage) {
        if (new Random().nextBoolean()) {
            LOGGER.error(failureLogMessage);
            throw new RuntimeException("Resource failure.");
        }
    }

    @GET
    @Retry(maxRetries = 4)
    @Path("/{id}")
    public Response getById(@PathParam("id") UUID id) {
        Optional<Fruit> fruit = fruitRepository.getById(id).await().indefinitely();
        if (fruit.isEmpty())
            return Response.noContent().status(404).build();
        return Response.ok(fruit.get()).build();
    }

    @POST
    public Uni<Fruit> add(Fruit fruit) {
        return fruitRepository.addFruit(fruit);
    }

    @DELETE
    @Retry(maxRetries = 4)
    @Path("/{id}")
    public Uni<Boolean> delete(@PathParam("id") UUID id) {
        return fruitRepository.deleteFruit(id);
    }
}
