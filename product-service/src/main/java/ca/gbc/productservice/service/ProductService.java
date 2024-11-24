package ca.gbc.productservice.service;

import ca.gbc.productservice.dto.ProductResponse;
import ca.gbc.productservice.dto.ProductRequest;


import java.util.List;

public interface ProductService {

    ProductResponse createProduct(ProductRequest productrequest);
    List<ProductResponse>getAllProducts();
    String updateProduct(String productId,ProductRequest productrequest);
    void deleteProduct(String productId);
}