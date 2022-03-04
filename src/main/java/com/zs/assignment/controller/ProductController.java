package com.zs.assignment.controller;

import com.zs.assignment.entity.CategoryMismatchException;
import com.zs.assignment.entity.IdMismatchException;
import com.zs.assignment.entity.Product;
import com.zs.assignment.service.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;

@RestController
@RequestMapping("/e-commerce")
public class ProductController {

    ProductService productService = new ProductService();
    private final static Logger LOGGER = LogManager.getLogger(ProductController.class.getName());

    @GetMapping("/products-inventory")
    public ResponseEntity<ArrayList<Product>> fetchProductsInventory() {
        ArrayList<Product> productList = new ArrayList<Product>();
        try {
            productList = productService.getAllProducts();
            LOGGER.info("Product list fetched successfully");
        }
        catch (SQLException sqlException){
            LOGGER.error(sqlException.getMessage());
            return ResponseEntity.status(500).body(productList);
        }
        return ResponseEntity.status(200).body(productList);
    }

    @GetMapping("/products-by-category")
    public ResponseEntity<ArrayList<Product>> fetchProductsByCategory(@RequestParam(defaultValue = "") String category){
        ArrayList<Product> productList = new ArrayList<>();
        try{
            productList = productService.getProductsByCategory(category);
            LOGGER.info("Product list as per category fetched successfully");
        }
        catch (SQLException sqlException){
            LOGGER.error(sqlException.getMessage());
            return ResponseEntity.status(500).body(productList);
        }
        return ResponseEntity.status(200).body(productList);


    }



    @PostMapping("/products")
    public ResponseEntity<String> createProduct(@RequestBody Product product){

        try {
            productService.createProduct(product);
            LOGGER.info("Product record inserted successfully");
        }
        catch (IdMismatchException idMismatchException){
            LOGGER.error(idMismatchException.getMessage());
            return ResponseEntity.status(400).body(idMismatchException.getMessage());
        }
        catch (SQLException sqlException){
            LOGGER.error(sqlException.getMessage());
            return ResponseEntity.status(500).body("Something went wrong while connecting to database");
        }
        catch (IllegalArgumentException illegalArgumentException){
            LOGGER.error(illegalArgumentException.getMessage());
            return ResponseEntity.status(400).body("Input format not right. Please try again");
        }
        catch (CategoryMismatchException categoryMismatchException){
            LOGGER.error(categoryMismatchException.getMessage());
            return ResponseEntity.status(400).body(categoryMismatchException.getMessage());
        }

        return ResponseEntity.status(200).body("Product record created successfully");
    }


    @PutMapping("/products")
    public ResponseEntity<String> updateProduct(@RequestBody Product product){
        try {
            productService.updateProduct(product.getProductId(),product);
        }
        catch (SQLException sqlException){
            LOGGER.error(sqlException.getMessage());
            return ResponseEntity.status(500).body("Something went wrong while connecting to database");
        }
        catch (IllegalArgumentException illegalArgumentException){
            LOGGER.error(illegalArgumentException.getMessage());
            return ResponseEntity.status(400).body(illegalArgumentException.getMessage());
        }
        catch(IdMismatchException idMismatchException){
            LOGGER.error(idMismatchException.getMessage());
            return ResponseEntity.status(400).body(idMismatchException.getMessage());
        }
        return ResponseEntity.status(200).body("Product record updated successfully");
    }
}
