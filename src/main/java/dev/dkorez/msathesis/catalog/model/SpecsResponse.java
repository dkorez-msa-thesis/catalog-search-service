package dev.dkorez.msathesis.catalog.model;

import lombok.Data;
import org.eclipse.microprofile.graphql.Type;

@Data
@Type("specification")
public class SpecsResponse {
    String name;
    String value;
}
