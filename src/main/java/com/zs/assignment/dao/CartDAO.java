package com.zs.assignment.dao;

import com.zs.assignment.entity.Cart;
import com.zs.assignment.entity.IdMismatchException;
import com.zs.assignment.entity.Product;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class CartDAO {

    private static Properties loadPropertiesFile(){
        Properties properties = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream("src/main/resources/application.properties");
            properties.load(fileInputStream);
            fileInputStream.close();
        }
        catch (FileNotFoundException fileNotFoundException){
            System.out.println(fileNotFoundException.getMessage());
        }
        catch (IOException ioException){
            System.out.println(ioException.getMessage());
        }
        return properties;
    }

    Properties properties = loadPropertiesFile();

    private final String DB_URL = properties.getProperty("url");
    private final String USERNAME = properties.getProperty("userName");
    private final String PASSWORD = properties.getProperty("password");

    public boolean createCartRecord(Cart cart, int userId) throws SQLException, IdMismatchException{

        final String INSERT_CART_RECORD_QUERY = "INSERT INTO cart(id,name,category,price,quantity,user_id) values(?,?,?,?,?,?)";
        if(checkCartRecord(cart.getProductId(),userId)){
            throw new IdMismatchException("Product already exists in the cart");
        }
        try(Connection connection = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CART_RECORD_QUERY);
        ) {

            preparedStatement.setInt(1,cart.getProductId());
            preparedStatement.setString(2,cart.getName().toLowerCase());
            preparedStatement.setString(3, cart.getCategory().toLowerCase());
            preparedStatement.setFloat(4,cart.getPrice());
            preparedStatement.setInt(5,cart.getQuantity());
            preparedStatement.setInt(6,userId);
            if(preparedStatement.executeUpdate()>0){
                return true;
            }
        }
        return false;
    }

    public boolean updateCartRecord(int userId, int productId, int quantity) throws SQLException, IdMismatchException{
        final String UPDATE_CART_RECORD_QUERY = "UPDATE cart SET quantity = ? where id = ? and user_id = ?";
        try(Connection connection = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CART_RECORD_QUERY);){

            preparedStatement.setInt(1,quantity);
            preparedStatement.setInt(2,productId);
            preparedStatement.setInt(3,userId);
            if(preparedStatement.executeUpdate()==0){
                throw new IdMismatchException("Product not present in cart");
            }
        }
        return true;
    }

    public boolean deleteCartRecord(int productId)throws SQLException,IdMismatchException{
        final String DELETE_CART_RECORD_QUERY = "DELETE FROM cart WHERE user_id = ?";
        try(Connection connection = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_CART_RECORD_QUERY);){
            preparedStatement.setInt(1,productId);
            if(preparedStatement.executeUpdate()==0){
                throw new IdMismatchException("Cart already empty");
            }
        }
        return true;
    }

    public boolean deleteProductRecord(int userId, int productId)throws SQLException,IdMismatchException{
        final String DELETE_CART_RECORD_QUERY = "DELETE FROM cart WHERE user_id = ? and id = ?";
        try(Connection connection = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_CART_RECORD_QUERY);){
            preparedStatement.setInt(1,userId);
            preparedStatement.setInt(2,productId);
            if(preparedStatement.executeUpdate()==0){
                throw new IdMismatchException("Product not present in cart");
            }
        }
        return true;
    }

    public boolean checkCartRecord(int id, int userId) throws SQLException{
        final String CHECK_CART_RECORD_QUERY = "SELECT * FROM cart WHERE id = ? AND user_id = ?";
        try(Connection connection = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(CHECK_CART_RECORD_QUERY)){
            preparedStatement.setInt(1,id);
            preparedStatement.setInt(2,userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }
        }
        return false;
    }

    public ArrayList<Cart> fetchAllCartRecords(int userId) throws SQLException{
        ArrayList<Cart> cartList = new ArrayList<>();
        final String FETCH_ALL_RECORDS_QUERY = "SELECT * FROM cart where user_id = ?";
        try(Connection connection = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(FETCH_ALL_RECORDS_QUERY)){
            preparedStatement.setInt(1,userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                cartList.add(new Cart(new Product(resultSet.getInt("id"),resultSet.getString("name"),resultSet.getString("category"),resultSet.getFloat("price")),resultSet.getInt("quantity")));
            }
        }
        return cartList;
    }

}
