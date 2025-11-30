package rating.engine.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import rating.engine.persistence.ProductEntity;
import rating.engine.persistence.ProductRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Cacheable(value = "products", key = "#productId")
    public ProductEntity getProduct(String productId) {
        return productRepository.findByProductId(productId).orElseThrow();
    }

}