package com.zs.assignment.controller;


import com.zs.assignment.entity.Category;
import com.zs.assignment.entity.CategoryMismatchException;
import com.zs.assignment.entity.IdMismatchException;
import com.zs.assignment.entity.Product;
import com.zs.assignment.service.CategoryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;

@RestController
@RequestMapping("/e-commerce")
public class CategoryController {

    CategoryService categoryService = new CategoryService();
    private final static Logger LOGGER = LogManager.getLogger(ProductController.class.getName());

    @GetMapping("/categories")
    public ResponseEntity<ArrayList<Category>> fetchCategories(){
        ArrayList<Category> categoriesList = new ArrayList<>();
        try{
            categoriesList = categoryService.getAllCategories();
            LOGGER.info("Category list fetched successfully");
        }
        catch (SQLException sqlException){
            LOGGER.error(sqlException.getMessage());
            return ResponseEntity.status(500).body(categoriesList);
        }
        return ResponseEntity.status(200).body(categoriesList);
    }

    @PostMapping("/categories")
    public ResponseEntity<String> createProduct(@RequestBody String category){

        try {
            categoryService.addCategory(category);
            LOGGER.info("Product record inserted successfully");
        }
        catch (CategoryMismatchException categoryMismatchException){
            LOGGER.error(categoryMismatchException.getMessage());
            return ResponseEntity.status(400).body(categoryMismatchException.getMessage());
        }
        catch (SQLException sqlException){
            LOGGER.error(sqlException.getMessage());
            return ResponseEntity.status(500).body("Something went wrong while connecting to database");
        }
        catch (IllegalArgumentException illegalArgumentException){
            LOGGER.error(illegalArgumentException.getMessage());
            return ResponseEntity.status(400).body(illegalArgumentException.getMessage());
        }

        return ResponseEntity.status(200).body("Category added successfully");
    }

    @PutMapping("/category")
    ResponseEntity<String> updateCart(@RequestBody Category category) {

        try {
            categoryService.updateCategory(category);
            LOGGER.info("Product record inserted successfully");
        } catch (IdMismatchException idMismatchException) {
            LOGGER.error(idMismatchException.getMessage());
            return ResponseEntity.status(400).body(idMismatchException.getMessage());
        } catch (SQLException sqlException) {
            LOGGER.error(sqlException.getMessage());
            return ResponseEntity.status(500).body("Something went wrong while connecting to database");
        } catch (IllegalArgumentException illegalArgumentException) {
            LOGGER.error(illegalArgumentException.getMessage());
            return ResponseEntity.status(400).body(illegalArgumentException.getMessage());
        }

        return ResponseEntity.status(200).body("Category updated successfully");
    }

    @DeleteMapping("/category")
    public ResponseEntity<String> deleteFromCart(@RequestParam int categoryId) throws SQLException{
        try{
            categoryService.deleteCategory(categoryId);
        } catch (IdMismatchException idMismatchException) {
            LOGGER.error(idMismatchException.getMessage());
            return ResponseEntity.status(400).body(idMismatchException.getMessage());
        } catch (SQLException sqlException) {
            LOGGER.error(sqlException.getMessage());
            return ResponseEntity.status(500).body("Something went wrong while connecting to database");
        } catch (IllegalArgumentException illegalArgumentException) {
            LOGGER.error(illegalArgumentException.getMessage());
            return ResponseEntity.status(400).body(illegalArgumentException.getMessage());
        }
        return ResponseEntity.status(200).body("Category removed successfully");

    }


}
