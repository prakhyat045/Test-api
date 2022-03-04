package com.zs.assignment.entity;

public class Cart extends Product{
    int quantity;
    public Cart(Product product, int quantity){
        super(product.getProductId(),product.getName(),product.getCategory(),product.getPrice());
        this.quantity = quantity;
    }

    public int getQuantity(){
        return this.quantity;
    }

}
