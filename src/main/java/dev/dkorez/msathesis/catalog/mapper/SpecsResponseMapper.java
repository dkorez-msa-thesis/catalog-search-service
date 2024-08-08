package dev.dkorez.msathesis.catalog.mapper;

import dev.dkorez.msathesis.catalog.dto.SpecificationDto;
import dev.dkorez.msathesis.catalog.grpc.SpecsGrpc;
import dev.dkorez.msathesis.catalog.model.SpecsResponse;

public class SpecsResponseMapper {
    public static SpecsResponse toResponse(SpecificationDto dto) {
        if (dto == null)
            return null;

        SpecsResponse response = new SpecsResponse();
        response.setName(dto.getName());
        response.setValue(dto.getValue());

        return response;
    }

    public static SpecsGrpc toGrpcResponse(SpecificationDto dto) {
        if (dto == null)
            return null;

        return SpecsGrpc.newBuilder()
                .setName(dto.getName())
                .setValue(dto.getValue())
                .build();
    }
}
