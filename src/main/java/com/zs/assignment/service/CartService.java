package com.zs.assignment.service;

import com.zs.assignment.dao.CartDAO;
import com.zs.assignment.entity.Cart;
import com.zs.assignment.entity.IdMismatchException;
import com.zs.assignment.entity.Product;

import java.sql.SQLException;
import java.util.ArrayList;

public class CartService {

    CartDAO cartDAO;
    ProductService productService;

    public CartService(){
        cartDAO = new CartDAO();
        productService = new ProductService();
    }

    public CartService(CartDAO cartDAO){
        this.cartDAO = cartDAO;
        productService = new ProductService();
    }

    public boolean addToCart(int userId, int productId, int quantity) throws SQLException, IdMismatchException, IllegalArgumentException {
        if(productId < 1){
            throw new IllegalArgumentException("Product id cannot be negative or zero");
        }
        else if(quantity < 1){
            throw new IllegalArgumentException("Quantity must be at least 1");
        }
        else if(userId < 1){
            throw new IllegalArgumentException("User id cannot be negative or zero");
        }
        Product product = productService.getProductById(productId);
        return cartDAO.createCartRecord(new Cart(product,quantity),userId);
    }

    public boolean updateCart(int userId, int productId, int quantity) throws SQLException, IdMismatchException, IllegalArgumentException{
        if(productId < 1){
            throw new IllegalArgumentException("Product id cannot be negative or zero");
        }
        else if(quantity < 1){
            throw new IllegalArgumentException("Quantity must be at least 1");
        }
        else if(userId < 1){
            throw new IllegalArgumentException("User id cannot be negative or zero");
        }
        return cartDAO.updateCartRecord(userId,productId,quantity);
    }

    public boolean deleteFromCart(int userId) throws SQLException, IllegalArgumentException, IdMismatchException{
        if(userId < 1){
            throw new IllegalArgumentException("User id cannot be negative or zero");
        }
        return cartDAO.deleteCartRecord(userId);
    }


    public boolean deleteProductFromCart(int userId, int productId) throws SQLException, IllegalArgumentException, IdMismatchException{
        if(userId < 1){
            throw new IllegalArgumentException("User id cannot be negative or zero");
        }
        else if(productId<1){
            throw  new IllegalArgumentException("Product Id cannot be negative or zero");
        }
        return cartDAO.deleteProductRecord(userId,productId);
    }

    public ArrayList<Cart> fetchAllCart(int userId) throws SQLException, IllegalArgumentException, IdMismatchException{
        ArrayList<Cart> cartArrayList;
        if(userId < 1){
            throw new IllegalArgumentException("User id cannot be negative");
        }
        cartArrayList = cartDAO.fetchAllCartRecords(userId);
        if(cartArrayList.size()==0){
            throw new IdMismatchException("Cart for user id does not exists.");
        }
        return cartArrayList;
    }





}
