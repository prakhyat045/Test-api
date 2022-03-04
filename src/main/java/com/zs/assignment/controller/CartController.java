package com.zs.assignment.controller;

import com.zs.assignment.entity.Cart;
import com.zs.assignment.entity.IdMismatchException;
import com.zs.assignment.service.CartService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;

@RestController
@RequestMapping("/e-commerce")
public class CartController {

    CartService cartService = new CartService();
    private final static Logger LOGGER = LogManager.getLogger(CartController.class.getName());

    @PostMapping("/cart")
    public ResponseEntity<String> addToCart(@RequestParam int userId, @RequestParam int productId, @RequestParam(defaultValue = "1") int quantity) {

        try {
            cartService.addToCart(userId,productId, quantity);
            LOGGER.info("Product record inserted successfully");
        } catch (IdMismatchException idMismatchException) {
            LOGGER.error(idMismatchException.getMessage());
            return ResponseEntity.status(400).body(idMismatchException.getMessage());
        } catch (SQLException sqlException) {
            LOGGER.error(sqlException.getMessage());
            return ResponseEntity.status(500).body("Something went wrong while connecting to database");
        } catch (IllegalArgumentException illegalArgumentException) {
            LOGGER.error(illegalArgumentException.getMessage());
            return ResponseEntity.status(400).body("Input format not right. Please try again");
        }

        return ResponseEntity.status(200).body("Product record created successfully");
    }

    @PutMapping("/cart")
    ResponseEntity<String> updateCart(@RequestParam int userId, @RequestParam int productId, @RequestParam(defaultValue = "1") int quantity) {

        try {
            cartService.updateCart(userId, productId, quantity);
            LOGGER.info("Product record inserted successfully");
        } catch (IdMismatchException idMismatchException) {
            LOGGER.error(idMismatchException.getMessage());
            return ResponseEntity.status(400).body(idMismatchException.getMessage());
        } catch (SQLException sqlException) {
            LOGGER.error(sqlException.getMessage());
            return ResponseEntity.status(500).body("Something went wrong while connecting to database");
        } catch (IllegalArgumentException illegalArgumentException) {
            LOGGER.error(illegalArgumentException.getMessage());
            return ResponseEntity.status(400).body("Input format not right. Please try again");
        }

        return ResponseEntity.status(200).body("Cart updated successfully");
    }


    @GetMapping("/cart")
    public ResponseEntity<ArrayList<Cart>> fetchCart(@RequestParam int userId) {
        ArrayList<Cart> cartList = new ArrayList<>();
        try {
            cartList = cartService.fetchAllCart(userId);
        } catch (SQLException sqlException) {
            LOGGER.error(sqlException.getMessage());
            return ResponseEntity.status(500).body(cartList);
        }catch (IllegalArgumentException illegalArgumentException){
            LOGGER.error(illegalArgumentException.getMessage());
            return ResponseEntity.status(400).body(cartList);
        }catch (IdMismatchException idMismatchException){
            LOGGER.error(idMismatchException.getMessage());
            return ResponseEntity.status(404).body(cartList);
        }

        return ResponseEntity.status(200).body(cartList);
    }

    @DeleteMapping("/cart")
    public ResponseEntity<String> deleteFromCart(@RequestParam int userId) throws SQLException{
        try{
            cartService.deleteFromCart(userId);
        } catch (IdMismatchException idMismatchException) {
            LOGGER.error(idMismatchException.getMessage());
            return ResponseEntity.status(400).body(idMismatchException.getMessage());
        } catch (SQLException sqlException) {
            LOGGER.error(sqlException.getMessage());
            return ResponseEntity.status(500).body("Something went wrong while connecting to database");
        } catch (IllegalArgumentException illegalArgumentException) {
            LOGGER.error(illegalArgumentException.getMessage());
            return ResponseEntity.status(400).body("Input format not right. Please try again");
        }
        return ResponseEntity.status(200).body("Cart emptied successfully");

    }

    @DeleteMapping("/cart/product")
    public ResponseEntity<String> deleteProductFromCart(@RequestParam int userId, @RequestParam int productId) throws SQLException{
        try{
            cartService.deleteProductFromCart(userId,productId);
        } catch (IdMismatchException idMismatchException) {
            LOGGER.error(idMismatchException.getMessage());
            return ResponseEntity.status(404).body(idMismatchException.getMessage());
        } catch (SQLException sqlException) {
            LOGGER.error(sqlException.getMessage());
            return ResponseEntity.status(500).body("Something went wrong while connecting to database");
        } catch (IllegalArgumentException illegalArgumentException) {
            LOGGER.error(illegalArgumentException.getMessage());
            return ResponseEntity.status(400).body("Input format not right. Please try again");
        }
        return ResponseEntity.status(200).body("Product removed successfully");

    }


}
