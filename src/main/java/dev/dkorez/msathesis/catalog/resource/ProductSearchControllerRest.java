package dev.dkorez.msathesis.catalog.resource;

import dev.dkorez.msathesis.catalog.dto.ProductDto;
import dev.dkorez.msathesis.catalog.mapper.ProductResponseMapper;
import dev.dkorez.msathesis.catalog.model.ProductRestResponse;
import dev.dkorez.msathesis.catalog.service.ProductSearchService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.io.IOException;
import java.util.List;

@ApplicationScoped
@Path("products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductSearchControllerRest {
    @Inject
    private ProductSearchService productSearchService;

    @GET
    public List<ProductDto> getProducts() throws IOException {
        return productSearchService.listAll();
    }

    @GET
    @Path("/{id}")
    public ProductDto getProduct(@PathParam("id") Long id) throws IOException {
        return productSearchService.get(id.toString());
    }

    @GET
    @Path("/search")
    public List<ProductRestResponse> search(@QueryParam("q") String q) throws IOException {
        return productSearchService.search(q).stream()
                .map(ProductResponseMapper::toRestResponse)
                .toList();
    }
}
