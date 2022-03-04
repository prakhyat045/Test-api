package com.zs.assignment.dao;

import com.zs.assignment.entity.Category;
import com.zs.assignment.entity.CategoryMismatchException;
import com.zs.assignment.entity.IdMismatchException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Properties;

public class CategoryDAO {


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


//    private final String DB_URL = "jdbc:postgresql://localhost:5432/productdb1";
//    private final String USERNAME = "postgres";
//    private final String PASSWORD = "mypassword";

//    public static void main(String[] args) throws CategoryMismatchException {
//        try {
//            CategoryDAO categoryDAO = new CategoryDAO();
//            categoryDAO.insertCategory("electronics");
//        }
//        catch (SQLException sqlException){
//            sqlException.printStackTrace();
//        }
//    }

    public boolean insertCategory(String category) throws SQLException, CategoryMismatchException{
        if(checkCategoryExists(category)){
            throw new CategoryMismatchException("Category already exists");
        }
        final String INSERT_CATEGORY_QUERY = "INSERT INTO category(category) values(?)";
        try(Connection connection = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CATEGORY_QUERY);){
            preparedStatement.setString(1,category.toLowerCase());
            if(preparedStatement.executeUpdate()==0){
                return false;
            }
        }
        return true;
    }

    public boolean updateCategory(int categoryId, String category)throws SQLException, IdMismatchException{
        final String UPDATE_CATEGORY_QUERY = "UPDATE category SET category = ? WHERE category_id = ?";
        try(Connection connection = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CATEGORY_QUERY)){
            preparedStatement.setString(1,category.toLowerCase());
            preparedStatement.setInt(2,categoryId);
            if(preparedStatement.executeUpdate() == 0){
                throw new IdMismatchException("Category does not exist");
            }
        }
        return true;
    }

    public ArrayList<Category> fetchAllCategory() throws SQLException{
        final String FETCH_ALL_CATEGORY_QUERY = "SELECT * FROM category";
        ArrayList<Category> categoryList = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
            Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(FETCH_ALL_CATEGORY_QUERY);
            while(resultSet.next()){
                categoryList.add(new Category(resultSet.getInt("category_id"),resultSet.getString("category")));
            }
        }
        return categoryList;
    }

    public boolean deleteCategoryRecord(int categoryId) throws SQLException, IdMismatchException{
        final String DELETE_CATEGORY_QUERY = "DELETE FROM category WHERE category_id = ?";
        try(Connection connection = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_CATEGORY_QUERY)){
            preparedStatement.setInt(1,categoryId);
            if(preparedStatement.executeUpdate()==0){
                throw new IdMismatchException("Category does not exist");
            }
        }
        return true;

    }

    public boolean checkCategoryExists(String category) throws SQLException {
        final String CHECK_CATEGORY_EXISTS_QUERY = "SELECT * FROM category where category = ?";
        try(Connection connection = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(CHECK_CATEGORY_EXISTS_QUERY)){
            preparedStatement.setString(1,category);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.next()){
                return false;
            }
        }
        return true;
    }
}
