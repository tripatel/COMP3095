package ca.gbc.productservice.service;

import ca.gbc.productservice.dto.ProductRequest;
import ca.gbc.productservice.dto.ProductResponse;
import ca.gbc.productservice.model.Product;
import ca.gbc.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productrepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public ProductResponse createProduct(ProductRequest productrequest) {

        log.debug("creating new Product: {}", productrequest.name());
        Product product = Product.builder()
                .name(productrequest.name())
                .description(productrequest.description())
                .price(productrequest.price())
                .build();
        //persist product
        productrepository.save(product);

        log.info("Product {} is saved", product.getId());
        return new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice());

    }

    @Override
    public List<ProductResponse> getAllProducts() {

        log.debug("Returning a list products");
        List<Product> products = productrepository.findAll();

        // return products.stream().map(product -> mapToproductresponse(product)).toList();
        return products.stream().map(this::mapToproductresponse).toList();
    }

    private ProductResponse mapToproductresponse(Product product) {
        return new ProductResponse(product.getId(), product.getName()
                , product.getDescription(), product.getPrice());
    }

    @Override
    public String updateProduct(String productId, ProductRequest productrequest) {
        log.debug("Updating product with id {}", productId);

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(productId));
        Product product = mongoTemplate.findOne(query, Product.class);

        if (product != null) {
            product.setName(productrequest.name());
            product.setDescription(productrequest.description());
            product.setPrice(productrequest.price());
            return productrepository.save(product).getId();
        }
        return productId;


    }

    @Override
    public void deleteProduct(String productId) {

        log.debug("Deleting product with id {}", productId);
        productrepository.deleteById(productId);


    }
}