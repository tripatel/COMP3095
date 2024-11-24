package ca.gbc.productservice.controller;

import ca.gbc.productservice.dto.ProductRequest;
import ca.gbc.productservice.dto.ProductResponse;
import ca.gbc.productservice.service.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor

public class ProductController {

    private final ProductServiceImpl ProductService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {

        ProductResponse createProduct = ProductService.createProduct(productRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/product/" + createProduct.id());

        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).contentType(MediaType.APPLICATION_JSON).body(createProduct)

                ;

    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts(){ return ProductService.getAllProducts() ; }


    @PutMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> updateproduct(@PathVariable("productId") String productId,
                                           @RequestBody ProductRequest productrequest) {
        String updateProductId = ProductService.updateProduct(productId, productrequest);

        //SET THE LOCATION TO THE HEADERS
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location","/api/productId/" +  updateProductId);
        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }


    //@DeleteMapping
    //public ResponseEntity<?> deleteProduct(@PathVariable("productId") String productId) {
      //  ProductService.deleteproduct(productId);
        //return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable("productId") String productId) {
        ProductService.deleteProduct(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}


