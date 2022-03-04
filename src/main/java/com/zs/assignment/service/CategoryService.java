package com.zs.assignment.service;

import com.zs.assignment.dao.CategoryDAO;
import com.zs.assignment.entity.Category;
import com.zs.assignment.entity.CategoryMismatchException;
import com.zs.assignment.entity.IdMismatchException;

import java.sql.SQLException;
import java.util.ArrayList;

public class CategoryService {
    CategoryDAO categoryDAO;
    public CategoryService(){
        categoryDAO = new CategoryDAO();
    }

    public CategoryService(CategoryDAO categoryDAO){
        this.categoryDAO = categoryDAO;
    }

    public boolean addCategory(String category) throws SQLException, CategoryMismatchException, IllegalArgumentException {
        if(category.equals("")){
            throw new IllegalArgumentException("Not a valid category");
        }
        return categoryDAO.insertCategory(category);
    }

    public boolean updateCategory(Category category) throws SQLException, IllegalArgumentException, IdMismatchException {
        if(category.getCategoryId() < 1){
            throw new IllegalArgumentException("Category Id cannot be negative or zero");
        }
        else if(category.getCategory().equals("")){
            throw new IllegalArgumentException("Not a valid category");
        }

        return categoryDAO.updateCategory(category.getCategoryId(),category.getCategory());
    }

    public ArrayList<Category> getAllCategories() throws SQLException{
        return categoryDAO.fetchAllCategory();
    }

    public boolean deleteCategory(int categoryId) throws SQLException, IdMismatchException, IllegalArgumentException{
        if(categoryId<1){
            throw new IllegalArgumentException("Category Id cannot be negative or zero");
        }
        return categoryDAO.deleteCategoryRecord(categoryId);
    }

    public boolean checkCategory(String category) throws SQLException{
        return categoryDAO.checkCategoryExists(category);
    }
}
