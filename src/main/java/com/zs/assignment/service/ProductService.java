package com.zs.assignment.service;

import com.zs.assignment.dao.ProductDAO;
import com.zs.assignment.entity.CategoryMismatchException;
import com.zs.assignment.entity.IdMismatchException;
import com.zs.assignment.entity.Product;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductService {

    ProductDAO productDAO;

    public ProductService(){
        productDAO = new ProductDAO();
    }

    public ProductService(ProductDAO productDAO){
        this.productDAO = productDAO;
    }

    /**
     * Fetches all products
     * @return list containing all product records
     * @throws SQLException in case of database related operation error
     */
    public ArrayList<Product> getAllProducts() throws SQLException{
        return productDAO.fetchAllProducts();
    }

    /**
     * Fetches products according to category
     * @param category string denoting product category
     * @return list containing product according to category
     * @throws SQLException in case of database related operation error
     */
    public ArrayList<Product> getProductsByCategory(String category) throws SQLException{
        return productDAO.fetchProductsByCategory(category);
    }

    /**
     * Fetches all product categories
     * @return list containing all categories
     * @throws SQLException in case of database connection error
     */
    public ArrayList<String> getAllCategories() throws SQLException{
        return productDAO.fetchAllCategories();
    }

    /**
     * Creates a product record if input format is valid
     * @param product instance of product class
     * @return boolean value denoting creation of product record
     * @throws SQLException in case of database related operation error
     * @throws IllegalArgumentException in case input format is not valid for insertion
     * @throws IdMismatchException in case id already exists in database
     */
    public boolean createProduct(Product product) throws SQLException,IllegalArgumentException, IdMismatchException, CategoryMismatchException {
        if(product == null || product.getName().equals("") || product.getPrice()<0 || product.getCategory().equals("") || product.getProductId()<1){
            throw new IllegalArgumentException("Input format not right. Please try again");
        }
        CategoryService categoryService = new CategoryService();
        if(!categoryService.checkCategory(product.getCategory())){
            throw new CategoryMismatchException("Cannot create product as given category does not exist");
        }
        return productDAO.insertProduct(product);
    }

    /**
     * Updates a product record if input format is valid
     * @param product instance of product class
     * @return boolean value denoting update of product record
     * @throws SQLException in case of database related operation error
     * @throws IllegalArgumentException in case input format is not valid for update
     * @throws IdMismatchException in case id does not exists in database
     */
    public boolean updateProduct(int productId, Product product) throws SQLException,IllegalArgumentException, IdMismatchException {
        if(product == null || product.getName().equals("") || product.getPrice()<0 || product.getCategory().equals("") || product.getProductId()<1){
            throw new IllegalArgumentException("Input format not right. Please try again");
        }
        return productDAO.updateProduct(productId,product);
    }

    public Product getProductById(int productId) throws SQLException, IdMismatchException{
        return productDAO.getProductById(productId);
    }


}
