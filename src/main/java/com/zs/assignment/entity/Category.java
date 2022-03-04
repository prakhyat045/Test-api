package com.zs.assignment.entity;

public class Category {
    int categoryId;
    String category;

    public Category(){

    }

    public Category(int categoryId, String category){
        this.categoryId = categoryId;
        this.category = category;
    }

    public int getCategoryId() {
        return this.categoryId;
    }

    public String getCategory(){
        return this.category;
    }
}
