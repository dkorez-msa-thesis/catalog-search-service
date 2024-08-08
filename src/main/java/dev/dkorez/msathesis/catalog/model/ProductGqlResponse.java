package dev.dkorez.msathesis.catalog.model;

import io.smallrye.graphql.api.AdaptToScalar;
import io.smallrye.graphql.api.Scalar;
import lombok.Data;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Type;

import java.util.List;

@Data
@Type
@Name("product")
public class ProductGqlResponse {
    @AdaptToScalar(Scalar.Int.class)
    Long id;
    String name;
    String description;
    String price;
    int quantity;
    boolean active;
    String categoryName;
    String brandName;
    List<SpecsResponse> specs;
    List<String> tags;
}
