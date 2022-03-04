package com.zs.assignment.entity;

public class Product {

    private int productId = -1;
    private String name = "";
    private String category = "";
    private float price = -1;

    public Product() {

    }

    public Product(String name, String categoryName, float price) {
        this.name = name;
        this.category = categoryName;
        this.price = price;
    }

    public Product(int id, String name, String categoryName, float price) {
        this(name, categoryName, price);
        this.productId = id;
    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public float getPrice() {
        return price;
    }

}
