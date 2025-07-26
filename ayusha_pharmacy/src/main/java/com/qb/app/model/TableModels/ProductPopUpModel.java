package com.qb.app.model.TableModels;

import com.qb.app.model.entity.Product;

public class ProductPopUpModel {

    public Product product;

    public ProductPopUpModel() {
    }

    public ProductPopUpModel(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}
