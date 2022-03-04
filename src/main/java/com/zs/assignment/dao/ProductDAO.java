package com.zs.assignment.dao;

import com.zs.assignment.entity.IdMismatchException;
import com.zs.assignment.entity.Product;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class ProductDAO {


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

//    private static final String DRIVER = "org.postgresql.Driver";
//    private final String DB_URL = "jdbc:postgresql://localhost:5432/productdb1";
//    private final String USERNAME = "postgres";
//    private final String PASSWORD = "mypassword";

    /**
     * Inserts a record of product in database
     * @param product Instance of product class
     * @return boolean value to denote insertion of product
     * @throws SQLException in case of database connection error
     * @throws IdMismatchException in case if id already exists  in database
     */
    public boolean insertProduct(Product product) throws SQLException, IdMismatchException{
        final String PRODUCT_EXISTS_QUERY = "SELECT * from product where id = "+ product.getProductId();
        final String INSERT_PRODUCT_QUERY = "INSERT INTO product(id,name,category,price) values(?,?,?,?)";
        try(Connection connection = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PRODUCT_QUERY);
            Statement statement = connection.createStatement()){

            ResultSet resultSet = statement.executeQuery(PRODUCT_EXISTS_QUERY);
            if(resultSet.next()){
                throw new IdMismatchException("Cannot create product. Input product id already exists.");
            }

            preparedStatement.setInt(1,product.getProductId());
            preparedStatement.setString(2,product.getName().toLowerCase());
            preparedStatement.setString(3, product.getCategory().toLowerCase());
            preparedStatement.setFloat(4,product.getPrice());
            if(preparedStatement.executeUpdate()>0){
                return true;
            }
        }
        return false;
    }


    /**
     * Fetches all product records
     * @return list containing all product records
     * @throws SQLException in case of database connection error
     */
    public ArrayList<Product> fetchAllProducts() throws SQLException{
        final String FETCH_ALL_PRODUCT_QUERY = "SELECT * from product";
        ArrayList<Product> productList = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(FETCH_ALL_PRODUCT_QUERY)
        ){
            while(resultSet.next()){
                Product product = new Product(resultSet.getInt("id"),resultSet.getString("name"), resultSet.getString("category"), resultSet.getFloat("price"));
                productList.add(product);
            }
        }
        return productList;
    }

    /**
     * Updates a record in database
     * @param productId integer representing product id
     * @param product instance of product class
     * @return boolean value to denote update of product database
     * @throws SQLException in case of database connection error
     * @throws IdMismatchException in case if id does not exist
     */
    public boolean updateProduct(int productId, Product product) throws SQLException, IdMismatchException{
        final String UPDATE_PRODUCT_QUERY = "UPDATE product SET name = ?, category = ?, price = ? where id = ?";
        try(Connection connection = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PRODUCT_QUERY)){
            preparedStatement.setString(1,product.getName().toLowerCase());
            preparedStatement.setString(2,product.getCategory().toLowerCase());
            preparedStatement.setFloat(3,product.getPrice());
            preparedStatement.setInt(4,productId);
            if(preparedStatement.executeUpdate()==0){
                throw new IdMismatchException("Cannot update product. Input product id does not exists.");
            }
        }
        return true;
    }


    /**
     * Fetches all product records from a particular category
     * @param category string denoting product category
     * @return list containg product of a particular category
     * @throws SQLException in case of database connection error
     */
    public ArrayList<Product> fetchProductsByCategory(String category) throws SQLException{
        final String FETCH_PRODUCTS_BY_CATEGORY_QUERY = "SELECT * from product where lower(category) = ?";
        ArrayList<Product> productList = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(FETCH_PRODUCTS_BY_CATEGORY_QUERY);
        ){
            preparedStatement.setString(1,category.toLowerCase());
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Product product = new Product(resultSet.getInt("id"),resultSet.getString("name"), resultSet.getString("category"), resultSet.getFloat("price"));
                productList.add(product);
            }
        }
        return productList;
    }

    /**
     * Fetches all product categories
     * @return list containing all categories
     * @throws SQLException in case of database connection error
     */
    public ArrayList<String> fetchAllCategories() throws SQLException{
        final String FETCH_ALL_CATEGORY_QUERY = "SELECT DISTINCT category from product";
        ArrayList<String> categoryList = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(FETCH_ALL_CATEGORY_QUERY)
        ){
            while (resultSet.next()){
                categoryList.add(resultSet.getString("category"));
            }
        }
        return categoryList;
    }

    public Product getProductById(int productId) throws SQLException, IdMismatchException{
        Product product;
        final String GET_PRODUCT_BY_QUERY = "SELECT * FROM product WHERE id = ?";
        try(Connection connection = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(GET_PRODUCT_BY_QUERY);){
            preparedStatement.setInt(1,productId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.next()){
                throw new IdMismatchException("Product does not exist in the inventory");
            }
            product = new Product(resultSet.getInt("id"),resultSet.getString("name"),resultSet.getString("category"),resultSet.getFloat("price"));
        }
        return product;
    }


}
