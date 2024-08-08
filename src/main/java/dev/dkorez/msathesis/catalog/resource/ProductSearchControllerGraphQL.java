package dev.dkorez.msathesis.catalog.resource;

import dev.dkorez.msathesis.catalog.mapper.ProductResponseMapper;
import dev.dkorez.msathesis.catalog.model.ProductGqlResponse;
import dev.dkorez.msathesis.catalog.service.ProductSearchService;
import jakarta.inject.Inject;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;

import java.io.IOException;
import java.util.List;

@GraphQLApi
public class ProductSearchControllerGraphQL {
    private final ProductSearchService productSearchService;

    @Inject
    public ProductSearchControllerGraphQL(ProductSearchService productSearchService) {
        this.productSearchService = productSearchService;
    }

    @Query("products")
    public List<ProductGqlResponse> getProducts() throws IOException {
        return productSearchService.listAll().stream()
                .map(ProductResponseMapper::toGqlResponse)
                .toList();
    }

    @Query("product")
    public ProductGqlResponse getProduct(@Name("id") Integer id) throws IOException {
        return ProductResponseMapper.toGqlResponse(productSearchService.get(id.toString()));
    }

    @Query("searchProducts")
    public List<ProductGqlResponse> searchProducts(@Name("q") String q) throws IOException {
        return productSearchService.search(q).stream()
                .map(ProductResponseMapper::toGqlResponse)
                .toList();
    }
}
