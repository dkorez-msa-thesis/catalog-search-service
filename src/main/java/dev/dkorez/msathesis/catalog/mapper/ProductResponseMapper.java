package dev.dkorez.msathesis.catalog.mapper;

import dev.dkorez.msathesis.catalog.dto.ProductDto;
import dev.dkorez.msathesis.catalog.dto.TagDto;
import dev.dkorez.msathesis.catalog.grpc.ProductResponse;
import dev.dkorez.msathesis.catalog.grpc.SpecsGrpc;
import dev.dkorez.msathesis.catalog.model.ProductRestResponse;
import dev.dkorez.msathesis.catalog.model.ProductGqlResponse;

import java.util.Collections;
import java.util.List;

public class ProductResponseMapper {
    public static ProductRestResponse toRestResponse(ProductDto productDto) {
        if (productDto == null)
            return null;

        ProductRestResponse response = new ProductRestResponse();
        response.setId(productDto.getId());
        response.setName(productDto.getName());
        response.setDescription(productDto.getDescription());
        response.setPrice(productDto.getPrice());
        response.setQuantity(productDto.getQuantity());
        response.setCategoryName(productDto.getCategory() != null ?
                productDto.getCategory().getName() : null);
        response.setBrandName(productDto.getBrand() != null ?
                productDto.getBrand().getName() : null);
        response.setSpecs(productDto.getSpecs() != null ?
                productDto.getSpecs().stream().map(SpecsResponseMapper::toResponse).toList() :
                Collections.emptyList());
        response.setTags(productDto.getTags() != null ?
                productDto.getTags().stream().map(ProductResponseMapper::getTagName).toList() :
                Collections.emptyList());

        return response;
    }

    public static ProductGqlResponse toGqlResponse(ProductDto productDto) {
        ProductGqlResponse response = new ProductGqlResponse();
        response.setId(productDto.getId());
        response.setName(productDto.getName());
        response.setDescription(productDto.getDescription());
        response.setPrice(productDto.getPrice().toPlainString());
        response.setQuantity(productDto.getQuantity());
        response.setActive(productDto.isActive());
        response.setCategoryName(productDto.getCategory() != null ?
                productDto.getCategory().getName() : null);
        response.setBrandName(productDto.getBrand() != null ?
                productDto.getBrand().getName() : null);
        response.setSpecs(productDto.getSpecs() != null ?
                productDto.getSpecs().stream().map(SpecsResponseMapper::toResponse).toList() :
                Collections.emptyList());
        response.setTags(productDto.getTags() != null ?
                productDto.getTags().stream().map(ProductResponseMapper::getTagName).toList() :
                Collections.emptyList());

        return response;
    }

    public static ProductResponse toGrpcResponse(ProductDto productDto) {
        List<SpecsGrpc> specs = productDto.getSpecs() != null ?
                productDto.getSpecs().stream().map(SpecsResponseMapper::toGrpcResponse).toList() :
                Collections.emptyList();
        List<String> tags = productDto.getTags() != null ?
                productDto.getTags().stream().map(ProductResponseMapper::getTagName).toList() :
                Collections.emptyList();

        return ProductResponse.newBuilder()
                .setId(productDto.getId())
                .setName(productDto.getName())
                .setDescription(productDto.getDescription())
                .setPrice(productDto.getPrice().toPlainString())
                .setQuantity(productDto.getQuantity())
                .setCategoryName(productDto.getCategory() != null ? productDto.getCategory().getName() : null)
                .setBrandName(productDto.getBrand() != null ? productDto.getBrand().getName() : null)
                .addAllSpecs(specs)
                .addAllTags(tags)
                .build();
    }

    private static String getTagName(TagDto dao) {
        return dao.getName();
    }
}
