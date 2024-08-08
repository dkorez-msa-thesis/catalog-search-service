package dev.dkorez.msathesis.catalog.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.dkorez.msathesis.catalog.dto.ProductDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@ApplicationScoped
public class ProductSearchService {
    @Inject
    private RestClient restClient;

    @Inject
    private ObjectMapper objectMapper;

    public void index(ProductDto product) throws IOException {
        Request request = new Request("PUT", "/products/_doc/" + product.getId());
        request.setJsonEntity(objectMapper.writeValueAsString(product));
        restClient.performRequest(request);
    }

    public ProductDto get(String id) throws IOException {
        Request request = new Request("GET", "/products/_source/" + id);
        Response response = restClient.performRequest(request);
        String responseBody = EntityUtils.toString(response.getEntity());
        return objectMapper.readValue(responseBody, ProductDto.class);
    }

    public void delete(String id) throws IOException {
        Request request = new Request("DELETE", "/products/_doc/" + id);
        restClient.performRequest(request);
    }

    public List<ProductDto> listAll() throws IOException {
        Request request = new Request("GET", "/products/_search");

        Response response = restClient.performRequest(request);
        String responseBody = EntityUtils.toString(response.getEntity());
        JsonNode responseJson = objectMapper.readTree(responseBody);
        return parseProducts(responseJson);
    }

    public List<ProductDto> search(String q) throws IOException {
        Request request = new Request("GET", "/products/_search");
        request.setJsonEntity(constructJsonSearchQuery(q));

        Response response = restClient.performRequest(request);
        String responseBody = EntityUtils.toString(response.getEntity());
        JsonNode responseJson = objectMapper.readTree(responseBody);
        return parseProducts(responseJson);
    }

    private List<ProductDto> parseProducts(JsonNode responseJson) throws IOException {
        List<ProductDto> products = new ArrayList<>();
        JsonNode hitsNode = responseJson.path("hits").path("hits");
        Iterator<JsonNode> elements = hitsNode.elements();
        while (elements.hasNext()) {
            JsonNode hit = elements.next();
            JsonNode source = hit.path("_source");
            ProductDto product = objectMapper.treeToValue(source, ProductDto.class);
            products.add(product);
        }

        return products;
    }

    private String constructJsonSearchQuery(String q) {
        ObjectNode rootNode = objectMapper.createObjectNode();
        ObjectNode queryNode = rootNode.putObject("query");

        ObjectNode boolNode = queryNode.putObject("bool");
        ObjectNode mustNode = boolNode.putObject("must");
        ObjectNode innerBoolNode = mustNode.putObject("bool");

        ArrayNode shouldArray = innerBoolNode.putArray("should");

        ObjectNode wildcardNameNode = objectMapper.createObjectNode();
        wildcardNameNode.putObject("wildcard").putObject("name")
                .put("value", "*" + q + "*")
                .put("case_insensitive", true);
        shouldArray.add(wildcardNameNode);

        ObjectNode wildcardDescriptionNode = objectMapper.createObjectNode();
        wildcardDescriptionNode.putObject("wildcard").putObject("description")
                .put("value", "*" + q + "*")
                .put("case_insensitive", true);
        shouldArray.add(wildcardDescriptionNode);

        ObjectNode filterNode = boolNode.putObject("filter");
        filterNode.putObject("term").put("active", true);

        return rootNode.toString();
    }
}
