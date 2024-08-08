package dev.dkorez.msathesis.catalog.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.dkorez.msathesis.catalog.messaging.ProductEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@ApplicationScoped
public class ProductEventConsumer {
    private final Logger logger = LoggerFactory.getLogger(ProductEventConsumer.class);

    @Inject
    private ObjectMapper objectMapper;

    @Inject
    private ProductSearchService productSearchService;

    @Incoming("product-updates")
    public void processEvent(String event) {
        try {
            logger.info("incoming product-update: {}", event);
            ProductEvent productEvent = objectMapper.readValue(event, ProductEvent.class);

            switch (productEvent.getType()) {
                case CREATED, UPDATED -> productSearchService.index(productEvent.getProduct());
                case DELETED -> productSearchService.delete(productEvent.getProductId().toString());
            }
        } catch (JsonProcessingException e) {
            logger.error("error processing event: {}", e.getMessage(), e);
        } catch (IOException e) {
            logger.error("error indexing product: {}", e.getMessage(), e);
        }
    }
}
