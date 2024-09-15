package com.example.vimca.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.vimca.Model.Product;
import com.example.vimca.service.ProductService;

@RestController
@CrossOrigin
@RequestMapping("/app/v1/product")
public class ProductController {
	
	
	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        logger.info("Request to create product with name: {}", product.getProductName());
        try {
            Product createdProduct = productService.createProduct(product);
            return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error occurred while creating product: {}", e.getMessage(), e);
            return new ResponseEntity<>("An error occurred while creating the product", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        logger.info("Request to update product with ID: {}", id);
        try {
            Product updatedProduct = productService.updateProduct(id, product);
            if (updatedProduct != null) {
            	updatedProduct.setUpdatedDate(LocalDateTime.now());

                return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Product not found for update", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Error occurred while updating product with ID: {}. Error: {}", id, e.getMessage(), e);
            return new ResponseEntity<>("An error occurred while updating the product", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        logger.info("Request to fetch product with ID: {}", id);
        try {
            Product product = productService.getProductById(id);
            if (product != null) {
                return new ResponseEntity<>(product, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Error occurred while fetching product with ID: {}. Error: {}", id, e.getMessage(), e);
            return new ResponseEntity<>("An error occurred while fetching the product", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all-active")
    public ResponseEntity<?> getAllActiveProducts() {
        logger.info("Request to fetch all active products");
        try {
            List<Product> products = (List<Product>) productService.findAllActiveProduct(false);
            if (!products.isEmpty()) {
                return new ResponseEntity<>(products, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No active products found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Error occurred while fetching all active products. Error: {}", e.getMessage(), e);
            return new ResponseEntity<>("An error occurred while fetching the products", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllProducts() {
        logger.info("Request to fetch all  products");
        try {
            List<Product> products = (List<Product>) productService.findAllProduct(false);
            if (!products.isEmpty()) {
                return new ResponseEntity<>(products, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No  products found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Error occurred while fetching all  products. Error: {}", e.getMessage());
            return new ResponseEntity<>("An error occurred while fetching the products", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        logger.info("Request to delete product with ID: {}", id);
        try {
            productService.deleteProductById(id);
            return new ResponseEntity<>("Product deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while deleting product with ID: {}. Error: {}", id, e.getMessage(), e);
            return new ResponseEntity<>("An error occurred while deleting the product", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}