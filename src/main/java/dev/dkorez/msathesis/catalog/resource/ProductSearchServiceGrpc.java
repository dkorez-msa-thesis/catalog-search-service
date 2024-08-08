package dev.dkorez.msathesis.catalog.resource;

import com.google.protobuf.Empty;
import dev.dkorez.msathesis.catalog.grpc.*;
import dev.dkorez.msathesis.catalog.mapper.ProductResponseMapper;
import dev.dkorez.msathesis.catalog.service.ProductSearchService;
import io.quarkus.grpc.GrpcService;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;

import java.io.IOException;
import java.util.List;

@GrpcService
public class ProductSearchServiceGrpc implements ProductServiceGrpc {
    private final ProductSearchService productSearchService;

    @Inject
    public ProductSearchServiceGrpc(ProductSearchService productSearchService) {
        this.productSearchService = productSearchService;
    }

    @Override
    public Uni<GrpcProductResponse> getProducts(Empty request) {
        return Uni.createFrom().item(this::listAll);
    }

    @Override
    public Uni<ProductResponse> getProduct(GetProductRequest request) {
        return Uni.createFrom().item(() -> findById(request));
    }

    @Override
    @Blocking
    public Uni<GrpcProductResponse> searchProducts(SearchProductRequest request) {
        return Uni.createFrom().item(() -> search(request));
    }

    private GrpcProductResponse search(SearchProductRequest request) {
        try {
            List<ProductResponse> products = productSearchService.search(request.getQ()).stream()
                    .map(ProductResponseMapper::toGrpcResponse)
                    .toList();

            return GrpcProductResponse.newBuilder().addAllProducts(products).build();
        } catch (IOException e) {
            throw new RuntimeException("cannot fetch products");
        }
    }

    private GrpcProductResponse listAll() {
        try {
            List<ProductResponse> products = productSearchService.listAll().stream()
                    .map(ProductResponseMapper::toGrpcResponse)
                    .toList();

            return GrpcProductResponse.newBuilder().addAllProducts(products).build();
        } catch (IOException e) {
            throw new RuntimeException("cannot fetch products");
        }
    }

    private ProductResponse findById(GetProductRequest request) {
        try {
            return ProductResponseMapper.toGrpcResponse(productSearchService.get(String.valueOf(request.getId())));
        } catch (IOException e) {
            throw new RuntimeException("cannot fetch products");
        }
    }
}
